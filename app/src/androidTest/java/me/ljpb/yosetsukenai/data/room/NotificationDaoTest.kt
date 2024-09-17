package me.ljpb.yosetsukenai.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.ljpb.yosetsukenai.data.PeriodAndTime
import me.ljpb.yosetsukenai.data.SimplePeriod
import me.ljpb.yosetsukenai.data.SimpleTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class NotificationDaoTest {
    private lateinit var notificationDao: NotificationDao
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

    private val child1 = NotificationEntity(
        id = 1,
        repellentScheduleId = parent.id,
        jobId = UUID.randomUUID(),
        notificationId = 1,
        triggerTimeEpochSeconds = 1L,
        schedule = PeriodAndTime(SimplePeriod.ofDays(1), SimpleTime.of(1, 1)),
    )

    private val child2 = NotificationEntity(
        id = 2,
        repellentScheduleId = parent.id,
        jobId = UUID.randomUUID(),
        notificationId = 2,
        triggerTimeEpochSeconds = 2L,
        schedule = PeriodAndTime(SimplePeriod.ofDays(2), SimpleTime.of(2, 2)),
        )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repellentDao = database.repellentScheduleDao()
        notificationDao = database.notificationDao()
        converter = TableConverter()
        runBlocking {
            repellentDao.insert(parent)
            notificationDao.insert(child1)
            notificationDao.insert(child2)
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
        val list = notificationDao.getItemByRepellentScheduleId(parent.id).first()
        assert(list.isEmpty())
    }
}