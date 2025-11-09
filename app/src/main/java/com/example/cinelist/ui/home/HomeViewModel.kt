package com.example.cinelist.ui.home

import androidx.lifecycle.ViewModel
import com.example.cinelist.data.repository.MediaRepository
import com.example.cinelist.util.HomeUiState
import com.example.cinelist.util.MediaType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(private val mediaRepository: MediaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val disposables = CompositeDisposable()

    init {
        loadHomeMedia()
    }

    fun loadHomeMedia() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        val subscription = mediaRepository.getHomeMedia()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { homeMedia ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            movies = homeMedia.movies,
                            tvShows = homeMedia.tvShows,
                            error = null
                        )
                    }
                },
                { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "An unexpected error occurred"
                        )
                    }
                }
            )

        disposables.add(subscription)
    }

    fun toggleMediaType(mediaType: MediaType) {
        _uiState.update { it.copy(selectedMediaType = mediaType) }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}