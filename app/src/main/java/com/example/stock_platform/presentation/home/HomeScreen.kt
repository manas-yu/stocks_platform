package com.example.stock_platform.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.stock_platform.domain.model.gainers_losers.StockItem
import com.example.stock_platform.presentation.Dimens.MediumPadding1
import com.example.stock_platform.presentation.common.SearchBar
import com.example.stock_platform.presentation.common.StocksGrid
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToSearch: () -> Unit,
    navigateToDetails: (StockItem) -> Unit,
    navigateToViewAll: () -> Unit,
) {
    val state = viewModel.state.value
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle UI events
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = MediumPadding1)
                    .statusBarsPadding()
            ) {
                // Search Bar
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MediumPadding1),
                    readOnly = true,
                    onClick = navigateToSearch,
                    onValueChange = {},
                    onSearch = {},
                    text = ""
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Recently Searched Section
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(
                        title = "Recently Searched",
                        onViewAllClick = navigateToViewAll
                    )

                    Spacer(modifier = Modifier.height(8.dp))

//                    StocksGrid(
//                        stocks = state.recentlySearched.take(4),
//                        modifier = Modifier.padding(horizontal = MediumPadding1),
//                        onStockClick = navigateToDetails
//                    )
                }
                // Top Gainers Section with fixed height
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(
                        title = "Top Gainers",
                        onViewAllClick = navigateToViewAll
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StocksGrid(
                        stocks = state.topGainers.take(4),
                        modifier = Modifier.padding(horizontal = MediumPadding1),
                        onStockClick = navigateToDetails
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Top Losers Section with fixed height
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(
                        title = "Top Losers",
                        onViewAllClick = navigateToViewAll
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StocksGrid(
                        stocks = state.topLosers.take(4),
                        modifier = Modifier.padding(horizontal = MediumPadding1),
                        onStockClick = navigateToDetails
                    )
                }

                // Add extra space at the bottom for better scrolling
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Loading Indicator
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onViewAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MediumPadding1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = onViewAllClick,
            modifier = Modifier
        ) {
            Text(
                text = "View All",
                style = MaterialTheme.typography
                    .labelLarge.copy(textDecoration = TextDecoration.Underline)
            )
        }
    }
}
