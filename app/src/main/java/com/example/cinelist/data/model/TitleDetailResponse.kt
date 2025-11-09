package com.example.cinelist.data.model

import com.google.gson.annotations.SerializedName

data class TitleDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("plot_overview")
    val plotOverview: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("runtime_minutes")
    val runtimeMinutes: Int?,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("end_year")
    val endYear: Int?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("tmdb_id")
    val tmdbId: Int?,
    @SerializedName("tmdb_type")
    val tmdbType: String?,
    @SerializedName("genres")
    val genres: List<Int>?,
    @SerializedName("genre_names")
    val genreNames: List<String>?,
    @SerializedName("user_rating")
    val userRating: Double?,
    @SerializedName("critic_score")
    val criticScore: Int?,
    @SerializedName("us_rating")
    val usRating: String?,
    @SerializedName("poster")
    val poster: String?,
    @SerializedName("backdrop")
    val backdrop: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("networks")
    val networks: List<Int>?,
    @SerializedName("network_names")
    val networkNames: List<String>?,
    @SerializedName("relevance_percentile")
    val relevancePercentile: Double?
)
