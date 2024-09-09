package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.InsectEncounterEntity
import java.time.LocalDate

interface InsectEncounterAction {
    suspend fun insert(entity: InsectEncounterEntity): Long

    suspend fun update(entity: InsectEncounterEntity)

    suspend fun delete(entity: InsectEncounterEntity)

    fun getSize(): Long

    fun getInsects(from: LocalDate, to: LocalDate): Flow<List<InsectEncounterEntity>>

    fun getPagedInsects(limit: Int, offset: Int): Flow<List<InsectEncounterEntity>>
}