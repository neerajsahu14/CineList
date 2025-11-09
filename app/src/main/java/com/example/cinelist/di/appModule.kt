package com.example.cinelist.di

import com.example.cinelist.data.repositoryImpl.MediaRepositoryImpl
import com.example.cinelist.data.api.ApiService
import com.example.cinelist.data.repository.MediaRepository
import com.example.cinelist.ui.home.HomeViewModel
import com.example.cinelist.ui.detail.DetailViewModel
import com.example.cinelist.util.Constants
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // Provides a single instance of Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(Constants.WATCHMODE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    // Provides the ApiService
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }

    // Provides the API Key
    single { Constants.WATCHMODE_API_KEY }

    // Provides the Repository implementation
    single<MediaRepository> { MediaRepositoryImpl(get(), get()) }

    // Provides the HomeViewModel
    viewModel { HomeViewModel(get()) }

    // Provides the DetailViewModel
    viewModel { params -> DetailViewModel(get(), params.get()) }
}