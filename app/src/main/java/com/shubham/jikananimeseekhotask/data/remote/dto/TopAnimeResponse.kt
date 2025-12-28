package com.shubham.jikananimeseekhotask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TopAnimeResponseDto(
    @SerializedName("data")
    val data: List<AnimeDto>,
    @SerializedName("pagination")
    val pagination: PaginationDto
)