package com.shubham.jikananimeseekhotask.presentation.models

data class AnimeUiModel(
    val id: Int,
    val title: String,
    val episodes: Int?,
    val rating: Double?,
    val synopsis: String?,
    val genres: List<String>,
    val imageUrl: String?,
    val trailerUrl: String?
)