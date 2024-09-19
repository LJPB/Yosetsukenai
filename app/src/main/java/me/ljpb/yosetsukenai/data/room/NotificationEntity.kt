package me.ljpb.yosetsukenai.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import me.ljpb.yosetsukenai.data.PeriodAndTime
import java.util.UUID

@Entity(
    tableName = "notification",
    foreignKeys = [ForeignKey(
        entity = RepellentScheduleEntity::class,
        parentColumns = ["id"],
        childColumns = ["repellentScheduleId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class NotificationEntity(
    @PrimaryKey
    val id: Long = 0L,
    val repellentScheduleId: Long,
    val uuid: UUID,
    val notificationId: Int,
    val triggerTimeEpochSeconds: Long,
    val schedule: PeriodAndTime
)