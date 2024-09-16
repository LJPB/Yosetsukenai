package me.ljpb.yosetsukenai.notification

import android.content.Context
import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.PeriodUnit
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

object AppNotificationManager {
    /**
     * @param date 通知を送る日付
     * @param time 通知を送る時間
     * @param before dateからbeforeの期間だけ早く送る。
     * たとえば，date = 2024/10/01, before = 1Day だった場合， date - before = 2024/09/30 に通知を送る
     * もちろん，before = 0Day だった場合はdate日に送る
     * @param parentId NotificationEntityが外部キーとして参照するRepellentScheduleEntityのid
     * @param title 通知に表示するタイトル
     * @param text 通知に表示する本文
     */
    fun createNotificationEntity(
        date: LocalDate,
        time: SimpleTime,
        before: SimplePeriod,
        parentId: Long,
        zoneId: ZoneId,
        title: String,
        text: String
    ): NotificationEntity {
        val uuid = UUID.randomUUID()

        val localDateTime = LocalDateTime.of(
            date.minus(before),
            LocalTime.of(time.hour, time.minutes)
        )
        val zonedDateTime = ZonedDateTime.of(localDateTime, zoneId)
        val triggerTimeEpochSeconds = zonedDateTime.toEpochSecond()

        return NotificationEntity(
            repellentScheduleId = parentId,
            jobId = uuid,
            notificationId = uuid.hashCode(),
            notificationTitle = title,
            notificationText = text,
            triggerTimeEpochSeconds = triggerTimeEpochSeconds,
            schedule = PeriodAndTime(before, time)
        )
    }

    fun setNotification(notification: NotificationEntity) {
        
    }

    fun updateNotifySchedule(
        updatedNotificationEntity: NotificationEntity,
        title: String,
        text: String
    ) {

    }

    fun cancelNotify(notification: NotificationEntity) {

    }

    fun notify(context: Context, notification: NotificationEntity) {
    }

    private fun LocalDate.minus(period: SimplePeriod): LocalDate {
        return when (period.periodUnit) {
            PeriodUnit.Day -> this.minusDays(period.number.toLong())
            PeriodUnit.Week -> this.minusWeeks(period.number.toLong())
            PeriodUnit.Month -> this.minusMonths(period.number.toLong())
            PeriodUnit.Year -> this.minusYears(period.number.toLong())
        }
    }
}