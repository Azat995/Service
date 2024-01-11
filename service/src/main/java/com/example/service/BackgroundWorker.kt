package com.example.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class BackgroundWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val COUNT = "count"
    }

    override suspend fun doWork(): Result {
        return try {
            val count: Int = inputData.getInt(COUNT, 0)
            repeat(count) {
                delay(500)
                Log.d("BackgroundWorker123", "doWork: I am in $it iteration")
            }
            Result.success()
        } catch (error: Throwable) {
            Result.failure()
        }
    }

}