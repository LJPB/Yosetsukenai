package me.ljpb.yosetsukenai.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.ZoneId

@Entity(tableName = "insect_encounter")
data class InsectEncounterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val date: LocalDate,
    val size: String,
    val condition: String,
    val place: String,
    val zoneId: ZoneId
)