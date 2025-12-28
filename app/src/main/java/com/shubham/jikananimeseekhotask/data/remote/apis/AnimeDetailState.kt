package com.shubham.jikananimeseekhotask.data.remote.apis

import com.shubham.jikananimeseekhotask.data.local.AnimeEntity

data class AnimeDetailState(
    val isLoading: Boolean = false,
    val anime: AnimeEntity? = null,
    val error: String? = null   ,
    val isOffline: Boolean = false

)
