package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.AppDatabaseConverter
import me.ljpb.yosetsukenai.data.room.RepellentScheduleDao
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate

class RepellentScheduleRepository(
    private val dao: RepellentScheduleDao,
    private val converter: AppDatabaseConverter
) : RepellentScheduleAction {
    override suspend fun insert(entity: RepellentScheduleEntity): Long = dao.insert(entity)

    override suspend fun update(entity: RepellentScheduleEntity) = dao.update(entity)

    override suspend fun delete(entity: RepellentScheduleEntity) = dao.delete(entity)

    override fun getSize(): Long = dao.getSize()

    override fun getEnabledRepellents(currentDate: LocalDate): Flow<List<RepellentScheduleEntity>> =
        dao.getEnabledItems(converter.fromLocalDateToString(currentDate))

    override fun getExpiredRepellents(currentDate: LocalDate): Flow<List<RepellentScheduleEntity>> =
        dao.getExpiredItems(converter.fromLocalDateToString(currentDate))

    override fun getPagedRepellents(limit: Int, offset: Int): Flow<List<RepellentScheduleEntity>> =
        dao.getPagedItems(limit = limit, offset = offset)

}