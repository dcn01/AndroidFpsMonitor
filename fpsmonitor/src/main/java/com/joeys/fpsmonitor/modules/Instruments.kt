package com.joeys.fpsmonitor.modules

import android.app.Application

interface Instruments {
    fun install(application: Application)
    fun start()
    fun stop()
}