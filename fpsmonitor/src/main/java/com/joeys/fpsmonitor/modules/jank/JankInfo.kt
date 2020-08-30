package com.joeys.fpsmonitor.modules.jank

import androidx.annotation.Keep

@Keep
class JankInfo(
    val jankSpend: Long,
    val startTime: Long,
    val endTime: Long,
    val stackTraces: List<StackTraceElement>
) {

    override fun toString(): String {
        val result = StringBuilder()
        result.append("MainThread doing too much job! JankInfo startTime:$startTime   endTime:$endTime jankSpend:${jankSpend}ms\n")
        for (stackTrace in stackTraces) {
            result.append("\t$stackTrace \n")
        }
        return result.toString()
    }

}