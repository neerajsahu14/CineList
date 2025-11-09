package com.example.cinelist.data.api

import com.example.cinelist.data.model.MovieListResponse
import com.example.cinelist.data.model.TitleDetailResponse
import com.example.cinelist.data.model.TvShowListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Call 1: Get a list of popular movies
    @GET("v1/list-titles")
    fun getMovies(
        @Query("apiKey") apiKey: String,
        @Query("types") types: String = "movie",
        @Query("sort_by") sortBy: String = "popularity_desc"
    ): Single<MovieListResponse>


    @GET("v1/list-titles")
    fun getTvShows(
        @Query("apiKey") apiKey: String,
        @Query("types") types: String = "tv_series",
        @Query("sort_by") sortBy: String = "popularity_desc"
    ): Single<TvShowListResponse>

    // Call 3: Get details for a specific item
    @GET("v1/title/{id}/details")
    fun getTitleDetails(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): Single<TitleDetailResponse>
}