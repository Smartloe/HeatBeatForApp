package com.tunehub.app.ui.navigation

sealed class AppRoute(val route: String) {
    data object Home : AppRoute("home")
    data object Search : AppRoute("search")
    data object Player : AppRoute("player")
    data object Playlist : AppRoute("playlist")
    data object Profile : AppRoute("profile")
    data object Settings : AppRoute("settings")

    companion object {
        val bottomRoutes = listOf(
            Home.route,
            Search.route,
            Player.route,
            Playlist.route,
            Profile.route,
            Settings.route,
        )
    }
}
