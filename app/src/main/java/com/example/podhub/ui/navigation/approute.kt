package com.example.podhub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.podhub.IntroScreen1
import com.example.podhub.IntroScreen2
import com.example.podhub.ui.feature.login.LoginScreen
import com.example.podhub.ui.feature.home.HomeScreen

object Routes {
    const val INTRO1 = "intro1"
    const val INTRO2 = "intro2"
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun AppRouter(navController: NavHostController) {
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
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
    }
}
