package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.InsectEntity
import java.time.LocalDate

interface InsectAction {
    suspend fun insert(entity: InsectEntity): Long

    suspend fun update(entity: InsectEntity)

    suspend fun delete(entity: InsectEntity)

    fun getSize(): Long

    fun getInsects(from: LocalDate, to: LocalDate): Flow<List<InsectEntity>>

    fun getPagedInsects(limit: Int, offset: Int): Flow<List<InsectEntity>>

    fun getMaxDate(): Flow<LocalDate?>

    fun getMinDate(): Flow<LocalDate?>
    
    fun countByDate(date: LocalDate): Flow<Int>
}
