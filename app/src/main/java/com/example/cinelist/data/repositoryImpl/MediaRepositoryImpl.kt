package com.example.cinelist.data.repositoryImpl

import com.example.cinelist.data.api.ApiService
import com.example.cinelist.data.model.HomeMedia
import com.example.cinelist.data.model.TitleDetailResponse
import com.example.cinelist.data.repository.MediaRepository
import io.reactivex.Single

class MediaRepositoryImpl(
    private val apiService: ApiService,
    private val apiKey: String
) : MediaRepository {

    override fun getHomeMedia(): Single<HomeMedia> {
        return Single.zip(
            apiService.getMovies(apiKey),      // Call 1: Get movies
            apiService.getTvShows(apiKey),     // Call 2: Get TV shows

            // This function combines the results when both are successful
            { movieResponse, tvShowResponse ->
                HomeMedia(movieResponse.titles, tvShowResponse.titles)
            }
        )
    }
    override fun getTitleDetails(titleId: Int): Single<TitleDetailResponse> {
        return apiService.getTitleDetails(titleId, apiKey)
    }
}