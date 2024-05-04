package com.ltu.m7019e.themoviedb.database

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ltu.m7019e.themoviedb.model.Movie
import com.ltu.m7019e.themoviedb.model.MovieResponse
import com.ltu.m7019e.themoviedb.network.MovieDBApiService
import com.ltu.m7019e.themoviedb.utils.Constants.RECONNECT_RELOAD_TAG
import com.ltu.m7019e.themoviedb.utils.Constants.RELOAD_PAGE_TAG
import com.ltu.m7019e.themoviedb.workers.ReconnectWorker

interface MoviesRepository {
    suspend fun  getPopularMovies(): MovieResponse

    suspend fun getTopRatedMovies(): MovieResponse

    fun schedulePopularReload()

    fun scheduleTopRatedReload()

    fun cancelWork()
}

enum class PageType {
    POPULAR, TOP_RATED
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

        val data = Data.Builder()
            .putString(RELOAD_PAGE_TAG, PageType.POPULAR.name)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReconnectWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(RECONNECT_RELOAD_TAG, ExistingWorkPolicy.REPLACE, workRequest)
    }

    override fun scheduleTopRatedReload() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val data = Data.Builder()
            .putString(RELOAD_PAGE_TAG, PageType.TOP_RATED.name)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReconnectWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(RECONNECT_RELOAD_TAG, ExistingWorkPolicy.REPLACE, workRequest)
    }

    override fun cancelWork() {
        workManager.cancelUniqueWork(RECONNECT_RELOAD_TAG)
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
        else return res.map { it.movie }
    }

    override suspend fun getTopRated(): List<Movie> {
        val res = movieDao.getTopRated()
        if(res.isEmpty()) throw Exception("No cached top rated movies")
        else return res.map { it.movie }
    }

    override suspend fun cachePopular(movies: List<Movie>) {
        if(movies.isEmpty()) throw Exception("Empty movie list.")
        movieDao.clearTopRated()
        var listNr = 0
        movieDao.cachePopular(movies.map { CachedPopular(it, listNr++) })
    }

    override suspend fun cacheTopRated(movies: List<Movie>) {
        if(movies.isEmpty()) throw Exception("Empty movie list.")
        movieDao.clearPopular()
        var listNr = 0
        movieDao.cacheTopRated(movies.map { CachedTopRated(it, listNr++) })
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