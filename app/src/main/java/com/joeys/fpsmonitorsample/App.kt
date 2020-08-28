package com.joeys.fpsmonitorsample

import android.app.Application
import com.joeys.fpsmonitor.FpsMonitor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FpsMonitor.install(this)
    }
}