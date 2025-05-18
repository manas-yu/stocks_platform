package com.example.stockapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import java.text.DecimalFormat

@Composable
fun StockPriceChart(
    modifier: Modifier = Modifier,
    currentPrice: Float = 178.72f,
    changePercentage: Float = 1.24f
) {
    // Generate dummy data for a trading day (9:30 AM to 4:00 PM with 30-minute intervals)
    val stockPriceData = generateDummyStockPriceData(basePrice = currentPrice)

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Line chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                val steps = 5
                val xAxisData = AxisData.Builder()
                    .axisStepSize(100.dp)
                    .backgroundColor(Color.Transparent)
                    .steps(stockPriceData.size - 1)
                    .labelData { i ->
                        // Convert index to time label (9:30 AM to 4:00 PM)
                        val hour = 9 + (i * 30) / 60
                        val minute = (i * 30) % 60
                        val amPm = if (hour >= 12) "PM" else "AM"
                        val displayHour = if (hour > 12) hour - 12 else hour

                        // Only show labels at 1-hour intervals for better readability
                        if (minute == 0 || minute == 30 && i % 2 == 0) {
                            "$displayHour:${minute.toString().padStart(2, '0')} $amPm"
                        } else {
                            ""
                        }
                    }
                    .labelAndAxisLinePadding(15.dp)
                    .axisLineColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .axisLabelColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .build()

                // Calculate min and max prices to set y-axis range
                val minPrice = stockPriceData.minByOrNull { it.y }?.y ?: 0f
                val maxPrice = stockPriceData.maxByOrNull { it.y }?.y ?: 0f
                val priceRange = maxPrice - minPrice
                val paddedMin = minPrice - (priceRange * 0.1f)
                val paddedMax = maxPrice + (priceRange * 0.1f)

                val yAxisData = AxisData.Builder()
                    .steps(steps)
                    .backgroundColor(Color.Transparent)
                    .labelData { i ->
                        val price = paddedMin + (paddedMax - paddedMin) * i / steps
                        DecimalFormat("#,##0.00").format(price)
                    }
                    .labelAndAxisLinePadding(20.dp)
                    .axisLineColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .axisLabelColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .build()

                // Determine chart color based on price trend
                val isPriceUp = changePercentage >= 0
                val chartColor = if (isPriceUp) Color(0xFF4CAF50) else Color(0xFFE53935)
                val transparentColor = chartColor.copy(alpha = 0f)

                val lineChartData = LineChartData(
                    linePlotData = LinePlotData(
                        lines = listOf(
                            Line(
                                dataPoints = stockPriceData,
                                LineStyle(
                                    color = chartColor,
                                    lineType = LineType.SmoothCurve(isDotted = false),
                                    width = 3f
                                ),
                                IntersectionPoint(
                                    color = chartColor,
                                    radius = 4.dp
                                ),
                                SelectionHighlightPoint(
                                    color = chartColor,
                                    radius = 4.dp
                                ),
                                ShadowUnderLine(
                                    alpha = 0.3f,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            chartColor.copy(alpha = 0.3f),
                                            transparentColor
                                        )
                                    )
                                ),
                                SelectionHighlightPopUp(
                                    backgroundColor = MaterialTheme.colorScheme.surface,
                                    popUpLabel = { x, y ->
                                        val hour = 9 + (x.toInt() * 30) / 60
                                        val minute = (x.toInt() * 30) % 60
                                        val amPm = if (hour >= 12) "PM" else "AM"
                                        val displayHour = if (hour > 12) hour - 12 else hour
                                        "$${DecimalFormat("#,##0.00").format(y)}\n$displayHour:${minute.toString().padStart(2, '0')} $amPm"
                                    },
                                )
                            )
                        )
                    ),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    xAxisData = xAxisData,
                    yAxisData = yAxisData,
                    gridLines = GridLines(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        enableHorizontalLines = true,
                        enableVerticalLines = false
                    )
                )

                LineChart(
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    lineChartData = lineChartData
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Time range indicator
            Text(
                text = "Today, 9:30 AM - 4:00 PM",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Generates dummy stock price data for a trading day with 30-minute intervals
 * Trading hours: 9:30 AM - 4:00 PM (13 data points)
 */
private fun generateDummyStockPriceData(basePrice: Float): List<Point> {
    // Create realistic price fluctuations around the base price
    val volatility = basePrice * 0.02f // 2% volatility
    val dataPoints = mutableListOf<Point>()

    // Trading hours: 9:30 AM - 4:00 PM with 30-minute intervals
    val intervals = 14 // 13 30-minute intervals in 6.5 hours (9:30 AM to 4:00 PM)

    var currentPrice = basePrice
    for (i in 0 until intervals) {
        // Random walk with slight mean reversion
        val randomChange = (Math.random() * volatility * 2 - volatility).toFloat()
        val meanReversion = (basePrice - currentPrice) * 0.1f
        currentPrice += randomChange + meanReversion

        dataPoints.add(Point(i.toFloat(), currentPrice))
    }

    return dataPoints
}
