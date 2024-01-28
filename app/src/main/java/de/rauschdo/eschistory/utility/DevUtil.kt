package de.rauschdo.eschistory.utility

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

fun loremIpsum(words: Int = 2) = LoremIpsum(words).values.joinToString(" ")