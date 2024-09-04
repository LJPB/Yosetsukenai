package me.ljpb.yosetsukenai.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.ljpb.yosetsukenai.data.SimplePeriod
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class RepellentScheduleDaoTest {
    private lateinit var dao: RepellentScheduleDao
    private lateinit var database: AppDatabase
    private lateinit var converter: TableConverter

    private val validityItem1 = RepellentScheduleEntity(
        id = 1,
        name = "a",
        validityPeriod = SimplePeriod.ofDays(3),
        startDate = LocalDate.of(2024, 9, 5),
        finishDate = LocalDate.of(2024, 9, 8),
        places = listOf("aaa", "bbb"),
        ignore = false
    )

    private val validityItem2 = RepellentScheduleEntity(
        id = 2,
        name = "b",
        validityPeriod = SimplePeriod.ofWeeks(1),
        startDate = LocalDate.of(2024, 9, 4),
        finishDate = LocalDate.of(2024, 9, 11),
        places = listOf("ccc"),
        ignore = false
    )

    private val validityItemsList = listOf(validityItem1, validityItem2)

    private val expiredItemButIgnore = RepellentScheduleEntity(
        id = 3,
        name = "c",
        validityPeriod = SimplePeriod.ofWeeks(1),
        startDate = LocalDate.of(2024, 8, 5),
        finishDate = LocalDate.of(2024, 8, 12),
        places = listOf("aaa", "bbb"),
        ignore = true
    )

    private val expiredItem = RepellentScheduleEntity(
        id = 4,
        name = "d",
        validityPeriod = SimplePeriod.ofDays(2),
        startDate = LocalDate.of(2024, 9, 1),
        finishDate = LocalDate.of(2024, 9, 3),
        places = listOf("aaa", "bbb"),
        ignore = false
    )
    
    private val itemsOrderByDate = listOf(
        expiredItemButIgnore,
        expiredItem,
        validityItem2,
        validityItem1
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.repellentScheduleDao()
        converter = TableConverter()
        runBlocking {
            dao.insert(validityItem1)
            dao.insert(validityItem2)
            dao.insert(expiredItemButIgnore)
            dao.insert(expiredItem)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    // 有効期間内のアイテムが終了日の昇順で取得できるかのテスト
    @Test
    @Throws(Exception::class)
    fun getEnabledItemsTestInValidityPeriod() = runBlocking {
        val items = dao.getEnabledItems(converter.fromLocalDateToString(LocalDate.of(2024, 9, 5))).first()
        assertEquals(items, validityItemsList)
    }

    @Test
    @Throws(Exception::class)
    fun getExpiredItemsTest() = runBlocking {
        val items = dao.getExpiredItems(converter.fromLocalDateToString(LocalDate.of(2024, 9, 5))).first()
        assertEquals(items, listOf(expiredItem))
    }
    
    // 開始日の昇順，終了日の昇順でアイテムが並んでいるかのテスト
    @Test
    @Throws(Exception::class)
    fun getPagedItemsTest() = runBlocking {
        val items = dao.getPagedItems(itemsOrderByDate.size, 0).first()
        assertEquals(items, itemsOrderByDate)
    }

}