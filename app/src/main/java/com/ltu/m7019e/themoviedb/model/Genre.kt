package com.ltu.m7019e.themoviedb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName(value = "id")
    var id: Long = 0L,

    @SerialName(value = "name")
    var name: String,
)
