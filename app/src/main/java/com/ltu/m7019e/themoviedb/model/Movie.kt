package com.ltu.m7019e.themoviedb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ltu.m7019e.themoviedb.database.GenreConverters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favorite_movies")
data class Movie(
    @PrimaryKey
    @SerialName(value = "id")
    var id: Long = 0L,

    @SerialName(value = "title")
    var title: String,

    @SerialName(value = "poster_path")
    var posterPath: String,

    @SerialName(value = "backdrop_path")
    var backdropPath: String,

    @SerialName(value = "release_date")
    var releaseDate: String,

    @SerialName(value = "genre_ids")
    @TypeConverters(GenreConverters::class)
    var genreIDs: List<Int>,

    @SerialName(value = "overview")
    var overview: String,

)
