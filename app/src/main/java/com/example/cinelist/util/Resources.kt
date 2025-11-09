package com.example.cinelist.util

import com.example.cinelist.data.model.Title
import com.example.cinelist.data.model.TitleDetailResponse

enum class MediaType {
    MOVIES, TV_SHOWS
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Title> = emptyList(),
    val tvShows: List<Title> = emptyList(),
    val error: String? = null,
    val selectedMediaType: MediaType = MediaType.MOVIES
)


data class DetailUiState(
    val isLoading: Boolean = false,
    val titleDetail: TitleDetailResponse? = null,
    val error: String? = null
)