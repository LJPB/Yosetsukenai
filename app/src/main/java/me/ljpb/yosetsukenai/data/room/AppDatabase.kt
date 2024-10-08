package me.ljpb.yosetsukenai.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RepellentScheduleEntity::class, InsectEntity::class, NotificationEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TableConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repellentScheduleDao(): RepellentScheduleDao

    abstract fun insectDao(): InsectDao
    
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}