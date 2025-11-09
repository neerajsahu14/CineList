package com.example.cinelist.data.model

import com.google.gson.annotations.SerializedName

data class TvShowListResponse(
    @SerializedName("titles")
    val titles: List<Title> = emptyList(),
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0
)
