package com.shubham.jikananimeseekhotask

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shubham.jikananimeseekhotask.data.local.AnimeEntity
import com.shubham.jikananimeseekhotask.presentation.screens.AnimeDetailScreen
import com.shubham.jikananimeseekhotask.presentation.viewmodels.AnimeViewModel
import com.shubham.jikananimeseekhotask.ui.theme.JikanAnimeSeekhoTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JikanAnimeSeekhoTaskTheme {
                var selectedAnimeId by remember { mutableStateOf<Int?>(null) }


                Scaffold( topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (selectedAnimeId == null)
                                    "Anime List"
                                else
                                    "Anime Details"
                            )
                        },
                        navigationIcon = {
                            if (selectedAnimeId != null) {
                                IconButton(onClick = { selectedAnimeId = null }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        }
                    )
                }) {
                    

                    innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {


                        if (selectedAnimeId == null) {
                            AnimeListScreen(
                                onAnimeClick = { animeId ->
                                    selectedAnimeId = animeId
                                    Log.d("AnimeClick", "Clicked anime id: $animeId")
                                }
                            )
                        } else {
                            AnimeDetailScreen(
                                animeId = selectedAnimeId!!,
                                onBack = { selectedAnimeId = null }
                            )
                        }
                    }
                }



                }
            }
        }
    }

@Composable
fun AnimeListScreen(
    viewModel: AnimeViewModel = hiltViewModel(),
    onAnimeClick:  (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            itemsIndexed(state.animeList) { index, anime ->

                AnimeCard(
                    anime = anime,
                    onClick = { onAnimeClick(anime.id) }
                )

                if (
                    index == state.animeList.lastIndex &&
                    state.hasNextPage &&
                    !state.isLoading
                ) {
                    viewModel.loadNextPage()
                }
            }

            // 🔹 Pagination loader
            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        state.error?.let { errorMsg ->
            Log.e("ERROR", "AnimeListScreen: in error ", )
            Text(
                text = errorMsg,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
            FirstLaunchOfflineUI(onRetry = { viewModel.loadNextPage() })
        }
    }

}
@Composable
fun FirstLaunchOfflineUI(onRetry: () -> Unit) {
    Log.e("InERROR", "FirstLaunchOfflineUI: ", )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Refresh, contentDescription = null)

        Spacer(Modifier.height(8.dp))

        Text("No internet connection")
        Text("Connect to the internet and try again")

        Spacer(Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun AnimeCard(anime: AnimeEntity, onClick:  () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier
                    .size(width = 90.dp, height = 120.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Episodes: ${anime.episodes ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Rating: ${anime.rating ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}



