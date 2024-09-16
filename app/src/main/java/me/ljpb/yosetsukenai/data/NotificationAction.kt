package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.NotificationEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

interface NotificationAction {
    suspend fun insert(entity: NotificationEntity): Long

    suspend fun update(entity: NotificationEntity)

    suspend fun delete(entity: NotificationEntity)
    
    fun getNotificationsOf(parent: RepellentScheduleEntity): Flow<List<NotificationEntity>>
}