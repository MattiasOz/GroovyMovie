package com.ltu.m7019e.themoviedb.model

import com.ltu.m7019e.themoviedb.database.MovieLocations

data class Movie(
    var id: Long = 0L,
    var title: String,
    var posterPath: String,
    var backdropPath: String,
    var releaseDate: String,
    var overview: String
)
