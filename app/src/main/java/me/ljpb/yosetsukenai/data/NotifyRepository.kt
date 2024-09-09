package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.NotifyDao
import me.ljpb.yosetsukenai.data.room.NotifyEntity
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity

class NotifyRepository(private val dao: NotifyDao) : NotifyAction {
    override suspend fun insert(entity: NotifyEntity): Long = dao.insert(entity)

    override suspend fun update(entity: NotifyEntity) = dao.update(entity)

    override suspend fun delete(entity: NotifyEntity) = dao.delete(entity)

    override fun getNotifiesOf(parent: RepellentScheduleEntity): Flow<List<NotifyEntity>> =
        dao.getItemByRepellentScheduleId(parent.id)

}