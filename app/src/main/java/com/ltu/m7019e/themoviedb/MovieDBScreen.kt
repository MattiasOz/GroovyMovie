package com.ltu.m7019e.themoviedb

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ltu.m7019e.themoviedb.workers.ReconnectWorker


enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Detail(title = R.string.movie_details),
    About(title = R.string.about_page)
}

//private var page: PageType = PageType.TOP_RATED
private var scheduleReload: (() -> Unit)? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDBAppBar(
    currentScreen: MovieDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateAbout: () -> Unit,
    movieDBViewModel: MovieDBViewModel,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        actions = {
            if (!canNavigateBack) {
                IconButton(
                    onClick = {
                        menuExpanded = ! menuExpanded
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Open Menu to select different movie lists"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.popular_movies)) },
                        onClick = {
                            scheduleReload = movieDBViewModel::schedulePopularReload
                            movieDBViewModel.getPopularMovies()
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.top_rated_movies)) },
                        onClick = {
                            scheduleReload = movieDBViewModel::scheduleTopRatedReload
                            movieDBViewModel.getTopRatedMovies()
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.saved_movies)) },
                        onClick = {
                            movieDBViewModel.getSavedMovies()
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "About Us") },
                        onClick = {
                            navigateAbout()
                            menuExpanded = false
                        }
                    )
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}


@Composable
fun TheMovieDBApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = MovieDBScreen.valueOf(
        backStackEntry?.destination?.route ?: MovieDBScreen.List.name
    )
    val movieDBViewModel: MovieDBViewModel = viewModel(factory = MovieDBViewModel.Factory)
    ReconnectWorker.setViewModel(movieDBViewModel)

    Scaffold(
        topBar = {
            MovieDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navigateAbout = { navController.navigate(MovieDBScreen.About.name) },
                movieDBViewModel = movieDBViewModel,
            )
        },
        modifier = modifier
    ) { innerPadding ->
        val genreMap = getGenreMap(movieDBViewModel.genreListUiState)

//        val scheduleReload by when (page) {
//            PageType.POPULAR -> remember { mutableStateOf(movieDBViewModel::schedulePopularReload) }
//            PageType.TOP_RATED -> remember { mutableStateOf(movieDBViewModel::scheduleTopRatedReload) }
//        }
//        val scheduleReload = when (page) {
//            PageType.POPULAR -> movieDBViewModel::schedulePopularReload
//            PageType.TOP_RATED -> movieDBViewModel::scheduleTopRatedReload
//        }
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
                    scheduleReload = scheduleReload?: movieDBViewModel::schedulePopularReload,
                    windowSize = windowSize,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            composable(route = MovieDBScreen.Detail.name) {
                MovieDetailScreen(
                    movieDBViewModel = movieDBViewModel,
                    genreMap = genreMap,
                    windowSize = windowSize,
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