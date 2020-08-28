package com.joeys.fpsmonitor

import android.app.Application
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager.LayoutParams


object FpsMonitor {

    private val core = Core()

    //门面模式，隐藏所有细节
    fun install(application: Application): Core {
        return core.install(application)
    }


    class Core : ForegroundLifecycleCallback() {
        private val layoutParams: LayoutParams = LayoutParams()
        private var app: Application? = null

        fun install(application: Application): Core {
            app = application
            application.registerActivityLifecycleCallbacks(this)

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

        fun stop() {

        }

    }
}