package me.ljpb.yosetsukenai.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return Result.success()
    }
}