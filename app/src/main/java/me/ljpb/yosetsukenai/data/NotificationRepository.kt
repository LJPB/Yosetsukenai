package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.NotificationDao
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.notification.NotificationManager

class NotificationRepository(private val dao: NotificationDao, private val notificationManager: NotificationManager) : NotificationAction {
    override suspend fun insert(entity: NotificationEntity): Long {
        notificationManager.setNotification(entity)
        return dao.insert(entity)
    }

    override suspend fun update(entity: NotificationEntity) {
        notificationManager.updateNotifySchedule(entity)
        dao.update(entity)
    }

    override suspend fun delete(entity: NotificationEntity) {
        notificationManager.cancelNotify(entity)
        dao.delete(entity)
    }

    override fun getNotificationsOf(parent: RepellentScheduleEntity): Flow<List<NotificationEntity>> =
        dao.getItemByRepellentScheduleId(parent.id)

}