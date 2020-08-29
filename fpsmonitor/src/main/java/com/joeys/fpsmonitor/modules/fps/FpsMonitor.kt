package com.joeys.fpsmonitor.modules.fps

import android.app.Application
import android.view.Choreographer
import androidx.annotation.IntRange
import com.joeys.fpsmonitor.modules.Instruments
import java.util.concurrent.TimeUnit

class FpsMonitor : Choreographer.FrameCallback, Instruments {


    /**
     * fps sampling period , default is 500ms
     * fps采样周期，默认500ms一次
     */
    @IntRange(from = 100, to = 1000)
    var interval = 500
    private val choreographer = Choreographer.getInstance()

    /**
     * draw how many frames in one interval
     * 当前周期内，一共绘制了frames帧
     */
    private var frames = 0
    private var intervalStartTime = 0L
    private var fpsWatch = mutableListOf<FpsWatch>()


    override fun install(application: Application) {
        val fpsDisplayWatcher = FpsDisplayWatcher()
        fpsDisplayWatcher.init(application)
        addWatch(fpsDisplayWatcher)
    }

    override fun start() {
        choreographer.postFrameCallback(this)
        for (watch in fpsWatch) {
            watch.start()
        }
    }

    override fun stop() {
        frames = 0
        intervalStartTime = 0L
        choreographer.removeFrameCallback(this)
        for (watch in fpsWatch) {
            watch.stop()
        }
    }

    fun addWatch(fpsWatch: FpsWatch) {
        this.fpsWatch.add(fpsWatch)
    }

    fun removeWatch(fpsWatch: FpsWatch) {
        this.fpsWatch.remove(fpsWatch)
    }

    override fun doFrame(frameTimeNanos: Long) {
        val current = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos)
        if (intervalStartTime > 0) {
            frames++
            val spend = current - intervalStartTime
            if (spend > interval) {
                val fps = frames * 1000 / spend.toDouble()
                frames = 0
                intervalStartTime = 0
                for (watch in fpsWatch) {
                    watch.onFps(fps)
                }
            }
        } else {
            intervalStartTime = current
        }
        choreographer.postFrameCallback(this)
    }


}