package me.ljpb.yosetsukenai.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class NotifyDaoTest {
    private lateinit var notifyDao: NotifyDao
    private lateinit var repellentDao: RepellentScheduleDao
    private lateinit var database: AppDatabase
    private lateinit var converter: TableConverter
    
    private val parent = RepellentScheduleEntity(
        id = 1,
        name = "a",
        validityPeriod = SimplePeriod.ofDays(3),
        startDate = LocalDate.of(2024, 9, 5),
        finishDate = LocalDate.of(2024, 9, 8),
        places = listOf("aaa", "bbb"),
        ignore = false,
        zoneId = ZoneId.of("UTC")
    )

    private val child1 = NotifyEntity(
        id = 1,
        repellentScheduleId = parent.id,
        jobId = UUID.randomUUID(),
        notifyId = 1,
        triggerTimeSeconds = 1L,
        schedule = SimplePeriod.ofDays(1),
        time = SimpleTime.of(1, 1)
    )

    private val child2 = NotifyEntity(
        id = 2,
        repellentScheduleId = parent.id,
        jobId = UUID.randomUUID(),
        notifyId = 2,
        triggerTimeSeconds = 2L,
        schedule = SimplePeriod.ofDays(2),
        time = SimpleTime.of(2, 2)
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repellentDao = database.repellentScheduleDao()
        notifyDao = database.notifyDao()
        converter = TableConverter()
        runBlocking {
            repellentDao.insert(parent)
            notifyDao.insert(child1)
            notifyDao.insert(child2)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }
    
    @Test
    fun parentDeleted() = runBlocking {
        repellentDao.delete(parent)
        val list = notifyDao.getItemByRepellentScheduleId(parent.id).first()
        assert(list.isEmpty())
    }
}