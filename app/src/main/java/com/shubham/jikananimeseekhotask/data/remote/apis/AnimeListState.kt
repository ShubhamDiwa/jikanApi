package com.shubham.jikananimeseekhotask.data.remote.apis

import com.shubham.jikananimeseekhotask.data.local.AnimeEntity

data class AnimeListState(
    val animeList: List<AnimeEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasNextPage: Boolean = true ,
)


