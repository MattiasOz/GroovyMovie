package com.ltu.m7019e.themoviedb.database

import com.ltu.m7019e.themoviedb.model.Movie

class MovieLocations(locations: List<String>) {
    fun getLocations(movie: Movie): List<String>? {
        return when (movie.id){
            1L -> listOf("Malmö", "New York")
            2L -> listOf("Malmö", "London")
            3L -> listOf("Skrockholm", "Frankrike")
            4L -> listOf("Månen", "Pluto", "Alpha Centauri")
            5L -> null
            else -> null
        }
    }
}