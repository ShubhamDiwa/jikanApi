package com.shubham.jikananimeseekhotask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ImageSizeDto(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("large_image_url")
    val largeImageUrl: String?
)
