package com.ltu.m7019e.themoviedb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName(value = "genres")
    var genres: List<Genre> = listOf(),
)
