package com.example.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

interface INotificationBuilder {
    fun showNotification()
}

class NotificationFactory {
    fun createNotification(context: Context, type: PushChannelType, title: String, description: String): INotificationBuilder {
        return when (type) {
            PushChannelType.DEFAULT -> DefaultNotification(context, title, description)
            PushChannelType.MESSAGES -> MessagesNotification(context, title, description)
        }
    }
}

private class DefaultNotification(private val context: Context, private val title: String, private val description: String) : INotificationBuilder {
    override fun showNotification() {
        showNotification(context, MainActivity::class.java, title, description, PushChannelType.DEFAULT.channelId)
    }
}

private class MessagesNotification(private val context: Context, private val title: String, private val description: String) : INotificationBuilder {
    override fun showNotification() {
        showNotification(context, MainActivity::class.java, title, description, PushChannelType.MESSAGES.channelId)
    }
}

private fun showNotification(context: Context, pendingActivity: Class<*>, title: String, description: String, channelID: String) {
    val intent = Intent(context, pendingActivity)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val builder = NotificationCompat.Builder(context, channelID)
        .setContentTitle(title)
        .setContentText(description)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
        .setContentIntent(pendingIntent)
        .build()
    notificationManager.notify(System.currentTimeMillis().toInt(), builder)
}
