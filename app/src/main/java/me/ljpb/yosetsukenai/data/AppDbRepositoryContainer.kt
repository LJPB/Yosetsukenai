package me.ljpb.yosetsukenai.data

import android.content.Context
import androidx.work.WorkManager
import me.ljpb.yosetsukenai.data.room.AppDatabase
import me.ljpb.yosetsukenai.data.room.TableConverter

interface DbRepositoryContainer {
    val insectRepository: InsectAction
    val notificationRepository: NotificationAction
    val repellentScheduleRepository: RepellentScheduleAction
}

class AppDbRepositoryContainer(private val context: Context) : DbRepositoryContainer {
    private val db = AppDatabase.getDatabase(context)
    private val typeConverter = TableConverter()
    private val workManager = WorkManager.getInstance(context)

    override val insectRepository: InsectAction by lazy {
        InsectRepository(
            db.insectDao(),
            typeConverter
        )
    }

    override val notificationRepository: NotificationAction by lazy {
        NotificationRepository(db.notificationDao(), workManager)
    }

    override val repellentScheduleRepository: RepellentScheduleAction by lazy {
        RepellentScheduleRepository(
            db.repellentScheduleDao(),
            typeConverter
        )
    }
}