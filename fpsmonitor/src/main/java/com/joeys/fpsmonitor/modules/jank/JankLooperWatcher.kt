package com.joeys.fpsmonitor.modules.jank

import android.os.SystemClock
import android.util.Printer

class JankLooperWatcher(
    private val onEventStart: (startTime: Long) -> Unit,
    private val onEventEnd: (endTime: Long) -> Unit,
    private val onEventJank: (jankTimes: Long, isLongBlock: Boolean) -> Unit
) : Printer {

    private var eventStart = false
    private val longBlockThresholdMillis: Long = 500
    private val shortBlockThresholdMillis: Long = 100
    private var thisEventStartTime: Long = 0
    private var thisEventStartThreadTime: Long = 0

    override fun println(x: String?) {
        if (!eventStart) { // 事件开始
            thisEventStartTime = System.currentTimeMillis()
            thisEventStartThreadTime = SystemClock.currentThreadTimeMillis()
            eventStart = true
            onEventStart(thisEventStartTime)
        } else {
            val thisEventEndTime = System.currentTimeMillis()
            val thisEventThreadEndTime = SystemClock.currentThreadTimeMillis()
            eventStart = false
            val eventCostTime: Long = thisEventEndTime - thisEventStartTime
            val eventCostThreadTime: Long = thisEventThreadEndTime - thisEventStartThreadTime
            if (eventCostTime >= longBlockThresholdMillis) {
                onEventJank(eventCostTime, true)
            } else if (eventCostTime >= shortBlockThresholdMillis) {
                onEventJank(eventCostTime, false)
            }
            onEventEnd(thisEventEndTime)
        }
    }
}