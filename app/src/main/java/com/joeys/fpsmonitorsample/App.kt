package com.joeys.fpsmonitorsample

import android.app.Application
import com.joeys.fpsmonitor.Monitor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Monitor.install(this)
    }
}