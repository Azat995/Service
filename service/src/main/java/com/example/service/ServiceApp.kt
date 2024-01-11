package com.example.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class ServiceApp : Application() {

    companion object {
        lateinit var instance: ServiceApp
            private set

        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        createNotificationChannel(PushChannelType.DEFAULT.channelId, "Default")
        createNotificationChannel(PushChannelType.MESSAGES.channelId, "Messages")
    }

    private fun createNotificationChannel(channelID: String, channelName: String) {
        val vibrationPattern = longArrayOf(500, 500, 500)
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Example notification"
            setVibrationPattern(vibrationPattern)
            enableVibration(true)
            enableLights(true)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}