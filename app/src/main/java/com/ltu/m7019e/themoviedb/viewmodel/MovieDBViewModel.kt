package com.ltu.m7019e.themoviedb.viewmodel

import androidx.lifecycle.ViewModel
import com.ltu.m7019e.themoviedb.database.MovieDBUIState
import com.ltu.m7019e.themoviedb.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieDBViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MovieDBUIState())
    val uiState: StateFlow<MovieDBUIState> = _uiState.asStateFlow()

    fun setSelectedMovie(movie: Movie){
        _uiState.update{ currentState ->
            currentState.copy(selectedMovie = movie)
        }
    }
}