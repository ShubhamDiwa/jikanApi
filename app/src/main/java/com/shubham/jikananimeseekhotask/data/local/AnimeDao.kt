package com.shubham.jikananimeseekhotask.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<AnimeEntity>)

    @Query("SELECT * FROM anime ORDER BY rating DESC")
     fun getAllAnime(): Flow<List<AnimeEntity>>

    @Query("DELETE FROM anime")
    suspend fun clearAll()


    @Query("SELECT * FROM anime WHERE id = :id LIMIT 1")
    suspend fun getAnimeById(id: Int): AnimeEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anime: AnimeEntity)
}