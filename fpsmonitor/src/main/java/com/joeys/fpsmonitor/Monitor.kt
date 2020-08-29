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
import android.widget.TextView
import com.joeys.fpsmonitor.modules.Instruments
import com.joeys.fpsmonitor.modules.fps.FpsWatch
import com.joeys.fpsmonitor.modules.fps.FpsMonitor
import com.joeys.fpsmonitor.modules.jank.JankMonitor
import java.text.DecimalFormat


object Monitor {

    private val core = Core()

    fun install(application: Application): Core {
        return core.install(application)
    }

    class Core : ForegroundLifecycleCallback() {
        private var app: Application? = null
        private val instruments = mutableListOf<Instruments>(
            FpsMonitor(),
            JankMonitor()
        )

        fun install(application: Application): Core {
            app = application
            application.registerActivityLifecycleCallbacks(this)
            for (instrument in instruments) {
                instrument.install(application)
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
            for (instrument in instruments) {
                instrument.start()
            }
        }

        fun stop() {
            for (instrument in instruments) {
                instrument.stop()
            }
        }
    }
}