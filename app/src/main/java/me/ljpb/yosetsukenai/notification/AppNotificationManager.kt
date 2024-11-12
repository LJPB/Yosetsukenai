package me.ljpb.yosetsukenai.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import me.ljpb.yosetsukenai.MainActivity
import me.ljpb.yosetsukenai.R
import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

object AppNotificationManager {
    object Keys {
        const val ENTITY_ID = "entityId"
    }

    /**
     * @param date 通知を送る日付
     * @param time 通知を送る時間
     * @param before dateからbeforeの期間だけ早く送る。
     * たとえば，date = 2024/10/01, before = 1Day だった場合， date - before = 2024/09/30 に通知を送る
     * もちろん，before = 0Day だった場合はdate日に送る
     * @param parentId NotificationEntityが外部キーとして参照するRepellentScheduleEntityのid
     */
    fun createNotificationEntity(
        date: LocalDate,
        time: SimpleTime,
        before: SimplePeriod,
        parentId: Long,
        zoneId: ZoneId,
    ): NotificationEntity {
        val uuid = UUID.randomUUID()

        val localDateTime = LocalDateTime.of(
            date.minus(before), // 日付
            LocalTime.of(time.hour, time.minutes) // 時間
        )
        val zonedDateTime = ZonedDateTime.of(localDateTime, zoneId)
        val triggerTimeEpochSeconds = zonedDateTime.toEpochSecond()

        return NotificationEntity(
            repellentScheduleId = parentId,
            uuid = uuid,
            notificationId = uuid.hashCode(),
            triggerTimeEpochSeconds = triggerTimeEpochSeconds,
            schedule = PeriodAndTime(before, time)
        )
    }

    /**
     * 通知時間を更新したNotificationEntityを取得する
     * @param original 更新対象となるNotificationEntity
     * @param updatedDate 更新後に通知を送る日付
     * @param updatedTime 更新後の通知を送る時間
     * @param updatedBefore dateからbeforeの期間だけ早く送る。
     * @param updatedZoneId 更新後のZoneId
     */
    fun updateNotificationEntity(
        original: NotificationEntity,
        updatedDate: LocalDate,
        updatedTime: SimpleTime,
        updatedBefore: SimplePeriod,
        updatedZoneId: ZoneId,
    ): NotificationEntity {

        val localDateTime = LocalDateTime.of(
            updatedDate.minus(updatedBefore), // 日付
            LocalTime.of(updatedTime.hour, updatedTime.minutes) // 時間
        )
        val zonedDateTime = ZonedDateTime.of(localDateTime, updatedZoneId)
        val triggerTimeEpochSeconds = zonedDateTime.toEpochSecond()

        return original.copy(
            triggerTimeEpochSeconds = triggerTimeEpochSeconds,
            schedule = PeriodAndTime(updatedBefore, updatedTime)
        )
    }

    /**
     * 通知をスケジュールする
     * notification.jobIdに対応するWorkRequestがすでに登録済みの場合は更新する
     */
    fun setNotification(notification: NotificationEntity, workManager: WorkManager) {
        val triggerTimeSeconds = notification.triggerTimeEpochSeconds
        val currentTimeSeconds = System.currentTimeMillis() / 1000

        // workManagerにenqueue後，doWork()が実行されるまでの遅延時間。だから通知予定の時刻と現在時刻の差分を渡す。
        val delaySeconds = triggerTimeSeconds - currentTimeSeconds

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
            .addTag(notification.repellentScheduleId.toString())
            .setInputData(notification.toData())
            .build()

        workManager.enqueueUniqueWork(
            notification.uuid.toString(),
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun updateNotification(
        updatedNotificationEntity: NotificationEntity,
        workManager: WorkManager
    ) {
        setNotification(updatedNotificationEntity, workManager)
    }

    fun cancelNotification(notification: NotificationEntity, workManager: WorkManager) {
        workManager.cancelUniqueWork(notification.uuid.toString())
    }

    fun cancelAllNotifiesOf(repellent: RepellentScheduleEntity, workManager: WorkManager) {
        workManager.cancelAllWorkByTag(repellent.id.toString())
    }

    fun notify(
        context: Context,
        notification: NotificationEntity,
        title: String,
        text: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, notification.notificationId, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = context.getString(R.string.notification_channel_id)
        val builder = NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(notification.notificationId, builder.build())
        }
    }

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            context.getString(R.string.notification_channel_id),
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun LocalDate.minus(period: SimplePeriod): LocalDate {
        return when (period.periodUnit) {
            PeriodUnit.Day -> this.minusDays(period.number.toLong())
            PeriodUnit.Week -> this.minusWeeks(period.number.toLong())
            PeriodUnit.Month -> this.minusMonths(period.number.toLong())
            PeriodUnit.Year -> this.minusYears(period.number.toLong())
        }
    }

    private fun NotificationEntity.toData(): Data {
        val builder = Data.Builder()
        builder.putLong(Keys.ENTITY_ID, this.id)
        return builder.build()
    }
}
