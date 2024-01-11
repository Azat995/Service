package com.example.service

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.service.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()
        requestNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            } else {
                // repeat the permission or open app details
            }
        }
    }

    private fun setClickListeners() = with(binding) {
        startServiceButton.setOnClickListener {
            SomeService.startForegroundService(this@MainActivity)
        }
        showNotificationButton.setOnClickListener {
            showNotification(this@MainActivity, MainActivity::class.java, "title", "desc", PushChannelType.DEFAULT.channelId)
        }
        startWorkManagerButton.setOnClickListener {
            startOneTimeBackgroundRequest(10)
        }
    }

    private fun startOneTimeBackgroundRequest(count: Int) {
        val inputData: Data = Data.Builder().putInt(BackgroundWorker.COUNT, count).build()
        val backgroundRequest = OneTimeWorkRequest.Builder(BackgroundWorker::class.java).setInputData(inputData).setInitialDelay(0, TimeUnit.SECONDS).build()
        WorkManager.getInstance(applicationContext).enqueue(backgroundRequest)
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
}
