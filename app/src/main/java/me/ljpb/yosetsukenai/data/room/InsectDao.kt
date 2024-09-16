package me.ljpb.yosetsukenai.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InsectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: InsectEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: InsectEntity)

    @Delete
    suspend fun delete(entity: InsectEntity)

    @Query("select count(*) from insect")
    fun getSize(): Long

    // 指定した期間内に発見した虫のリストの取得 
    @Query("select * from insect where date between :from and :to order by date asc")
    fun getItems(from: String, to: String): Flow<List<InsectEntity>>

    // 発見したすべての虫を日付順でページングして取得 
    @Query("select * from insect order by date asc limit :limit offset :offset")
    fun getPagedItems(limit: Int, offset: Int): Flow<List<InsectEntity>>
}