package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.RepellentScheduleEntity
import java.time.LocalDate

interface RepellentScheduleAction {
    suspend fun insert(entity: RepellentScheduleEntity): Long

    suspend fun update(entity: RepellentScheduleEntity)

    suspend fun delete(entity: RepellentScheduleEntity)

    fun getSize(): Long
    
    fun getRepellentById(id: Long): Flow<RepellentScheduleEntity?>

    fun getEnabledRepellents(currentDate: LocalDate): Flow<List<RepellentScheduleEntity>>

    fun getExpiredRepellents(currentDate: LocalDate): Flow<List<RepellentScheduleEntity>>

    fun getPagedRepellents(limit: Int, offset: Int): Flow<List<RepellentScheduleEntity>>
}