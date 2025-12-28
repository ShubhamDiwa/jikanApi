package com.shubham.jikananimeseekhotask.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.jikananimeseekhotask.data.remote.NetworkHelper
import com.shubham.jikananimeseekhotask.data.remote.apis.AnimeDetailState
import com.shubham.jikananimeseekhotask.data.remote.apis.AnimeListState
import com.shubham.jikananimeseekhotask.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository ,
    private val networkHelper: NetworkHelper

) : ViewModel() {
    private val _state = MutableStateFlow(AnimeListState())
    val state: StateFlow<AnimeListState> = _state.asStateFlow()

    private var isFirstLaunchOffline = false

    private val _stateDetail = MutableStateFlow(AnimeDetailState())
    val stateDetail: StateFlow<AnimeDetailState> = _stateDetail

    private var currentPage = 1
    private var isRequestInProgress = false


    init {
        observeAnimeFromDb()
        loadNextPage()
        
    }

    fun loadNextPage() {
        Log.e(
            "PAGINATION_CHECK",
            "inProgress=$isRequestInProgress hasNext=${_state.value.hasNextPage}"
        )

        if (isRequestInProgress || !_state.value.hasNextPage) return

        viewModelScope.launch {
            isRequestInProgress = true
            _state.value = _state.value.copy(isLoading = true)

            if (!networkHelper.isOnline() && _state.value.animeList.isEmpty()) {
                Log.e("Offline ", "loadNextPage: ", )
                isFirstLaunchOffline = true
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "",
                    hasNextPage = true
                )
                isRequestInProgress = false
                return@launch
            }

            if (networkHelper.isOnline()) {

                Log.e("Online", "loadNextPage: ", )
                isFirstLaunchOffline = false

                val hasNext = repository.fetchAnime(currentPage)

                _state.value = _state.value.copy(
                    isLoading = false,
                    hasNextPage = hasNext
                )

                if (hasNext) currentPage++
            }

            isRequestInProgress = false
        }
    }

    private fun observeAnimeFromDb() {
        viewModelScope.launch {
            repository.getAnimeFromDb().collectLatest { list ->
                if (isFirstLaunchOffline && list.isEmpty()) return@collectLatest
                _state.value = _state.value.copy(animeList = list, error = null, isLoading = false)
            }
        }

    }



    fun loadAnimeDetail(animeId: Int) {
        viewModelScope.launch {

            _stateDetail.value = AnimeDetailState(isLoading = true)

            try {
                val isOnline = networkHelper.isOnline()

                val localAnime = repository.getAnimeByIdFromDb(animeId)
                if (localAnime != null) {
                    _stateDetail.value = AnimeDetailState(
                        anime = localAnime,
                            isOffline = !isOnline
                    )
                }


                if (isOnline) {
                    val apiAnime = repository.fetchAnimeDetail(animeId)
                    _stateDetail.value = AnimeDetailState(
                        anime = apiAnime,
                        isOffline = false
                    )
                }

            } catch (e: Exception) {
                _stateDetail.value = AnimeDetailState(
                    error = e.message ?: "No internet & no cached data"
                )
            }
        }
    }
}