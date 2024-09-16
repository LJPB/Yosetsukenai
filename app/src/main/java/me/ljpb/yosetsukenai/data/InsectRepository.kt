package me.ljpb.yosetsukenai.data

import kotlinx.coroutines.flow.Flow
import me.ljpb.yosetsukenai.data.room.AppDatabaseConverter
import me.ljpb.yosetsukenai.data.room.InsectDao
import me.ljpb.yosetsukenai.data.room.InsectEntity
import java.time.LocalDate

class InsectRepository(
    private val dao: InsectDao,
    private val converter: AppDatabaseConverter
) : InsectAction {
    override suspend fun insert(entity: InsectEntity): Long = dao.insert(entity)

    override suspend fun update(entity: InsectEntity) = dao.update(entity)

    override suspend fun delete(entity: InsectEntity) = dao.delete(entity)

    override fun getSize(): Long = dao.getSize()

    override fun getInsects(from: LocalDate, to: LocalDate): Flow<List<InsectEntity>> =
        dao.getItems(
            from = converter.fromLocalDateToString(from),
            to = converter.fromLocalDateToString(to)
        )


    override fun getPagedInsects(limit: Int, offset: Int): Flow<List<InsectEntity>> = dao.getPagedItems(limit = limit, offset = offset)
}