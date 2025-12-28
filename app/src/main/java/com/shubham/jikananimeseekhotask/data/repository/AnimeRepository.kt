package com.shubham.jikananimeseekhotask.data.repository

import com.shubham.jikananimeseekhotask.data.local.AnimeDao
import com.shubham.jikananimeseekhotask.data.local.AnimeEntity
import com.shubham.jikananimeseekhotask.data.local.toEntity
import com.shubham.jikananimeseekhotask.data.remote.NetworkHelper
import com.shubham.jikananimeseekhotask.data.remote.apis.ApiServices
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeRepository @Inject constructor(
   private val apiServices: ApiServices,
    private val animeDao: AnimeDao   ,
   private val networkHelper: NetworkHelper

) {

    suspend fun getAnimeFromDb(): Flow<List<AnimeEntity>> {
        return animeDao.getAllAnime()
    }

    suspend fun fetchAnime(page: Int): Boolean {
        return try {
            val response = apiServices.getTopAnime(page)

            val entities = response.data.map { it.toEntity() }

            animeDao.insertAll(entities)
            response.pagination.hasNextPage
        } catch (e: Exception) {
            false
        }
    }


    
    suspend fun getAnimeByIdFromDb(animeId: Int): AnimeEntity? {
        return animeDao.getAnimeById(animeId)
    }



    suspend fun fetchAnimeDetail(animeId: Int): AnimeEntity {

        return if (networkHelper.isOnline()) {

            // 🔹 ONLINE
            val response = apiServices.getAnimeDetail(animeId)

            val dto = response.body()?.data
                ?: throw Exception("Anime detail not found")

            val entity = dto.toEntity()

            animeDao.insert(entity)   // cache fresh data
            entity

        } else {

            // 🔹 OFFLINE
            animeDao.getAnimeById(animeId)
                ?: throw Exception("No offline data available")
        }
    }

}




