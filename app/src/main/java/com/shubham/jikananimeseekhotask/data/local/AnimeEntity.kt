package com.shubham.jikananimeseekhotask.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,

    val episodes: Int?,

    val rating: Double?,

    val synopsis: String?,

    val genres: String,

    val imageUrl: String?,

    val trailerUrl: String?
)