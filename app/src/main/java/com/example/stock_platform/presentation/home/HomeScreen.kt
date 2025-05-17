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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.stock_platform.R
import com.example.stock_platform.presentation.Dimens.MediumPadding1
import com.example.stock_platform.presentation.common.EmptyContent
import com.example.stock_platform.presentation.common.SearchBar
import com.example.stock_platform.presentation.common.StockTile
import com.example.stock_platform.presentation.common.StocksGrid
import com.example.stock_platform.presentation.view_all.ViewAllType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToSearch: () -> Unit,
    navigateToDetails: (String) -> Unit,
    navigateToViewAll: (ViewAllType) -> Unit,
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            if (event is HomeViewModel.UIEvent.ShowSnackbar) {
                snackbarHostState.showSnackbar(message = event.message)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(HomeEvent.LoadRecentSearches)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
        ) {
            item {
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
            }
            item {
                SectionHeader(
                    title = "Recently Searched",
                    onViewAllClick = { navigateToViewAll(ViewAllType.RECENT_SEARCHES) }
                )
            }
            item {
                when {
                    state.isRecentSearchesLoading -> Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    state.recentSearches.isEmpty() -> EmptyContent(
                        alphaAnim = 0.3f,
                        message = "No recent searches",
                        R.drawable.ic_search_document
                    )

                    else -> Unit // handled below
                }
            }
            if (state.recentSearches.isNotEmpty()) {
                items(state.recentSearches.take(3)) { stock ->
                    StockTile(
                        stock = stock,
                        onItemClick = { navigateToDetails(stock.symbol) }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                SectionHeader(
                    title = "Top Gainers",
                    onViewAllClick = { navigateToViewAll(ViewAllType.TOP_GAINERS) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                when {
                    state.isGainersLosersLoading -> Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    state.error != null -> EmptyContent(
                        alphaAnim = 0.3f,
                        message = state.error,
                        R.drawable.ic_network_error
                    )

                    else -> Unit
                }
            }
            if (!state.isGainersLosersLoading && state.error == null) {
                item {
                    StocksGrid(
                        stocks = state.topGainers.take(4),
                        modifier = Modifier.padding(horizontal = MediumPadding1),
                        onStockClick = navigateToDetails
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
                item {
                    SectionHeader(
                        title = "Top Losers",
                        onViewAllClick = { navigateToViewAll(ViewAllType.TOP_LOSERS) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StocksGrid(
                        stocks = state.topLosers.take(4),
                        modifier = Modifier.padding(horizontal = MediumPadding1),
                        onStockClick = navigateToDetails
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
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
