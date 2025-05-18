package com.example.stock_platform.presentation.details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.stock_platform.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    onBackClick: () -> Unit,
    symbol: String
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                text = symbol,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            actionIconContentColor = colorResource(
                id = R.color.body
            ),
            containerColor = Color.Transparent,
            navigationIconContentColor = colorResource(
                id = R.color.body
            ),
        )

    )
}