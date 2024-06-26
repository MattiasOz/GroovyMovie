package com.ltu.m7019e.themoviedb.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.themoviedb.model.Movie
import com.ltu.m7019e.themoviedb.utils.Constants
import com.ltu.m7019e.themoviedb.viewmodel.MovieListUiState
import com.ltu.m7019e.themoviedb.utils.getGenresFromIDs

@Composable
fun MovieListScreen(
    movieListUiState: MovieListUiState,
    genreMap: Map<Long, String>,
    onMovieListItemClicked: (Movie) -> Unit,
    scheduleReload: () -> Unit,
    cancelScheduleReload: () -> Unit,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
){
    when(windowSize) {
        WindowWidthSizeClass.Compact -> {
            CompactScreen(
                movieListUiState = movieListUiState,
                genreMap = genreMap,
                onMovieListItemClicked = onMovieListItemClicked,
                scheduleReload = scheduleReload,
                cancelScheduleReload = cancelScheduleReload,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Medium -> {
            CompactScreen(
                movieListUiState = movieListUiState,
                genreMap = genreMap,
                onMovieListItemClicked = onMovieListItemClicked,
                scheduleReload = scheduleReload,
                cancelScheduleReload = cancelScheduleReload,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Expanded -> {      // TODO: only "compact screen" is necessary but we need horizontal scrolling
            ExpandedScreen(
                movieListUiState = movieListUiState,
                genreMap = genreMap,
                onMovieListItemClicked = onMovieListItemClicked,
                scheduleReload = scheduleReload,
                cancelScheduleReload = cancelScheduleReload,
                modifier = modifier.fillMaxHeight()
            )
        }
    }

}

@Composable
fun CompactScreen(
    movieListUiState: MovieListUiState,
    genreMap: Map<Long, String>,
    onMovieListItemClicked: (Movie) -> Unit,
    scheduleReload: () -> Unit,
    cancelScheduleReload: () -> Unit,
    modifier: Modifier = Modifier
){
    LazyVerticalGrid(columns = GridCells.Adaptive(300.dp), modifier = modifier) {
        when(movieListUiState) {
            is MovieListUiState.Success -> {
                cancelScheduleReload()
                items(movieListUiState.movieList) {movie ->
                    val genreList = getGenresFromIDs(movie.genreIDs, genreMap)
                    MovieListItemCard(
                        movie = movie,
                        genreList = genreList,
                        onMovieListItemClicked = onMovieListItemClicked,
                        modifier = Modifier
                            .padding(8.dp)//.background(Color(0xffffffff))
                            .fillMaxWidth()
                    )
                }
            }
            is MovieListUiState.Loading -> {
                item {
                    Text(
                        text = "Loading",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is MovieListUiState.Error -> {
                scheduleReload()
                item {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandedScreen(
    movieListUiState: MovieListUiState,
    genreMap: Map<Long, String>,
    onMovieListItemClicked: (Movie) -> Unit,
    scheduleReload: () -> Unit,
    cancelScheduleReload: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(rows = GridCells.Adaptive(200.dp), modifier = modifier) {
        when(movieListUiState) {
            is MovieListUiState.Success -> {
                cancelScheduleReload()
                items(movieListUiState.movieList) {movie ->
                    val genreList = getGenresFromIDs(movie.genreIDs, genreMap)
                    MovieListItemVerticalCard(
                        movie = movie,
                        genreList = genreList,
                        onMovieListItemClicked = onMovieListItemClicked,
                        modifier = Modifier
                            .padding(8.dp)//.background(Color(0xffffffff))
                            .fillMaxHeight()
                            //.fillMaxWidth(0.3f)
                            .width(300.dp)
                    )
                }
            }
            is MovieListUiState.Loading -> {
                item {
                    Text(
                        text = "Loading",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is MovieListUiState.Error -> {
                scheduleReload()
                item {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListItemVerticalCard(
    movie: Movie,
    genreList: List<String>,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {
            onMovieListItemClicked(movie)
        },
        modifier = modifier
    ) {
        Row {
            Box{
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_WIDTH + movie.posterPath,
                    contentDescription = null,
                    modifier = Modifier
                        .width(92.dp)
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Row {
                    Text(
                        text = movie.releaseDate,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                    Genres(genreList) // <----
                }
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    //maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListItemCard(
    movie: Movie,
    genreList: List<String>,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        onClick = {
              onMovieListItemClicked(movie)
        },
        modifier = modifier
    ) {
        Row {
            Box{
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_WIDTH + movie.posterPath,
                    contentDescription = null,
                    modifier = Modifier
                        .width(92.dp)
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Row {
                    Text(
                        text = movie.releaseDate,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                    Genres(genreList) // <----
                }
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
            }
        }
    }
}

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

/*
@Preview(showBackground = true)
@Composable
fun MovieDetailPreview() {
    TheMovieDBTheme {
        MovieListItemCard(
            movie = Movie(
                1,
                "Raya and the Last Dragon",
                "/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
                "/9xeEGUZjgiKlI69jwIOi0hjKUIk.jpg",
                "2021-03-03",
                "Long ago, in the fantasy world of Kumandra, humans and dragons lived together in harmony. But when an evil force threatened the land, the dragons sacrificed themselves to save humanity. Now, 500 years later, that same evil has returned and it’s up to a lone warrior, Raya, to track down the legendary last dragon to restore the fractured land and its divided people."
            ), {}
        )
    }
}*/