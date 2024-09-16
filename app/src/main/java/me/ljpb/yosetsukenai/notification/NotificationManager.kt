package me.ljpb.yosetsukenai.notification

import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import java.time.LocalDate

interface NotificationManager {
    fun createNotificationEntity(startDate: LocalDate, schedule: PeriodAndTime, parentId: Long): NotificationEntity

    fun setNotification(notification: NotificationEntity)

    fun updateNotifySchedule(updatedNotificationEntity: NotificationEntity)

    fun cancelNotify(notification: NotificationEntity)
}