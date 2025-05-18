package com.example.stock_platform.presentation.view_all

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.stock_platform.R
import com.example.stock_platform.domain.model.gainers_losers.StockItem
import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.presentation.common.EmptyContent
import com.example.stock_platform.presentation.common.StockTile
import com.example.stock_platform.presentation.common.StocksGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(
    viewModel: ViewAllViewModel,
    onNavigateToStockDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (state.viewAllType) {
                            ViewAllType.RECENT_SEARCHES -> "Recent Searches"
                            ViewAllType.TOP_GAINERS -> "Top Gainers"
                            ViewAllType.TOP_LOSERS -> "Top Losers"
                            null -> ""
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(ViewAllEvent.NavigateBack)
                        onNavigateBack()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_arrow),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error.isNotEmpty()) {
                EmptyContent(
                    alphaAnim = 0.3f,
                    message = state.error,
                    iconId = R.drawable.ic_network_error
                )
            } else {
                when (state.viewAllType) {
                    ViewAllType.RECENT_SEARCHES -> {
                        RecentSearchesList(
                            recentSearches = state.recentSearches,
                            displayCount = state.displayItemCount,
                            onLoadMore = { viewModel.onEvent(ViewAllEvent.LoadMore) },
                            onStockClick = { symbol ->
                                viewModel.onEvent(ViewAllEvent.StockClicked(symbol))
                                onNavigateToStockDetail(symbol)
                            }
                        )
                    }
                    ViewAllType.TOP_GAINERS -> {
                        StockItemsView(
                            stockItems = state.topGainers,
                            displayCount = state.displayItemCount,
                            onLoadMore = { viewModel.onEvent(ViewAllEvent.LoadMore) },
                            onStockClick = { ticker ->
                                viewModel.onEvent(ViewAllEvent.StockClicked(ticker))
                                onNavigateToStockDetail(ticker)
                            }
                        )
                    }
                    ViewAllType.TOP_LOSERS -> {
                        StockItemsView(
                            stockItems = state.topLosers,
                            displayCount = state.displayItemCount,
                            onLoadMore = { viewModel.onEvent(ViewAllEvent.LoadMore) },
                            onStockClick = { ticker ->
                                viewModel.onEvent(ViewAllEvent.StockClicked(ticker))
                                onNavigateToStockDetail(ticker)
                            }
                        )
                    }
                    null -> {
                        // Handle null case
                    }
                }
            }
        }
    }
}

@Composable
fun RecentSearchesList(
    recentSearches: List<BestMatch>,
    displayCount: Int,
    onLoadMore: () -> Unit,
    onStockClick: (String) -> Unit
) {
    val displayedItems = recentSearches.take(displayCount)
    val hasMoreItems = recentSearches.size > displayCount

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(displayedItems) { stock ->
            StockTile(
                stock = stock,
                onItemClick = { onStockClick(stock.symbol) }
            )
        }

        if (hasMoreItems) {
            item {
                LoadMoreButton(onLoadMore = onLoadMore)
            }
        }
    }
}

@Composable
fun StockItemsView(
    stockItems: List<StockItem>,
    displayCount: Int,
    onLoadMore: () -> Unit,
    onStockClick: (String) -> Unit
) {
    val displayedItems = stockItems.take(displayCount)
    val hasMoreItems = stockItems.size > displayCount

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            StocksGrid(
                stocks = displayedItems,
                onStockClick = onStockClick
            )
        }

        if (hasMoreItems) {
            item {
                LoadMoreButton(onLoadMore = onLoadMore)
            }
        }
    }
}

@Composable
fun LoadMoreButton(onLoadMore: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onLoadMore,
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Load More")
        }
    }
}