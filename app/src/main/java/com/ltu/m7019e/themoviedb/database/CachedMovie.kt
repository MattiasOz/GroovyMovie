package com.ltu.m7019e.themoviedb.database

import androidx.room.Embedded
import androidx.room.Entity
import com.ltu.m7019e.themoviedb.model.Movie

@Entity(tableName = "cached_popular", primaryKeys = ["id"])
data class CachedPopular(
    @Embedded
    var movie: Movie,
    var listNr: Int
)

@Entity(tableName = "cached_top_rated", primaryKeys = ["id"])
data class CachedTopRated(
    @Embedded
    var movie: Movie,
    var listNr: Int
)
