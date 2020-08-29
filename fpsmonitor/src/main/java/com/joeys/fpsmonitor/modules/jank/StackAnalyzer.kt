package com.joeys.fpsmonitor.modules.jank

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.atomic.AtomicBoolean

class StackAnalyzer {
    private val mShouldSample = AtomicBoolean(false)
    var sampleInterval: Long = 100
    var sampleDelay: Long = 0

    fun startAnalysis() {
        if (mShouldSample.getAndSet(true)) {
            return
        }
    }

    fun stopAnalysis() {
        if (!mShouldSample.getAndSet(false)) {
            return
        }
    }

    companion object {
        val analyzerHandler: Handler by lazy { Handler(HandlerThread("analyzer").looper) }
    }
}