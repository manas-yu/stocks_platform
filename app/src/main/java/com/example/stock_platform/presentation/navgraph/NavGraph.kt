package com.example.stock_platform.presentation.navgraph

import androidx.compose.runtime.Composable
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
import com.example.stock_platform.presentation.view_all.ViewAllScreen
import com.example.stock_platform.presentation.view_all.ViewAllViewModel

private const val s = "viewAllType"

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
                   navigateToViewAll = {
                          navigateToViewAll(navController,it.name)
                   },
                )
            }
            composable(route = Route.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                val homeScreenViewModel: HomeViewModel = hiltViewModel()
                SearchScreen(
                    viewModel = viewModel,
                    homeScreenViewModel = homeScreenViewModel,
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
            composable(route = Route.ViewAllScreen.route + "/{viewAllType}") {
                val viewModel: ViewAllViewModel = hiltViewModel()
                ViewAllScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToStockDetail = { symbol ->
                        navigateToDetails(navController,symbol)
                    }
                )
            }
        }
    }

}

private fun navigateToDetails(navController:NavController, symbol: String) {
    navController.navigate(Route.DetailScreen.route + "/$symbol")
}

private fun navigateToViewAll(navController:NavController, viewAllType: String) {
    navController.navigate(Route.ViewAllScreen.route + "/$viewAllType")
}