package me.ljpb.yosetsukenai.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.ljpb.yosetsukenai.data.SimplePeriod
import java.time.LocalDate

@Entity(tableName = "repellent_schedule")
data class RepellentScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val validityPeriod: SimplePeriod,
    val startDate: LocalDate,
    val finishDate: LocalDate,
    val places: List<String>,
    val ignore: Boolean
)
