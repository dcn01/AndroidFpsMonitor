package com.joeys.fpsmonitor.modules.jank

import android.app.Application
import android.os.Looper
import android.util.Log
import com.joeys.fpsmonitor.modules.Instruments

class JankMonitor : Instruments {

    private var app: Application? = null
    private var stackAnalyzer: StackAnalyzer? = null
    private var jankLooperWatcher: JankLooperWatcher? = null


    override fun install(application: Application) {
        app = application
        stackAnalyzer = StackAnalyzer(Looper.getMainLooper().thread)
        jankLooperWatcher = JankLooperWatcher(
            onEventStart = {
                stackAnalyzer?.startAnalysis()
            },
            onEventEnd = {
                stackAnalyzer?.stopAnalysis()
            },
            onEventJank = { jankTimes: Long, startTime: Long, endTime: Long, isLongBlock: Boolean ->
                val traces = stackAnalyzer?.getTrace(startTime, endTime)
                traces?.forEach {
                    val jankInfo = JankInfo(jankTimes,startTime, endTime, it.value)
                    Log.w("monitor", jankInfo.toString())
                }
            })
        Looper.getMainLooper().setMessageLogging(jankLooperWatcher)
    }

    override fun start() {

    }

    override fun stop() {
    }


}