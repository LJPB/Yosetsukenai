package me.ljpb.yosetsukenai

import android.content.Context
import me.ljpb.yosetsukenai.data.InsectAction
import me.ljpb.yosetsukenai.data.InsectRepository
import me.ljpb.yosetsukenai.data.NotificationAction
import me.ljpb.yosetsukenai.data.NotificationRepository
import me.ljpb.yosetsukenai.data.RepellentScheduleAction
import me.ljpb.yosetsukenai.data.RepellentScheduleRepository
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

    override val insectRepository: InsectAction by lazy {
        InsectRepository(
            db.insectDao(),
            typeConverter
        )
    }

    override val notificationRepository: NotificationAction by lazy {
        NotificationRepository(db.notificationDao())
    }

    override val repellentScheduleRepository: RepellentScheduleAction by lazy {
        RepellentScheduleRepository(
            db.repellentScheduleDao(),
            typeConverter
        )
    }
}