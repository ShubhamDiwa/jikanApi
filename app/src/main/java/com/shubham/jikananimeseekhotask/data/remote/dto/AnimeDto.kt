package com.shubham.jikananimeseekhotask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeDto(
    @SerializedName("mal_id")
    val malId: Int,

    val title: String,

    @SerializedName("title_english")
    val titleEnglish: String?,

    val episodes: Int?,

    val score: Double?,

    val synopsis: String?,

    val images: ImagesDto,

    val trailer: TrailerDto?,

    val genres: List<GenreDto>
)