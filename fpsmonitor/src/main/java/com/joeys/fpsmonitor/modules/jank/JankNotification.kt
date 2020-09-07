package com.joeys.fpsmonitor.modules.jank

import android.annotation.TargetApi
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.joeys.fpsmonitor.R

class JankNotification(private val application: Application) {
    private val channelId = "jank"
    private val channelName = "Jank Notification"
    private val groupId = "jank"
    private val groupName = "jank"
    private val manager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var builder: NotificationCompat.Builder
    private var id = 0

    fun init() {
        if (Build.VERSION.SDK_INT >= 26) {
            createChannel(groupId, groupName, channelId, channelName)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(application, channelId)
        } else {
            builder = NotificationCompat.Builder(application, channelId)
            builder.priority = NotificationCompat.PRIORITY_HIGH
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Jank happended!")
            .setOngoing(false)
            .setAutoCancel(false)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
    }

    fun notificate(jankInfo: JankInfo) {
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(jankInfo.toString()))

        manager.notify(id++, builder.build())
    }


    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(
        groupId: String,
        groupName: String,
        channelId: String,
        channelName: String
    ) {

        val group = NotificationChannelGroup(groupId, groupName)
        manager.createNotificationChannelGroup(group)

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.canBypassDnd()
        channel.lockscreenVisibility = NotificationCompat.VISIBILITY_SECRET
        channel.canShowBadge()
        channel.group = groupId
        channel.enableVibration(false)
        channel.enableLights(false)
        channel.vibrationPattern = longArrayOf(0)
        channel.setSound(null, null)
        manager.createNotificationChannel(channel)

    }

}