package com.ltu.m7019e.themoviedb.database

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ltu.m7019e.themoviedb.model.Movie
import com.ltu.m7019e.themoviedb.model.MovieResponse
import com.ltu.m7019e.themoviedb.network.MovieDBApiService
import com.ltu.m7019e.themoviedb.utils.Constants
import com.ltu.m7019e.themoviedb.workers.ReconnectWorker

interface MoviesRepository {
    suspend fun  getPopularMovies(): MovieResponse

    suspend fun getTopRatedMovies(): MovieResponse

    fun schedulePopularReload()
}


class NetworkMoviesRepository(
    private val apiService: MovieDBApiService,
    context: Context
) : MoviesRepository {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return apiService.getTopRatedMovies()
    }

    override fun schedulePopularReload() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReconnectWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(Constants.RECONNECT_RELOAD_TAG, ExistingWorkPolicy.REPLACE, workRequest)
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