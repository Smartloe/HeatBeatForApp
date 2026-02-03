package com.tunehub.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tunehub.app.ui.screens.HomeScreen
import com.tunehub.app.ui.screens.PlayerScreen
import com.tunehub.app.ui.screens.PlaylistScreen
import com.tunehub.app.ui.screens.ProfileScreen
import com.tunehub.app.ui.screens.SearchScreen
import com.tunehub.app.ui.screens.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = AppRoute.Home.route,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(AppRoute.Home.route) { HomeScreen() }
        composable(AppRoute.Search.route) { SearchScreen() }
        composable(AppRoute.Player.route) { PlayerScreen() }
        composable(AppRoute.Playlist.route) { PlaylistScreen() }
        composable(AppRoute.Profile.route) { ProfileScreen() }
        composable(AppRoute.Settings.route) { SettingsScreen() }
    }
}
