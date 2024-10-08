package me.ljpb.yosetsukenai.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId

class InsectDaoTest {
    private lateinit var dao: InsectDao
    private lateinit var database: AppDatabase
    private lateinit var converter: TableConverter

    private val item1 = InsectEntity(
        id = 1,
        name = "a",
        date = LocalDate.of(2024, 9, 5),
        size = "a",
        condition = "a",
        place = "a",
        zoneId = ZoneId.of("UTC")
    )
    private val item2 = InsectEntity(
        id = 2,
        name = "b",
        date = LocalDate.of(2024, 9, 7),
        size = "b",
        condition = "b",
        place = "b",
        zoneId = ZoneId.of("UTC")
    )
    private val item3 = InsectEntity(
        id = 3,
        name = "c",
        date = LocalDate.of(2024, 8, 5),
        size = "c",
        condition = "c",
        place = "c",
        zoneId = ZoneId.of("UTC")
    )
    private val item4 = InsectEntity(
        id = 4,
        name = "d",
        date = LocalDate.of(2024, 8, 6),
        size = "d",
        condition = "d",
        place = "d",
        zoneId = ZoneId.of("UTC")
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.insectDao()
        converter = TableConverter()
        runBlocking {
            dao.insert(item1)
            dao.insert(item2)
            dao.insert(item3)
            dao.insert(item4)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun getItemsTest() = runBlocking {
        val items = dao.getItems(
            from = converter.fromLocalDateToString(LocalDate.of(2024, 8, 5)),
            to = converter.fromLocalDateToString(LocalDate.of(2024, 9, 6))
        ).first()
        val expect = listOf(item3, item4, item1)
        assertEquals(items, expect)
    }

    @Test
    @Throws(Exception::class)
    fun getItemsTestNotExist() = runBlocking {
        val items = dao.getItems(
            from = converter.fromLocalDateToString(LocalDate.of(2024, 9, 8)),
            to = converter.fromLocalDateToString(LocalDate.of(2025, 1, 1))
        ).first()
        val expect = emptyList<InsectEntity>()
        assertEquals(items, expect)
    }

    @Test
    @Throws(Exception::class)
    fun getPagedItemsTest() = runBlocking {
        val items = dao.getPagedItems(
            limit = 2,
            offset = 2
        ).first()
        val expect = listOf(item1, item2)
        assertEquals(items, expect)
    }

}