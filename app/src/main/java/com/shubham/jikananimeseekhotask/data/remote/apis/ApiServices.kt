package com.shubham.jikananimeseekhotask.data.remote.apis

import com.shubham.jikananimeseekhotask.data.remote.dto.AnimeDetailResponseDto
import com.shubham.jikananimeseekhotask.data.remote.dto.TopAnimeResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    /*@GET(ApiConstants.TOP_ANIME)
    suspend fun getTopAnime(@Query("page") page: Int): Response<ApiBaseResponse<TopAnimeResponseDto>>*/

    @GET(ApiConstants.TOP_ANIME)
    suspend fun getTopAnime(
        @Query("page") page: Int
    ): TopAnimeResponseDto

    @GET(ApiConstants.ANIME_DETAIL)
    suspend fun getAnimeDetail(
        @Path("id") animeId: Int
    ):  Response<AnimeDetailResponseDto>
    

}