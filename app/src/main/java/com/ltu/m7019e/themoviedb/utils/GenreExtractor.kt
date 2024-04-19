package com.ltu.m7019e.themoviedb.utils

import com.ltu.m7019e.themoviedb.viewmodel.GenreListUiState


fun getGenreMap(genreListUiState: GenreListUiState): MutableMap<Long, String> {
    val res = mutableMapOf<Long, String>()
    when (genreListUiState) {
        is GenreListUiState.Success -> {
            for ((id, name) in  genreListUiState.genreList){
                res[id] = name
            }
            return res
        }
        else -> {
            return res
        }
    }
}

fun getGenresFromIDs(ids: List<Int>, genreMap: MutableMap<Long, String>): MutableList<String> {
    var res = mutableListOf<String>()
    for (id in ids) {
        genreMap[id.toLong()]?.let { res.add(it) }
    }
    return res
}