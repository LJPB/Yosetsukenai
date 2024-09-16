package me.ljpb.yosetsukenai.notification

import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import java.time.LocalDate

interface NotificationManager {
    /**
     * 日付 dateから期間 before前に通知するようなEntityを取得する
     * たとえば，date = 2024/10/01, before = 1日前 なら 通知する日にちは 2024/10/01 - 1日 = 2024/09/30
     * もちろん，before = 0 なら通知する日にちはdate
     */
    fun createNotificationEntity(
        date: LocalDate,
        before: PeriodAndTime,
        parentId: Long
    ): NotificationEntity

    fun setNotification(notification: NotificationEntity)

    fun updateNotifySchedule(updatedNotificationEntity: NotificationEntity)

    fun cancelNotify(notification: NotificationEntity)
}