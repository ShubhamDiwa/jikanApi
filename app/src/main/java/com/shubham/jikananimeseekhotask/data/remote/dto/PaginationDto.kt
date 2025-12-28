package com.shubham.jikananimeseekhotask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PaginationDto (
    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("has_next_page")
    val hasNextPage: Boolean
)