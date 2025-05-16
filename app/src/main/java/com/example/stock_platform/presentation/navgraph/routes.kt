package com.example.stock_platform.presentation.navgraph

sealed class Route(val route: String) {
    object HomeScreen : Route("home_screen")
    object DetailScreen : Route("detail_screen")
    object SearchScreen : Route("search_screen")
    object AppStartNavigation : Route("app_start_navigation")
}