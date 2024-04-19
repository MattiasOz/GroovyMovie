package com.ltu.m7019e.themoviedb.database

import com.ltu.m7019e.themoviedb.model.Movie
import com.ltu.m7019e.themoviedb.viewmodel.MovieListUiState


data class MovieDBUIState(
    val selectedMovie: Movie? = null,
    val movieListUiState: MovieListUiState = MovieListUiState.Loading
)
