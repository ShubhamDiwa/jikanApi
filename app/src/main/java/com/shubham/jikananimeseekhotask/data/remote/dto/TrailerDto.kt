package com.shubham.jikananimeseekhotask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrailerDto(
    @SerializedName("embed_url")
    val embedUrl: String?
)
