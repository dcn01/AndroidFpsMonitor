package com.joeys.fpsmonitor

import android.view.Choreographer
import java.util.concurrent.TimeUnit

class Monitor : Choreographer.FrameCallback {

    private val choreographer = Choreographer.getInstance()
    private var frames = 0
    private var frameTimes = 0L
    private val interval = 500
    private var fpsWatch: FpsWatch? = null

    fun start() {
        choreographer.postFrameCallback(this)
    }

    fun stop() {
        frames = 0
        frameTimes = 0L
        choreographer.removeFrameCallback(this)
    }

    fun watch(fpsWatch: FpsWatch) {
        this.fpsWatch = fpsWatch
    }

    override fun doFrame(frameTimeNanos: Long) {
        val current = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos)
        if (frameTimes > 0) {
            frames++
            val spend = current - frameTimes
            if (spend > interval) {
                val fps = frames * 1000 / spend.toDouble()
                frames = 0
                frameTimes = 0
                fpsWatch?.onFps(fps)
            }

        } else {
            frameTimes = current
        }
        choreographer.postFrameCallback(this)
    }
}