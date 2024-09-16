package me.ljpb.yosetsukenai.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: NotificationEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: NotificationEntity)

    @Delete
    suspend fun delete(entity: NotificationEntity)

    @Query("select * from notification where repellentScheduleId = :id")
    fun getItemByRepellentScheduleId(id: Long): Flow<List<NotificationEntity>>
}