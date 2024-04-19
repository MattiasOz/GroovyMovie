package com.ltu.m7019e.themoviedb.database

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ltu.m7019e.themoviedb.network.MovieDBApiService
import com.ltu.m7019e.themoviedb.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val moviesRepository : MoviesRepository
    val genreRepository : GenreRepository
}

class DefaultAppContainer : AppContainer {
    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    val movieDBJson = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(movieDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.SERVER_BASE_URL)
        .build()

    private val retrofitService: MovieDBApiService by lazy {
        retrofit.create(MovieDBApiService::class.java)
    }

    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitService)
    }

    override val genreRepository: GenreRepository by lazy {
        NetworkGenreRepository(retrofitService)
    }

/*
    private val retrofitGenre: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(movieDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.GENRE_LIST_BASE_URL)
        .build()

    private val retrofitGenreService: MovieDBApiService by lazy {
        retrofitGenre.create(MovieDBApiService::class.java)
    }

    override val genreRepository: GenreRepository by lazy {
        NetworkGenreRepository(retrofitGenreService)
    }

 */
}
