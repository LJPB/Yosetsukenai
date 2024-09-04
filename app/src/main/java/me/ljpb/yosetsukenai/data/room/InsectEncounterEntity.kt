package me.ljpb.yosetsukenai.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "insect_encounter")
data class InsectEncounterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val date: LocalDate,
    val size: String,
    val condition: String,
    val place: String,
)