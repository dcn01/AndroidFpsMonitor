package com.joeys.fpsmonitor

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.concurrent.atomic.AtomicInteger

abstract class ForegroundLifecycleCallback : Application.ActivityLifecycleCallbacks {
    abstract fun onAppForeground()
    abstract fun onAppBackground()

    private var foregroundActivityCount = AtomicInteger(0)


    override fun onActivityStarted(activity: Activity) {
        val count = foregroundActivityCount.incrementAndGet()
        if (count == 1) {
            onAppForeground()
        }
    }

    override fun onActivityStopped(activity: Activity) {
        val count = foregroundActivityCount.decrementAndGet()
        if (count == 0) {
            onAppBackground()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}