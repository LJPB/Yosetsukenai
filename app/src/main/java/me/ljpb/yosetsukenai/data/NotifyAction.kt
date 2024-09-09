package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

interface NotifyAction {
    suspend fun insert(entity: NotifyEntity): Long

    suspend fun update(entity: NotifyEntity)

    suspend fun delete(entity: NotifyEntity)
    
    fun getNotifiesOf(parent: RepellentScheduleEntity): Flow<List<NotifyEntity>>
}