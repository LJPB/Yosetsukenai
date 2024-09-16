package me.ljpb.yosetsukenai.notification

import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.room.NotificationEntity

interface NotificationManager {
    fun createNotificationEntity(periodAndTime: PeriodAndTime, parentId: Long): NotificationEntity
    
    fun setNotification(notification: NotificationEntity)
    
    fun cancelNotify(notification: NotificationEntity)
}