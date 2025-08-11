package com.example.rickandmorty.di

import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.repo.CharactersRepository
import com.example.rickandmorty.viewmodel.CharactersViewModel
import com.example.rickandmorty.viewmodel.CharacterDetailViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val appModule = module {
    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single<RickAndMortyApi> { get<Retrofit>().create(RickAndMortyApi::class.java) }
    single { CharactersRepository(get()) }
    viewModel { CharactersViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
}

