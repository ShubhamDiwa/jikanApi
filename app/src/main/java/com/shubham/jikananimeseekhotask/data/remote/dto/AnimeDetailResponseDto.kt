package com.shubham.jikananimeseekhotask.data.remote.dto

import AnimeDetailDto
import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseDto(
    @SerializedName("data")
    val data: AnimeDetailDto
)
