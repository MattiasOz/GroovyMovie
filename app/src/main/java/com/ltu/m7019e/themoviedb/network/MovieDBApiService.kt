package com.ltu.m7019e.themoviedb.network

import com.ltu.m7019e.themoviedb.model.GenreResponse
import com.ltu.m7019e.themoviedb.model.MovieResponse
import com.ltu.m7019e.themoviedb.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDBApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenreList(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): GenreResponse
}