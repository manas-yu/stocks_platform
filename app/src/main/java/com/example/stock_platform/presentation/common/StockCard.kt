package com.example.stock_platform.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stock_platform.domain.model.gainers_losers.StockItem
import java.util.Locale

@Composable
fun StockCard(
    stockItem: StockItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top row with avatar and ticker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with first letter of ticker
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stockItem.ticker.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Ticker name
                Text(
                    text = stockItem.ticker,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Price",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = stockItem.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Change amount row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Change",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                val changeValue = stockItem.changeAmount
                    .replace("+", "")
                    .replace("-", "").toDoubleOrNull()

                val sign = when {
                    stockItem.changeAmount.trim().startsWith("-") -> "-"
                    else -> "+"
                }

                val formattedChangeAmount = changeValue?.let {
                    "$sign${String.format(Locale.US, "%.2f", it)}"
                } ?: stockItem.changeAmount

                Text(
                    text = formattedChangeAmount,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = determineChangeColor(stockItem.changeAmount)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Change percentage row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Δ %",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                val percentageValue = stockItem.changePercentage
                    .replace("%", "")
                    .toDoubleOrNull()
                val formattedPercentage = percentageValue?.let {
                    String.format(Locale.US, "%.2f%%", it)
                } ?: stockItem.changePercentage

                Text(
                    text = formattedPercentage,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = determineChangeColor(stockItem.changePercentage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 80.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Volume row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Vol",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = formatVolume(stockItem.volume),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun determineChangeColor(changeValue: String): Color {
    val numericValue = changeValue.replace("%", "").toDoubleOrNull() ?: 0.0
    return when {
        numericValue > 0 -> Color(0xFF4CAF50) // Green for positive change
        numericValue < 0 -> Color(0xFFF44336) // Red for negative change
        else -> MaterialTheme.colorScheme.onSurface
    }
}


private fun formatVolume(volume: String): String {
    val volumeNum = volume.toLongOrNull() ?: return volume
    return when {
        volumeNum >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", volumeNum / 1_000_000_000.0)
        volumeNum >= 1_000_000 -> String.format(Locale.US, "%.2fM", volumeNum / 1_000_000.0)
        volumeNum >= 1_000 -> String.format(Locale.US, "%.2fK", volumeNum / 1_000.0)
        else -> volume
    }
}
