package com.example.cinelist.data.repository

import com.example.cinelist.data.model.HomeMedia
import com.example.cinelist.data.model.TitleDetailResponse
import io.reactivex.Single

interface MediaRepository {

    fun getHomeMedia(): Single<HomeMedia>
    fun getTitleDetails(titleId: Int): Single<TitleDetailResponse>
}