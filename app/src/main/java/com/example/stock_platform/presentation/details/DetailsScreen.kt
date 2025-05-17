package com.example.stock_platform.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stock_platform.R
import com.example.stock_platform.presentation.common.EmptyContent
import com.example.stock_platform.presentation.details.components.DetailsTopBar
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun DetailsScreen(
    navigateBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = SnackbarHostState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is DetailsViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = scaffoldState) },
        topBar = {
            DetailsTopBar(
                onBackClick = navigateBack,
                symbol = state.stockSymbol
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            state.stockDetails?.let { details ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Company Name and Symbol
                    Text(
                        text = details.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "(${details.symbol}) - ${details.exchange}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Company Description Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            SectionTitle(title = "About")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = details.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Key Statistics
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            SectionTitle(title = "Key Statistics")

                            Spacer(modifier = Modifier.height(8.dp))

                            // Market Data
                            Row(modifier = Modifier.fillMaxWidth()) {
                                InfoItem(
                                    title = "Market Cap",
                                    value = "$${formatLargeNumber(details.marketCapitalization.toLongOrNull() ?: 0)}",
                                    modifier = Modifier.weight(1f)
                                )
                                InfoItem(
                                    title = "P/E Ratio",
                                    value = details.peRatio,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                InfoItem(
                                    title = "EPS",
                                    value = "$${details.eps}",
                                    modifier = Modifier.weight(1f)
                                )
                                InfoItem(
                                    title = "Dividend Yield",
                                    String.format(
                                        Locale.US, "%.2f%%",
                                        (details.dividendYield.toDoubleOrNull() ?: 0.0) * 100
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                InfoItem(
                                    title = "52-Week High",
                                    value = "$${details.week52High}",
                                    modifier = Modifier.weight(1f)
                                )
                                InfoItem(
                                    title = "52-Week Low",
                                    value = "$${details.week52Low}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Company Information
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            SectionTitle(title = "Company Information")

                            Spacer(modifier = Modifier.height(8.dp))

                            InfoItem(title = "Sector", value = details.sector)
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoItem(title = "Industry", value = details.industry)
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoItem(title = "Country", value = details.country)
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoItem(title = "Address", value = details.address)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Performance Metrics
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            SectionTitle(title = "Performance Metrics")

                            Spacer(modifier = Modifier.height(8.dp))

                            InfoItem(
                                title = "Revenue (TTM)",
                                value = (details.revenueTTM.toLongOrNull() ?: 0).toString()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoItem(
                                title = "EBITDA",
                                value = (details.ebitda.toLongOrNull() ?: 0).toString()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoItem(
                                title = "Profit Margin",
                                (details.profitMargin.toDoubleOrNull() ?: 0.0).toString()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoItem(
                                title = "Operating Margin",
                                value = (details.operatingMarginTTM.toDoubleOrNull()
                                    ?: 0.0).toString()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoItem(
                                title = "Return on Equity",
                                value = (details.returnOnEquityTTM.toDoubleOrNull()
                                    ?: 0.0).toString()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (state.error != null && !state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    EmptyContent(
                        alphaAnim = 0.3f,
                        message = state.error,
                        iconId = R.drawable.ic_network_error
                    )
                }
            }
        }
    }
}

fun formatLargeNumber(number: Long): String {
    return when {
        number >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", number / 1_000_000_000.0)
        number >= 1_000_000 -> String.format(Locale.US, "%.2fM", number / 1_000_000.0)
        number >= 1_000 -> String.format(Locale.US, "%.2fK", number / 1_000.0)
        else -> number.toString()
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun InfoItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}