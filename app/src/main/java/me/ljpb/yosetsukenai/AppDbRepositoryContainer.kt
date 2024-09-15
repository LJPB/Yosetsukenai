package me.ljpb.yosetsukenai

import android.content.Context
import me.ljpb.yosetsukenai.data.InsectEncounterAction
import me.ljpb.yosetsukenai.data.InsectEncounterRepository
import me.ljpb.yosetsukenai.data.NotifyAction
import me.ljpb.yosetsukenai.data.NotifyRepository
import me.ljpb.yosetsukenai.data.RepellentScheduleAction
import me.ljpb.yosetsukenai.data.RepellentScheduleRepository
import me.ljpb.yosetsukenai.data.room.AppDatabase
import me.ljpb.yosetsukenai.data.room.TableConverter

interface DbRepositoryContainer {
    val insectEncounterRepository: InsectEncounterAction
    val notifyRepository: NotifyAction
    val repellentScheduleRepository: RepellentScheduleAction
}

class AppDbRepositoryContainer(private val context: Context) : DbRepositoryContainer {
    private val db = AppDatabase.getDatabase(context)
    private val typeConverter = TableConverter()

    override val insectEncounterRepository: InsectEncounterAction by lazy {
        InsectEncounterRepository(
            db.insectEncounterDao(),
            typeConverter
        )
    }

    override val notifyRepository: NotifyAction by lazy {
        NotifyRepository(db.notifyDao())
    }

    override val repellentScheduleRepository: RepellentScheduleAction by lazy {
        RepellentScheduleRepository(
            db.repellentScheduleDao(),
            typeConverter
        )
    }
}