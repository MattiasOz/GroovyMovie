package com.ltu.m7019e.themoviedb.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ltu.m7019e.themoviedb.MovieDBApplication
import com.ltu.m7019e.themoviedb.database.CachedMovieRepository
import com.ltu.m7019e.themoviedb.database.CachedMoviesRepository
import com.ltu.m7019e.themoviedb.database.GenreRepository
import com.ltu.m7019e.themoviedb.database.ListCategory
import com.ltu.m7019e.themoviedb.database.MoviesRepository
import com.ltu.m7019e.themoviedb.database.SavedMovieRepository
import com.ltu.m7019e.themoviedb.model.Genre
import com.ltu.m7019e.themoviedb.model.Movie
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MovieListUiState{
    data class Success(val movieList: List<Movie>) : MovieListUiState
    object Loading : MovieListUiState
    object Error : MovieListUiState
}

sealed interface SelectedMovieUiState {
    data class Success(val movie: Movie, val isFavorite: Boolean) : SelectedMovieUiState
    object Loading : SelectedMovieUiState
    object Error : SelectedMovieUiState
}

sealed interface GenreListUiState{
    data class Success(val genreList: List<Genre>) : GenreListUiState
    object Loading : GenreListUiState
    object Error : GenreListUiState
}

class MovieDBViewModel(
    private val moviesRepository: MoviesRepository,
    private val genreRepository: GenreRepository,
    private val savedMovieRepository: SavedMovieRepository,
    private val cachedMovieRepository: CachedMovieRepository
): ViewModel() {
    var movieListUiState: MovieListUiState by mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by mutableStateOf(SelectedMovieUiState.Loading)
        private set

    var genreListUiState: GenreListUiState by mutableStateOf(GenreListUiState.Loading)
        private set

    init {
        getPopularMovies()
    }

    init {
        getGenres()
    }

    fun getGenres() {
        viewModelScope.launch {
            genreListUiState = GenreListUiState.Loading
            genreListUiState = try {
                GenreListUiState.Success(genreRepository.getGenres().genres)
            }catch (e: IOException) {
                GenreListUiState.Error
            }catch (e: HttpException) {
                GenreListUiState.Error
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                val movieList = moviesRepository.getPopularMovies().results
                cachedMovieRepository.cachePopular(movieList)
                MovieListUiState.Success(movieList)
            }catch (e: Exception) {
                try {
                    MovieListUiState.Success(cachedMovieRepository.getPopular())
                }catch (e: Exception) {
                    MovieListUiState.Error
                }
            }
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                val movieList = moviesRepository.getTopRatedMovies().results
                cachedMovieRepository.cacheTopRated(movieList)
                MovieListUiState.Success(movieList)
            }catch (e: Exception) {
                try {
                    MovieListUiState.Success(cachedMovieRepository.getTopRated())
                }catch (e: Exception) {
                    MovieListUiState.Error
                }
            }
        }
    }

    fun getSavedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(savedMovieRepository.getSavedMovies())
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun saveMovie(movie: Movie) {
        viewModelScope.launch {
            savedMovieRepository.insertMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, true)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            savedMovieRepository.deleteMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, false)
        }
    }

    fun setSelectedMovie(movie: Movie){
        viewModelScope.launch {
            selectedMovieUiState = SelectedMovieUiState.Loading
            selectedMovieUiState = try {
                SelectedMovieUiState.Success(movie, savedMovieRepository.getMovie(movie.id) != null)
            }catch (e: IOException) {
                SelectedMovieUiState.Error
            }catch (e: HttpException) {
                SelectedMovieUiState.Error
            }
        }
    }

    fun schedulePopularReload() {
        moviesRepository.schedulePopularReload()
    }

    fun scheduleTopRatedReload() {
        moviesRepository.scheduleTopRatedReload()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                val moviesRepository = application.container.moviesRepository
                val genreRepository = application.container.genreRepository
                val savedMovieRepository = application.container.savedMovieRepository
                val cachedMovieRepository = application.container.cachedMovieRepository
                MovieDBViewModel(
                    moviesRepository = moviesRepository,
                    genreRepository = genreRepository,
                    savedMovieRepository = savedMovieRepository,
                    cachedMovieRepository = cachedMovieRepository,
                )
            }
        }
    }
}