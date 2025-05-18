package com.example.stock_platform.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.stock_platform.R
import com.example.stock_platform.presentation.Dimens.MediumPadding1
import com.example.stock_platform.presentation.common.EmptyContent
import com.example.stock_platform.presentation.common.SearchBar
import com.example.stock_platform.presentation.common.StockTile
import com.example.stock_platform.presentation.home.HomeEvent
import com.example.stock_platform.presentation.home.HomeViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    homeScreenViewModel: HomeViewModel,
    navigateToDetails: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val state = viewModel.state.value
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column  {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    tint= MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MediumPadding1),
                readOnly = false,
                onValueChange = {
                    viewModel.onEvent(SearchEvent.UpdateSearchQuery(it))
                },
                text = state.searchQuery,
                onSearch = {
                    focusManager.clearFocus()
                    viewModel.onEvent(SearchEvent.SearchStocks)
                }
            )
        }


        Spacer(modifier = Modifier.height(MediumPadding1))

        // Show loading state
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Show error message if any
        state.error?.let { errorMessage ->
            EmptyContent(
                alphaAnim = 0.3f,
                message = errorMessage,
                iconId = R.drawable.ic_network_error
            )
        }

        // Show search results
        state.stocks?.let { stocks ->
            if (stocks.isEmpty() && !state.isLoading && state.searchQuery.isNotEmpty()) {
                EmptyContent(
                    alphaAnim = 0.3f,
                    message = "No results found",
                    iconId = R.drawable.ic_search_document
                )
            } else {
                LazyColumn {
                    items(stocks) { stock ->
                        StockTile(
                            stock = stock,
                            onItemClick = {
                                homeScreenViewModel.onEvent(
                                    HomeEvent.SaveRecentSearch(stock)
                                )
                                navigateToDetails(stock.symbol)
                            }
                        )
                    }
                }
            }
        }
    }
}

