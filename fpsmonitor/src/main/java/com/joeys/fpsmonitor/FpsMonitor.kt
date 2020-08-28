package com.joeys.fpsmonitor

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.RelativeLayout


object FpsMonitor {

    private val core = Core()


    //门面模式，隐藏所有细节
    fun install(application: Application): Core {
        return core.install(application)
    }


    class Core : ForegroundLifecycleCallback() {
        private val layoutParams: LayoutParams = LayoutParams()
        private var app: Application? = null
        private var rootView: View? = null
        private var textView: View? = null
        private var windowManager: WindowManager? = null
        private val monitor = Monitor()

        fun install(application: Application): Core {
            app = application
            application.registerActivityLifecycleCallbacks(this)
            windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            layoutParams.apply {
                width = LayoutParams.WRAP_CONTENT
                height = LayoutParams.WRAP_CONTENT
                flags =
                    LayoutParams.FLAG_KEEP_SCREEN_ON or LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_NOT_TOUCHABLE
                format = PixelFormat.TRANSLUCENT
                gravity = Gravity.BOTTOM
                x = 10
                if (Build.VERSION.SDK_INT >= 26)
                    type = LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    type = LayoutParams.TYPE_TOAST
            }
            val inflater = LayoutInflater.from(application)
            rootView = inflater.inflate(R.layout.text_layout, RelativeLayout(application))
            textView = rootView?.findViewById(R.id.textview_watch)
            watch {

            }
            return this
        }

        override fun onAppForeground() {
            show()
        }

        override fun onAppBackground() {
            stop()
        }

        fun show() {
            if (!hasOverlayPermission()) {
                startOverlaySettingActivity()
                return
            }
            monitor.start()
        }

        fun stop() {
            monitor.stop()
        }

        private fun watch(fpsWatch: FpsWatch) {
            monitor.watch(fpsWatch)
        }

        private fun startOverlaySettingActivity() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                app!!.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + app!!.packageName)
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }

        private fun hasOverlayPermission(): Boolean {
            return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(app)
        }


    }
}