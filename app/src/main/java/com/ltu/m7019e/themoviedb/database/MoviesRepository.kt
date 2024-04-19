package com.ltu.m7019e.themoviedb.database

import com.ltu.m7019e.themoviedb.model.MovieResponse
import com.ltu.m7019e.themoviedb.network.MovieDBApiService

interface MoviesRepository {
    suspend fun  getPopularMovies(): MovieResponse

    suspend fun getTopRatedMovies(): MovieResponse
}


class NetworkMoviesRepository(
    private val apiService: MovieDBApiService
) : MoviesRepository {
    override suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return apiService.getTopRatedMovies()
    }

}
