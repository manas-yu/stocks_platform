package com.example.stock_platform.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stock_platform.domain.model.gainers_losers.StockItem

@Composable
fun StocksGrid(
    stocks: List<StockItem>,
    modifier: Modifier = Modifier,
    onStockClick: (StockItem) -> Unit = {}
) {
    val rows = stocks.chunked(2)

    Column(modifier = modifier.fillMaxWidth()) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StockCard(
                    stockItem = rowItems[0],
                    modifier = Modifier.weight(1f),
                    onClick = { onStockClick(rowItems[0]) }
                )
                if (rowItems.size > 1) {
                    StockCard(
                        stockItem = rowItems[1],
                        modifier = Modifier.weight(1f),
                        onClick = { onStockClick(rowItems[1]) }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
