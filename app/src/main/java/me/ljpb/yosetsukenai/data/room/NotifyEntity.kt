package me.ljpb.yosetsukenai.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import me.ljpb.yosetsukenai.data.SimplePeriod
import java.util.UUID

@Entity(
    tableName = "notify",
    foreignKeys = [ForeignKey(
        entity = RepellentScheduleEntity::class,
        parentColumns = ["id"],
        childColumns = ["eventId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class NotifyEntity(
    @PrimaryKey
    val id: Long,
    val eventId: Long,
    val jobId: UUID,
    val notifyId: Int,
    val triggerTime: Long,
    val schedule: SimplePeriod
)