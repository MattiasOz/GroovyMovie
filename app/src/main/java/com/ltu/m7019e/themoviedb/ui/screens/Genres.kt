package com.ltu.m7019e.themoviedb.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun Genres(
    genreList: List<String>,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    var genreText = ""
    for (genre in genreList) {
        genreText = genreText + genre + if (genre != genreList.last())", " else " " // make last not have comma
    }
    Text(
        text = genreText,
        style = MaterialTheme.typography.bodySmall,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}