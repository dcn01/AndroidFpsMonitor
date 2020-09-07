package com.joeys.fpsmonitor.modules.fps

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
import android.widget.RelativeLayout
import android.widget.TextView
import com.joeys.fpsmonitor.R
import java.text.DecimalFormat

class FpsDisplayWatcher : FpsWatch {

    private val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
    private var app: Application? = null
    private var rootView: View? = null
    private var textView: TextView? = null
    private var windowManager: WindowManager? = null
    private val decimal = DecimalFormat("#.0' fps'")
    private var isShowing = false

    fun init(application: Application) {
        app = application
        windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        layoutParams.apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags =
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.BOTTOM
            x = 10
            if (Build.VERSION.SDK_INT >= 26)
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                type = WindowManager.LayoutParams.TYPE_TOAST
        }
        val inflater = LayoutInflater.from(application)
        rootView = inflater.inflate(R.layout.text_layout, RelativeLayout(application))
        textView = rootView?.findViewById(R.id.textview_watch)
    }

    override fun onFps(fps: Double) {
        textView?.let {
            it.text = decimal.format(fps)
        }
    }

    override fun start() {
        if (!hasOverlayPermission()) {
            startOverlaySettingActivity()
            return
        }
        if (!isShowing) {
            windowManager?.addView(rootView, layoutParams)
            isShowing = true
        }
    }

    override fun stop() {
        if (isShowing) {
            windowManager?.removeView(rootView)
            isShowing = false
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

    fun refreshLayout(fpsLayoutParams: WindowManager.LayoutParams.() -> Unit) {

    }


}