package com.joeys.fpsmonitor.modules.fps

interface FpsWatch {
    fun onFps(fps: Double)
    fun start()
    fun stop()
}