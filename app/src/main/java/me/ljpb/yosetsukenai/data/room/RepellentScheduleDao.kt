package me.ljpb.yosetsukenai.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RepellentScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: RepellentScheduleEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: RepellentScheduleEntity)

    @Delete
    suspend fun delete(entity: RepellentScheduleEntity)

    @Query("select count(*) from repellent_schedule")
    fun getSize(): Long

    // 現在有効な虫除けの一覧を開始日と終了日の昇順で取得
    @Query("select * from repellent_schedule where id = :id")
    fun getItemById(id: Long): Flow<RepellentScheduleEntity?>

    // 現在有効な虫除けの一覧を開始日と終了日の昇順で取得
    @Query("select * from repellent_schedule where :currentDate between startDate and finishDate order by finishDate asc")
    fun getEnabledItems(currentDate: String): Flow<List<RepellentScheduleEntity>>

    // 対応が必要な虫除けの一覧を取得
    @Query("select * from repellent_schedule where :currentDate > finishDate and `ignore` = false order by finishDate asc")
    fun getExpiredItems(currentDate: String): Flow<List<RepellentScheduleEntity>>

    @Query("select * from repellent_schedule order by startDate asc, finishDate asc limit :limit offset :offset")
    fun getPagedItems(limit: Int, offset: Int): Flow<List<RepellentScheduleEntity>>

    // 指定した期間内に登録したすべての虫除けの一覧を取得
    @Query("select * from repellent_schedule where startDate between :from and :to order by startDate asc, finishDate asc")
    fun getItemsByStartDate(from: String, to: String): Flow<List<RepellentScheduleEntity>>
  
    // 指定した期間内に終了するすべての虫除けの一覧を取得
    @Query("select * from repellent_schedule where finishDate between :from and :to order by finishDate asc, finishDate asc")
    fun getItemsByFinishDate(from: String, to: String): Flow<List<RepellentScheduleEntity>>
}
