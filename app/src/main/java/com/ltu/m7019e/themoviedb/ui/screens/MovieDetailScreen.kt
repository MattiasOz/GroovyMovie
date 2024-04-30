package com.ltu.m7019e.themoviedb.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.themoviedb.R
import com.ltu.m7019e.themoviedb.utils.Constants
import com.ltu.m7019e.themoviedb.utils.getGenresFromIDs
import com.ltu.m7019e.themoviedb.viewmodel.MovieDBViewModel
import com.ltu.m7019e.themoviedb.viewmodel.SelectedMovieUiState


@Composable
fun MovieDetailScreen(
    movieDBViewModel: MovieDBViewModel,
    genreMap: Map<Long, String>,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
){
    val selectedMovieUiState = movieDBViewModel.selectedMovieUiState
    when (selectedMovieUiState) {
        is SelectedMovieUiState.Success -> {
            val ctx = LocalContext.current
            val intent = remember {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + selectedMovieUiState.movie.backdropPath)
                )
            }
            val genreList = getGenresFromIDs(selectedMovieUiState.movie.genreIDs, genreMap)
            when(windowSize) {
                WindowWidthSizeClass.Compact -> {
                    CompactScreen(
                        movieDBViewModel = movieDBViewModel,
                        selectedMovieUiState = selectedMovieUiState,
                        genreList = genreList,
                        ctx = ctx,
                        intent = intent,
                        modifier = modifier
                    )
                }
                else -> {
                    ExpandedScreen(
                        movieDBViewModel = movieDBViewModel,
                        selectedMovieUiState = selectedMovieUiState,
                        genreList = genreList,
                        ctx = ctx,
                        intent = intent,
                        modifier = modifier
                    )
                }
            }


        }
        is SelectedMovieUiState.Loading -> {
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodySmall
            )
        }
        is SelectedMovieUiState.Error -> {
            Text(
                text = stringResource(R.string.error),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}

@Composable
fun CompactScreen(
    movieDBViewModel: MovieDBViewModel,
    selectedMovieUiState: SelectedMovieUiState.Success,
    genreList: List<String>,
    ctx: Context,
    intent: Intent,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item(0) {
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

            Column(

                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = selectedMovieUiState.movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Row {
                    Text(
                        text = selectedMovieUiState.movie.releaseDate,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                    //Genres(genreList, maxLines = Int.MAX_VALUE)
                    GenresScrollable(genreList)
                }
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.favorite),
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Switch(
                        checked = selectedMovieUiState.isFavorite,
                        onCheckedChange = {
                            if (it) {
                                movieDBViewModel.saveMovie(selectedMovieUiState.movie)
                            } else {
                                movieDBViewModel.deleteMovie(selectedMovieUiState.movie)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandedScreen(
    movieDBViewModel: MovieDBViewModel,
    selectedMovieUiState: SelectedMovieUiState.Success,
    genreList: List<String>,
    ctx: Context,
    intent: Intent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Box {
            AsyncImage(
                model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + selectedMovieUiState.movie.backdropPath,
                contentDescription = selectedMovieUiState.movie.title,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clickable {
                        ctx.startActivity(intent)
                    },
                contentScale = ContentScale.Crop
            )
        }
        LazyColumn(
        ) {
            item(0) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = selectedMovieUiState.movie.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                    Row {
                        Text(
                            text = selectedMovieUiState.movie.releaseDate,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(
                            modifier = Modifier.size(8.dp)
                        )
                        //Genres(genreList, maxLines = Int.MAX_VALUE)
                        GenresScrollable(genreList)
                    }
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.favorite),
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Switch(
                            checked = selectedMovieUiState.isFavorite,
                            onCheckedChange = {
                                if (it) {
                                    movieDBViewModel.saveMovie(selectedMovieUiState.movie)
                                } else {
                                    movieDBViewModel.deleteMovie(selectedMovieUiState.movie)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenresScrollable(genreList: List<String>) {
    LazyRow {
        items(genreList) { genre ->
            Text(text = genre + if (genre != genreList.last())", " else " ",
            style = MaterialTheme.typography.bodySmall
            )
        }
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