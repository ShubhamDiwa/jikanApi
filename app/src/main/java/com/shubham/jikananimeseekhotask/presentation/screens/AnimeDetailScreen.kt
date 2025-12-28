package com.shubham.jikananimeseekhotask.presentation.screens

import android.R
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shubham.jikananimeseekhotask.data.local.AnimeEntity
import com.shubham.jikananimeseekhotask.presentation.viewmodels.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    animeId: Int,
    viewModel: AnimeViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetail(animeId)
    }

    val state = viewModel.stateDetail.collectAsState().value

    Scaffold(
    ) { padding ->
        if (state.isOffline) {
            Snackbar {
                Text("You're offline")
            }
        }
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            state.anime != null -> {
                AnimeDetailContent(
                    anime = state.anime,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}



@Composable
fun AnimeDetailContent(
    anime: AnimeEntity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        
        when {
            !anime.trailerUrl.isNullOrEmpty() -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color.Red)
                ) {

                    TrailerOrPosterSection(
                        trailerUrl = anime.trailerUrl,
                        posterUrl = anime.imageUrl,
                        title = anime.title
                    )
                }
            }
            !anime.trailerUrl.isNullOrEmpty() -> {
                Log.e("trailer ", "AnimeDetailContent:${anime.trailerUrl} ", )
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.title,
                    modifier = Modifier
                        .background(color = Color.Red)
                        .fillMaxWidth()
                        .height(220.dp),

                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_media_play),
                    error = painterResource(R.drawable.ic_media_play)
                )
            }

            else -> {
                Log.e("Poster->else", "AnimeDetailContent:in Else ", )
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = "No media",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = anime.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        Text("⭐ Rating: ${anime.rating ?: "N/A"}")
        Text("🎞 Episodes: ${anime.episodes ?: "N/A"}")

        Spacer(Modifier.height(12.dp))

        Text(
            text = anime.synopsis ?: "No synopsis available",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(12.dp))

        if (!anime.genres.isNullOrEmpty()) {
            Text(
                text = "Genres: ${anime.genres}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun TrailerOrPosterSection(trailerUrl: String, posterUrl: String?, title: String) {

    val context = LocalContext.current
    val videoId = trailerUrl?.let { extractYouTubeVideoId(it) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Black)
    ) {
        when {
            videoId != null -> {
                TrailerThumbnailPlayer(
                    videoId = videoId,
                    onClick = {

                        val youtubeAppIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube:$videoId")
                        )
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=$videoId")
                        )

                        try {
                            context.startActivity(youtubeAppIntent)
                        } catch (e: Exception) {
                            context.startActivity(browserIntent)
                        }
                    }
                )
            }

            !posterUrl.isNullOrEmpty() -> {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_media_play)
                )
            }

            // Case 3: Neither available - Show placeholder
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No Media Available",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
    
}

@Composable
fun TrailerThumbnailPlayer(videoId: String, onClick: () -> Unit) {
    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = "Trailer Thumbnail",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_media_play)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Red,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Trailer",
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            color = Color.Red,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "TRAILER",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun extractYouTubeVideoId(url: String): String? {
    return try {
        when {
            url.contains("youtube-nocookie.com/embed/") -> {
                url.substringAfter("embed/").substringBefore("?")
            }
            url.contains("youtu.be/") -> {
                url.substringAfter("youtu.be/").substringBefore("?")
            }
            url.contains("youtube.com/watch?v=") -> {
                url.substringAfter("v=").substringBefore("&")
            }
            url.contains("youtube.com/embed/") -> {
                url.substringAfter("embed/").substringBefore("?")
            }
            else -> null
        }
    } catch (e: Exception) {
        Log.e("extractYouTubeVideoId", "Exception: ${e.message}")
        null
    }
}



