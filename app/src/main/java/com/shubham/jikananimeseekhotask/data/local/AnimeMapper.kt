package com.shubham.jikananimeseekhotask.data.local
import AnimeDetailDto
import com.shubham.jikananimeseekhotask.data.remote.dto.AnimeDto

fun AnimeDto.toEntity(): AnimeEntity {
    return AnimeEntity(
        id = malId,
        title = title,
        episodes = episodes,
        rating = score,
        synopsis = synopsis,
        genres = genres.joinToString(",") { it.name },
        imageUrl = images.webp?.imageUrl ?: images.jpg?.imageUrl,
        trailerUrl = trailer?.embedUrl
    )
}

fun AnimeDetailDto.toEntity(): AnimeEntity {
    return AnimeEntity(
        id = malId,
        title = titleEnglish ?: title,
        synopsis = synopsis,
        episodes = episodes,
        rating = score,
        imageUrl = images.jpg?.imageUrl,
        trailerUrl = trailer?.embedUrl,
        genres = genres.joinToString(", ") { it.name }
    )
}




