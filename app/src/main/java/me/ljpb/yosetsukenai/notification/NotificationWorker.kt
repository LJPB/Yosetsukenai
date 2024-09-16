package me.ljpb.yosetsukenai.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.ljpb.yosetsukenai.data.room.NotificationEntity

class NotificationWorker(
    private val notification: NotificationEntity,
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        notify(applicationContext, notification)
        return Result.success()
    }
}