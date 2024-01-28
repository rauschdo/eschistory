@file:OptIn(ExperimentalMaterial3Api::class)

package de.rauschdo.eschistory.ui.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BaseHeader(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    shape: Shape = RectangleShape
) {
    Surface(
        color = Color.White,
        shadowElevation = 16.dp,
        shape = shape
    ) {
        TopAppBar(
            modifier = modifier,
            title = title,
            actions = actions,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BaseHeaderPreview() {
    BaseHeader(
        title = {
            Text(text = "Titel")
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = ""
                )
            }
        }
    )
}