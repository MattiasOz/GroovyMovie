package com.ltu.m7019e.themoviedb.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.themoviedb.ui.theme.TheMovieDBTheme
import com.ltu.m7019e.themoviedb.utils.Constants
import com.ltu.m7019e.themoviedb.viewmodel.SelectedMovieUiState


@Composable
fun MovieDetailScreen(
    selectedMovieUiState: SelectedMovieUiState,
    modifier: Modifier = Modifier
){

    when (selectedMovieUiState) {
        is SelectedMovieUiState.Success -> {
            val ctx = LocalContext.current
            val intent = remember {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + selectedMovieUiState.movie.backdropPath)
                )
            }
            Column(
                modifier = modifier
            ) {
                Box {
                    AsyncImage(
                        model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + selectedMovieUiState.movie.backdropPath,
                        contentDescription = selectedMovieUiState.movie.title,
                        modifier = Modifier.clickable {
                            ctx.startActivity(intent)
                        },
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = selectedMovieUiState.movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Text(
                    text = selectedMovieUiState.movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Text(
                    text = selectedMovieUiState.movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
            }

        }
        is SelectedMovieUiState.Loading -> TODO()
        is SelectedMovieUiState.Error -> TODO()
    }

}

/*
@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    TheMovieDBTheme {
        MovieDetailScreen(
            selectedMovieUiState.movie = Movies().getMovies()[0]
        )
    }
}

 */