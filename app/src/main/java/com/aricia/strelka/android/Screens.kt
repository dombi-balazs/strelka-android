package com.aricia.strelka.android

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screens(val route: String) {
    Authentication("Authentication"),
    MainScreen("MainScreen")
}

@Composable
fun StrelkaNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.Authentication.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier) {
        composable(Screens.Authentication.route) {
            AuthPage(
                navController = navController,
                onAuthSuccess = {
                    navController.navigate(Screens.MainScreen.route) {
                        popUpTo(Screens.Authentication.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screens.MainScreen.route) {
            MainPage(navController = navController,
                onLogOutSuccess = {
                    navController.navigate(Screens.Authentication.route) {
                        popUpTo(Screens.MainScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
