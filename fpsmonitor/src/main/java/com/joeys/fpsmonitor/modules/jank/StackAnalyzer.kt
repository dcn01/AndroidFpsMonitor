package com.joeys.fpsmonitor.modules.jank

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.atomic.AtomicBoolean

class StackAnalyzer(private val thread: Thread) {
    private val mShouldSample = AtomicBoolean(false)
    private var sampleInterval: Long = 100
    private var sampleDelay: Long = 100
    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            doSample()
            if (mShouldSample.get()) {
                analyzerHandler.postDelayed(this, sampleInterval)
            }
        }
    }

    private fun doSample() {
        synchronized(stackTraces) {
            if (stackTraces.size >= MAX_TRACE_COUNT) {
                stackTraces.remove(stackTraces.keys.first())
            }
            stackTraces[System.currentTimeMillis()] = thread.stackTrace
        }
    }

    fun startAnalysis() {
        if (mShouldSample.getAndSet(true)) {
            return
        }
        analyzerHandler.removeCallbacks(mRunnable)
        analyzerHandler.postDelayed(mRunnable, sampleDelay)
    }

    fun stopAnalysis() {
        if (!mShouldSample.getAndSet(false)) {
            return
        }
        analyzerHandler.removeCallbacks(mRunnable)
    }

    fun getTrace(startTime: Long, endTime: Long): Map<Long, Array<StackTraceElement>> {
        synchronized(stackTraces) {
            if (stackTraces.size == 0) {
                return mapOf()
            }
            return stackTraces.filter {
                it.key in startTime..endTime
            }
        }
    }

    companion object {
        const val MAX_TRACE_COUNT = 30
        val analyzerHandler: Handler by lazy {
            val thread = HandlerThread("analyzer")
            thread.start()
            Handler(thread.looper)
        }
        val stackTraces = linkedMapOf<Long, Array<StackTraceElement>>()
    }
}