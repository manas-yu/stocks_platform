package com.example.stock_platform.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.stock_platform.presentation.details.DetailsScreen
import com.example.stock_platform.presentation.details.DetailsViewModel
import com.example.stock_platform.presentation.home.HomeScreen
import com.example.stock_platform.presentation.home.HomeViewModel
import com.example.stock_platform.presentation.search.SearchScreen
import com.example.stock_platform.presentation.search.SearchViewModel

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
                    navigateToSearch = {
                        navController.navigate(Route.SearchScreen.route)
                    },
                    navigateToDetails = {
                        navigateToDetails(navController,it)
                    },
                   navigateToViewAll = {  },
                )
            }
            composable(route = Route.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(
                    viewModel = viewModel,
                    navigateToDetails = { symbol ->
                        navigateToDetails(navController,symbol)
                    },
                    navigateBack = {
                        navController.popBackStack()
                    },
                )
            }
            composable(route = Route.DetailScreen.route + "/{symbol}") {
                val viewModel: DetailsViewModel = hiltViewModel()
                DetailsScreen(
                    viewModel = viewModel,
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}

private fun navigateToDetails(navController:NavController, symbol: String) {
    navController.navigate(Route.DetailScreen.route + "/$symbol")
}