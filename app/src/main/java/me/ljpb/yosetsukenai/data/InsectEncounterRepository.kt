package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.AppDatabaseConverter
import me.ljpb.yosetsukenai.data.room.InsectEncounterDao
import me.ljpb.yosetsukenai.data.room.InsectEncounterEntity
import java.time.LocalDate

class InsectEncounterRepository(
    private val dao: InsectEncounterDao,
    private val converter: AppDatabaseConverter
) : InsectEncounterAction {
    override suspend fun insert(entity: InsectEncounterEntity): Long = dao.insert(entity)

    override suspend fun update(entity: InsectEncounterEntity) = dao.update(entity)

    override suspend fun delete(entity: InsectEncounterEntity) = dao.delete(entity)

    override fun getSize(): Long = dao.getSize()

    override fun getInsects(from: LocalDate, to: LocalDate): Flow<List<InsectEncounterEntity>> =
        dao.getItems(
            from = converter.fromLocalDateToString(from),
            to = converter.fromLocalDateToString(to)
        )


    override fun getPagedInsects(limit: Int, offset: Int): Flow<List<InsectEncounterEntity>> = dao.getPagedItems(limit = limit, offset = offset)
}