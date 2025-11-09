package com.example.cinelist.data.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("titles")
    val titles: List<Title> = emptyList(),
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0
)

data class Title(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("tmdb_id")
    val tmdbId: Int?,
    @SerializedName("tmdb_type")
    val tmdbType: String?,
    @SerializedName("type")
    val type: String
)
