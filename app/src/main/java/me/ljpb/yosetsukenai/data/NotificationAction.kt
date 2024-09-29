package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.NotificationEntity

interface NotificationAction {
    suspend fun insert(entity: NotificationEntity): Long

    suspend fun update(entity: NotificationEntity)

    suspend fun delete(entity: NotificationEntity)
    
    fun getNotificationById(id: Long): Flow<NotificationEntity?>
    
    fun getNotificationsByParentId(parentId: Long): Flow<List<NotificationEntity>>
}