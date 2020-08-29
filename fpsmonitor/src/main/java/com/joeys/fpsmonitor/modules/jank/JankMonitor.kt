package com.joeys.fpsmonitor.modules.jank

import android.app.Application
import android.os.Looper
import com.joeys.fpsmonitor.modules.Instruments

class JankMonitor : Instruments {

    private var app: Application? = null
    private var stackAnalyzer: StackAnalyzer? = null
    private var jankLooperWatcher: JankLooperWatcher? = null

    override fun install(application: Application) {
        app = application
        stackAnalyzer = StackAnalyzer()
        jankLooperWatcher = JankLooperWatcher(
            onEventStart = {
                stackAnalyzer?.startAnalysis()
            },
            onEventEnd = {
                stackAnalyzer?.stopAnalysis()
            },
            onEventJank = { jankTimes, isLongBlock ->

            })
        Looper.getMainLooper().setMessageLogging(jankLooperWatcher)
    }

    override fun start() {

    }

    override fun stop() {
    }



}