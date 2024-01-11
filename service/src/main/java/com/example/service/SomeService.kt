package com.example.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat

class SomeService : Service() {

    companion object {
        fun startForegroundService(from: Context) {
            val intent = Intent(from, SomeService::class.java).apply {
                putExtras(Bundle().apply {
//                putString(PushKeys.CALL_ID, callId)
                })
            }
            from.startForegroundService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createDefaultNotification()
        startForeground(System.currentTimeMillis().toInt(), notification)
        return START_STICKY
    }

    private fun createDefaultNotification(): Notification {
        return NotificationCompat.Builder(this, PushChannelType.DEFAULT.channelId)
            .setContentTitle("Our title")
            .setContentText("Our Description")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}