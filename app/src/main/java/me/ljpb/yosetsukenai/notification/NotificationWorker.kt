package me.ljpb.yosetsukenai.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import me.ljpb.yosetsukenai.MyApplication
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.notification.AppNotificationManager.notify

class NotificationWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        val notificationEntityId = inputData.getLong(AppNotificationManager.Keys.ENTITY_ID, -1)
        val app = applicationContext as MyApplication
        val container = app.dbRepositoryContainer
        val notificationDao = container.notificationRepository
        val repellentDao = container.repellentScheduleRepository

        try {
            val notificationEntity =
                notificationDao.getNotificationById(notificationEntityId).first()
                    ?: throw Exception()

            val repellentId = notificationEntity.repellentScheduleId

            val repellent = repellentDao.getRepellentById(repellentId).first() ?: throw Exception()

            val formatedDate = applicationContext.getString(
                R.string.formated_date,
                repellent.finishDate.year,
                repellent.finishDate.monthValue,
                repellent.finishDate.dayOfMonth,
            )

            notify(
                applicationContext,
                notificationEntity,
                applicationContext.getString(R.string.notification_title),
                applicationContext.getString(
                    R.string.notification_text,
                    repellent.name,
                    formatedDate
                )
            )
        } catch (_: Exception) {
            Result.failure()
        }

        return Result.success()
    }
}