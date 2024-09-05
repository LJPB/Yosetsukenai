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

class InsectEncounterDaoTest {
    private lateinit var dao: InsectEncounterDao
    private lateinit var database: AppDatabase
    private lateinit var converter: TableConverter

    private val item1 = InsectEncounterEntity(
        id = 1,
        name = "a",
        date = LocalDate.of(2024, 9, 5),
        size = "a",
        condition = "a",
        place = "a"
    )
    private val item2 = InsectEncounterEntity(
        id = 2,
        name = "b",
        date = LocalDate.of(2024, 9, 7),
        size = "b",
        condition = "b",
        place = "b"
    )
    private val item3 = InsectEncounterEntity(
        id = 3,
        name = "c",
        date = LocalDate.of(2024, 8, 5),
        size = "c",
        condition = "c",
        place = "c"
    )
    private val item4 = InsectEncounterEntity(
        id = 4,
        name = "d",
        date = LocalDate.of(2024, 8, 6),
        size = "d",
        condition = "d",
        place = "d"
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.insectEncounterDao()
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
        val expect = emptyList<InsectEncounterEntity>()
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