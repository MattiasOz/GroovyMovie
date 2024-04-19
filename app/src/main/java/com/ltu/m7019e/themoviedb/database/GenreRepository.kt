package com.ltu.m7019e.themoviedb.database

import com.ltu.m7019e.themoviedb.model.GenreResponse
import com.ltu.m7019e.themoviedb.model.MovieResponse
import com.ltu.m7019e.themoviedb.network.MovieDBApiService

interface GenreRepository {
    suspend fun getGenres(): GenreResponse
}

class NetworkGenreRepository (
    private val apiService: MovieDBApiService
) : GenreRepository {
    override suspend fun getGenres(): GenreResponse {
        return apiService.getGenreList()
    }

}