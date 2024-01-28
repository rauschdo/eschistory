package de.rauschdo.eschistory.data

import androidx.compose.runtime.Composable

data class DialogModel(
    val id: String,
    val content: @Composable () -> Unit
)