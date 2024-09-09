package me.ljpb.yosetsukenai.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotifyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: NotifyEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: NotifyEntity)

    @Delete
    suspend fun delete(entity: NotifyEntity)

    @Query("select * from notify where repellentScheduleId = :id")
    fun getItemByRepellentScheduleId(id: Long): Flow<List<NotifyEntity>>
}