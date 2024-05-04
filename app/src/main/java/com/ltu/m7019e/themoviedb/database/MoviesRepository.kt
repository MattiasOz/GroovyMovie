package com.ltu.m7019e.themoviedb.database

import android.util.Log
import com.ltu.m7019e.themoviedb.model.Movie
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

interface SavedMovieRepository {
    suspend fun getSavedMovies(): List<Movie>
    suspend fun insertMovie(movie: Movie)
    suspend fun getMovie(id: Long): Movie?
    suspend fun deleteMovie(movie: Movie)
}

class SavedMoviesRepository(private val movieDao: MovieDao) : SavedMovieRepository {
    override suspend fun getSavedMovies(): List<Movie> {
        return movieDao.getFavoriteMovies()
    }

    override suspend fun insertMovie(movie: Movie) {
        movieDao.insertFavoriteMovie(movie)
    }

    override suspend fun getMovie(id: Long): Movie {
        return movieDao.getMovie(id)
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteFavoriteMovie(movie.id)
    }

}


interface CachedMovieRepository {
    suspend fun getPopular(): List<Movie>
    suspend fun getTopRated(): List<Movie>
    suspend fun cachePopular(movies: List<Movie>)
    suspend fun cacheTopRated(movies: List<Movie>)
}

class CachedMoviesRepository(private val movieDao: MovieDao) : CachedMovieRepository {

    override suspend fun getPopular(): List<Movie> {
        val res = movieDao.getPopular()
        if(res.isEmpty()) throw Exception("No cached popular movies")
        else return res
    }

    override suspend fun getTopRated(): List<Movie> {
        val res = movieDao.getTopRated()
        if(res.isEmpty()) throw Exception("No cached top rated movies")
        else return res
    }

    override suspend fun cachePopular(movies: List<Movie>) {
        if(movies.isEmpty()) throw Exception("Empty movie list.")
        movieDao.clearTopRated()
        movieDao.cachePopular(movies.map { CachedPopular(it) })
    }

    override suspend fun cacheTopRated(movies: List<Movie>) {
        if(movies.isEmpty()) throw Exception("Empty movie list.")
        movieDao.clearPopular()
        movieDao.cacheTopRated(movies.map { CachedTopRated(it) })
    }

    /*
    override suspend fun getPo(): List<Movie> {
        return movieDao.getCache()
    }
    override suspend fun cacheMovies(movies: List<Movie>, category: ListCategory) {
        Log.d("MoviesRepository", "Got to cache movies fun")
        movieDao.clearCache()
        movieDao.cache(movies.map { CachedMovie(it) })
    }*/
}