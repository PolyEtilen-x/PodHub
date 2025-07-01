package com.example.podhub.ui.navigation

import androidx.compose.runtime.Composable
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.podhub.IntroScreen1
import com.example.podhub.IntroScreen2
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.ui.feature.artist.ArtistSelectionScreen
import com.example.podhub.ui.feature.home.PodcastDetailScreen
import com.example.podhub.ui.feature.home.GenreDetailScreen
import com.example.podhub.ui.feature.login.LoginScreen
import com.example.podhub.ui.feature.home.HomeScreen
import com.example.podhub.ui.feature.home.PlayerScreen
import com.example.podhub.ui.feature.home.PodcastCategoriesScreen
import com.example.podhub.ui.feature.library.LibraryPodcastsScreen
import com.example.podhub.ui.feature.login.LoginViewModel
import com.example.podhub.ui.feature.podcast.PodcastCategoryScreen
//import com.example.podhub.ui.feature.room.RoomListScreen
import com.example.podhub.ui.feature.search.SearchScreen
import com.example.podhub.viewmodels.ArtistViewModel
import com.example.podhub.viewmodels.FavouriteViewModel
import com.example.podhub.viewmodels.PodcastViewModel


object Routes {
    const val INTRO1 = "intro1"
    const val INTRO2 = "intro2"
    const val LOGIN = "login"
    const val HOME = "home"
    const val FAVORITE_ARTIST = "favorite_artist"
    const val FAVORITE_PODCAST = "favorite_podcast"
    const val PODCAST_DETAIL = "podcast_detail"
    const val PLAYER = "player"
    const val SEARCH = "search"
    const val LIBRARY = "library"
    const val ROOM = "room"
}

@Composable
fun AppRouter(navController: NavHostController) {
    val artistViewModel: ArtistViewModel = viewModel(factory = viewModelFactory {
        initializer {

            ArtistViewModel()
        }
    })
    val podCastViewModel: PodcastViewModel = viewModel(factory = viewModelFactory {
        initializer {
            PodcastViewModel()
        }
    })
    val favouriteViewModel: FavouriteViewModel = viewModel(factory = viewModelFactory {
        initializer {
            FavouriteViewModel()
        }
    })

    LaunchedEffect(Unit) {
        podCastViewModel.fetchPodcasts("comedy",10)
        artistViewModel.fetchAllArtists()
    }

    NavHost(navController = navController, startDestination = Routes.INTRO1) {



        composable(Routes.INTRO1) {
            IntroScreen1(navController)
        }

        composable(Routes.INTRO2) {
            IntroScreen2(navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.FAVORITE_ARTIST) {
            ArtistSelectionScreen(navController,artistViewModel,favouriteViewModel)
        }

        composable(Routes.FAVORITE_PODCAST) {
            PodcastCategoryScreen(navController,podCastViewModel,favouriteViewModel)
        }

        composable(Routes.HOME) {
            HomeScreen(navController,artistViewModel,podCastViewModel,favouriteViewModel)
        }

        composable("genre/{genreName}") { backStackEntry ->
            val genreName = Uri.decode(backStackEntry.arguments?.getString("genreName") ?: "")
            GenreDetailScreen(genreName = genreName, navController = navController,podCastViewModel)
        }

        composable(Routes.PODCAST_DETAIL) {
            val podcast = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<PodcastResponseData>("podcast")
            if (podcast != null) {
                PodcastDetailScreen(navController, podcast,podCastViewModel,favouriteViewModel)
            }
        }
        composable(Routes.PLAYER) {
            val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle

            val initialEpisodeIndex = savedStateHandle?.get<Int>("initialEpisodeIndex") ?: 0
            val imageUrl = savedStateHandle?.get<String>("imageUrl") ?: ""
            val artistName = savedStateHandle?.get<String>("artistName") ?: ""

            PlayerScreen(
                navController = navController,
                podcastViewModel = podCastViewModel,
                initialEpisodeIndex = initialEpisodeIndex,
                imageUrl = imageUrl,
                artistName = artistName
            )
        }


        composable(Routes.SEARCH) {
            SearchScreen(navController)
        }
        composable(Routes.LIBRARY) {
            LibraryPodcastsScreen(navController)
        }
//        composable(Routes.ROOM) {
//            RoomListScreen(navController)
//        }

    }
}