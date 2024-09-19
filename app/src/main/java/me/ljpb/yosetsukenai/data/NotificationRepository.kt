package me.ljpb.yosetsukenai.data

import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.NotificationDao
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import me.ljpb.yosetsukenai.notification.AppNotificationManager

class NotificationRepository(private val dao: NotificationDao, private val workManager: WorkManager) : NotificationAction {
    override suspend fun insert(entity: NotificationEntity): Long {
        AppNotificationManager.setNotification(entity, workManager)
        return dao.insert(entity)
    }

    override suspend fun update(entity: NotificationEntity) {
        AppNotificationManager.updateNotification(entity, workManager)
        dao.update(entity)
    }

    override suspend fun delete(entity: NotificationEntity) {
        AppNotificationManager.cancelNotification(entity, workManager)
        dao.delete(entity)
    }

    override fun getNotificationById(id: Long): Flow<NotificationEntity?> = dao.getItemById(id)
    

    override fun getNotificationsOf(parent: RepellentScheduleEntity): Flow<List<NotificationEntity>> =
        dao.getItemByRepellentScheduleId(parent.id)

}