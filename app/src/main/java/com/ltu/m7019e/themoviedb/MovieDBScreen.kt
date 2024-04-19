package com.ltu.m7019e.themoviedb

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ltu.m7019e.themoviedb.ui.screens.AboutPageScreen
import com.ltu.m7019e.themoviedb.ui.screens.MovieDetailScreen
import com.ltu.m7019e.themoviedb.ui.screens.MovieListScreen
import com.ltu.m7019e.themoviedb.utils.getGenreMap
import com.ltu.m7019e.themoviedb.viewmodel.MovieDBViewModel


enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Detail(title = R.string.movie_details),
    About(title = R.string.about_page)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDBAppBar(
    currentScreen: MovieDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        actions = {
            if(currentScreen != MovieDBScreen.About) {
                IconButton(onClick = {navigateAbout()}) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = stringResource(R.string.about_button)
                    )
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}


@Composable
fun TheMovieDBApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = MovieDBScreen.valueOf(
        backStackEntry?.destination?.route ?: MovieDBScreen.List.name
    )

    Scaffold(
        topBar = {
            MovieDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navigateAbout = { navController.navigate(MovieDBScreen.About.name) }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        val movieDBViewModel: MovieDBViewModel = viewModel(factory = MovieDBViewModel.Factory)
        val genreMap = getGenreMap(movieDBViewModel.genreListUiState)

        NavHost(
            navController = navController,
            startDestination = MovieDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = MovieDBScreen.List.name) {
                MovieListScreen(
                    movieListUiState = movieDBViewModel.movieListUiState,
                    genreMap = genreMap,
                    onMovieListItemClicked = { movie ->
                        movieDBViewModel.setSelectedMovie(movie)
                        navController.navigate(MovieDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            composable(route = MovieDBScreen.Detail.name) {
                    MovieDetailScreen(
                        selectedMovieUiState = movieDBViewModel.selectedMovieUiState,
                        genreMap = genreMap,
                        modifier = Modifier
                    )
            }
            composable(route = MovieDBScreen.About.name){
                AboutPageScreen()
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
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