package com.example.stock_platform.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.stock_platform.presentation.home.HomeScreen
import com.example.stock_platform.presentation.home.HomeViewModel

@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.HomeScreen.route
        ) {
            composable(route = Route.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()
               HomeScreen(
                    viewModel = viewModel,
                    navigateToSearch = {  },
                    navigateToDetails = { _ ->

                    },
                   navigateToViewAll = {  },
                )
            }
        }
    }
}