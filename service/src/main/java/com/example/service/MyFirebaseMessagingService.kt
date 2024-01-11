package com.example.service

import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.helperlibrary.toLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        "onNewToken -> $token".toLog(TAG)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        "onDeletedMessages".toLog(TAG)
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
        ("onMessageSent -> $p0").toLog(TAG)
    }

    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
        "onSendError -> $p0 , $p1".toLog(TAG)
    }

    override fun handleIntent(intent: Intent?) {
        "handleIntent ${intent?.extras}".toLog(TAG)
        val title: String = intent?.extras?.getString(INTENT_TITLE) ?: ""
        val message: String = intent?.extras?.getString(INTENT_MESSAGE) ?: ""
        showNotificationWithWorker(title, message)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        "onMessageReceived -> remoteMessage.notification: ${remoteMessage.notification}".toLog(TAG)

        val title: String = remoteMessage.notification?.title ?: ""
        val message: String = remoteMessage.notification?.body ?: ""
        showNotificationWithWorker(title, message)
    }

    private fun showNotificationWithWorker(title: String, message: String) {
        val inputData: Data = Data.Builder().putString(PUSH_TITLE, title).putString(PUSH_MESSAGE, message).build()
        val notificationRequest = OneTimeWorkRequest.Builder(PushNotificationWorker::class.java).setInputData(inputData).build()
        WorkManager.getInstance(ServiceApp.applicationContext()).enqueue(notificationRequest)
    }

    companion object {
        private const val TAG = "push"
        const val PUSH_TITLE = "push_title"
        const val PUSH_MESSAGE = "push_message"

        private const val INTENT_TITLE = "gcm.notification.title"
        private const val INTENT_MESSAGE = "gcm.notification.body"
    }
}