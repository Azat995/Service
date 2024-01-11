package com.example.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.helperlibrary.toLog

class PushNotificationWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val title: String = inputData.getString(MyFirebaseMessagingService.PUSH_TITLE) ?: return Result.failure()
            val message: String = inputData.getString(MyFirebaseMessagingService.PUSH_MESSAGE) ?: return Result.failure()
            showNotification(PushChannelType.DEFAULT, title, message)
            Result.success()
        } catch (error: Throwable) {
            "PushNotificationWorker failure: $error ".toLog("push")
            Result.failure()
        }
    }

    private fun showNotification(type: PushChannelType, title: String, description: String) {
        val notificationFactory = NotificationFactory()
        val notification: INotificationBuilder = notificationFactory.createNotification(context, type, title, description)
        notification.showNotification()
    }

}