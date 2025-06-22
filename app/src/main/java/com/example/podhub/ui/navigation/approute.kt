package com.example.podhub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.podhub.IntroScreen1
import com.example.podhub.IntroScreen2
import com.example.podhub.ui.feature.artist.ArtistSelectionScreen
import com.example.podhub.ui.feature.login.LoginScreen
import com.example.podhub.ui.feature.home.HomeScreen
import com.example.podhub.ui.feature.podcast.PodcastCategoryScreen
import com.example.podhub.viewmodels.UserViewModel

object Routes {
    const val INTRO1 = "intro1"
    const val INTRO2 = "intro2"
    const val LOGIN = "login"
    const val HOME = "home"
    const val FAVORITE_ARTIST = "favorite_artist"
    const val FAVORITE_PODCAST = "favorite_podcast"
}

@Composable
fun AppRouter(navController: NavHostController,    userViewModel: UserViewModel = viewModel()) {
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
        composable (Routes.FAVORITE_ARTIST){
            ArtistSelectionScreen(navController)
        }
        composable (Routes.FAVORITE_PODCAST){
            PodcastCategoryScreen(navController)
        }
        composable(Routes.HOME) {
            HomeScreen(navController = navController, userViewModel = userViewModel)
        }
    }
}
