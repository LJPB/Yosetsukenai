package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.NotificationDao
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

class NotificationRepository(private val dao: NotificationDao) : NotificationAction {
    override suspend fun insert(entity: NotificationEntity): Long = dao.insert(entity)

    override suspend fun update(entity: NotificationEntity) = dao.update(entity)

    override suspend fun delete(entity: NotificationEntity) = dao.delete(entity)

    override fun getNotificationsOf(parent: RepellentScheduleEntity): Flow<List<NotificationEntity>> =
        dao.getItemByRepellentScheduleId(parent.id)

}