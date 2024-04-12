package com.ltu.m7019e.themoviedb.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.themoviedb.database.Movies
import com.ltu.m7019e.themoviedb.model.Movie
import com.ltu.m7019e.themoviedb.ui.theme.TheMovieDBTheme
import com.ltu.m7019e.themoviedb.utils.Constants


@Composable
fun MovieDetailScreen(
    movie: Movie,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Box {
            AsyncImage(
                model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + movie.backdropPath,
                contentDescription = movie.title,
                modifier = Modifier,
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier = Modifier.size(8.dp)
        )
        Text(
            text = movie.releaseDate,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(
            modifier = Modifier.size(8.dp)
        )
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier.size(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    TheMovieDBTheme {
        MovieDetailScreen(
            movie = Movies().getMovies()[0]
        )
    }
}