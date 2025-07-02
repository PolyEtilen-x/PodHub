package com.example.podhub.ui.navigation

//import com.example.podhub.ui.feature.room.RoomListScreen
import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.podhub.IntroScreen1
import com.example.podhub.IntroScreen2
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.ui.feature.artist.ArtistSelectionScreen
import com.example.podhub.ui.feature.home.GenreDetailScreen
import com.example.podhub.ui.feature.home.HomeScreen
import com.example.podhub.ui.feature.home.PlayerScreen
import com.example.podhub.ui.feature.home.PodcastDetailScreen
import com.example.podhub.ui.feature.home.PodcasterScreen
import com.example.podhub.ui.feature.library.LibraryScreen
import com.example.podhub.ui.feature.login.LoginScreen
import com.example.podhub.ui.feature.podcast.PodcastCategoryScreen
import com.example.podhub.ui.feature.search.SearchScreen
import com.example.podhub.viewmodels.ArtistViewModel
import com.example.podhub.viewmodels.FavouriteViewModel
import com.example.podhub.viewmodels.PodcastViewModel
import androidx.compose.runtime.getValue
import com.example.podhub.ui.feature.library.PlaylistDetailScreen
import com.example.podhub.viewmodels.HistoryViewModel
import com.example.podhub.viewmodels.ScriptViewModel
import com.example.podhub.viewmodels.SharedPlaylistViewModel


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
    const val PODCASTERS = "podcasters"
}

@Composable
fun AppRouter(navController: NavHostController) {
    val sharedPlaylistViewModel: SharedPlaylistViewModel = viewModel()
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
    val historyViewModel : HistoryViewModel = viewModel (factory = viewModelFactory {
        initializer {
            HistoryViewModel()
        }
    })
    val scriptViewModel : ScriptViewModel = viewModel (factory = viewModelFactory {
        initializer {
            ScriptViewModel()
        }
    })

    LaunchedEffect(Unit) {
        podCastViewModel.fetchPodcasts("comedy",10)
        artistViewModel.fetchAllArtists()
        scriptViewModel.fetchScriptByPodcastId(1222114325)
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
            HomeScreen(navController,artistViewModel,podCastViewModel,favouriteViewModel,historyViewModel)
        }

        composable("genre/{genreName}") { backStackEntry ->
            val genreName = Uri.decode(backStackEntry.arguments?.getString("genreName") ?: "")
            GenreDetailScreen(genreName = genreName, navController = navController,podCastViewModel)
        }
        composable(Routes.PODCASTERS) {
            val artists by artistViewModel.artists.collectAsState()
            PodcasterScreen(navController = navController, artists = artists)
        }

        composable(Routes.PODCAST_DETAIL) {
            val podcast = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<PodcastResponseData>("podcast")

            val isHistory = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<Boolean>("isHistory") ?: false // Default to false if not found

            if (podcast != null) {
                PodcastDetailScreen(
                    navController = navController,
                    podcast = podcast,
                    podCastViewModel,
                    favouriteViewModel = favouriteViewModel,
                    isHistory = isHistory,
                    historyViewModel
                )
            }
        }
        composable(Routes.PLAYER) {
            val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle


            val recentPlay = savedStateHandle?.get<Int>("recentPlay")
            val initialEpisodeIndex = savedStateHandle?.get<Int>("initialEpisodeIndex") ?: 0
            val imageUrl = savedStateHandle?.get<String>("imageUrl") ?: ""
            val artistName = savedStateHandle?.get<String>("artistName") ?: ""
            val podCastId = savedStateHandle?.get<Long>("podCastId") ?: ""

            if (recentPlay != null) {
                PlayerScreen(
                    navController = navController,
                    podcastViewModel = podCastViewModel,
                    historyViewModel = historyViewModel,
                    scriptViewModel,
                    initialEpisodeIndex = initialEpisodeIndex,

                    recentPlay = recentPlay,
                    imageUrl = imageUrl,
                    artistName = artistName,
                    podCastId = podCastId
                )
            }
        }


        composable(Routes.SEARCH) {
            SearchScreen(navController,podCastViewModel)
        }
        composable(Routes.LIBRARY) {
            LibraryScreen(navController,podCastViewModel,artistViewModel,favouriteViewModel)
        }

        composable("playlist_detail") {
            sharedPlaylistViewModel.selectedPlaylist?.let {
                PlaylistDetailScreen(playlist = it)
            } ?: Text("Không tìm thấy Playlist")
        }



//        composable(Routes.ROOM) {
//            RoomListScreen(navController)
//        }

    }
}