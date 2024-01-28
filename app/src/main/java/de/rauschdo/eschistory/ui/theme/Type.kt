package de.rauschdo.eschistory.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import de.rauschdo.eschistory.R

val fontAmphora = FontFamily(Font(R.font.amphora))

val fontEuroType = FontFamily(Font(R.font.eurotype2016))

val TypeDefinition = Typography(
    displayLarge = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_24),
    displayMedium = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_20),
    displaySmall = defineTextStyle(fontAmphora, FontWeight.Normal, textsize_14),
    headlineLarge = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_24),
    headlineMedium = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_20),
    headlineSmall = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_18),
    titleLarge = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_20),
    titleMedium = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_18),
    titleSmall = defineTextStyle(fontAmphora, FontWeight.Bold, textsize_18),
    bodyLarge = defineTextStyle(fontAmphora, FontWeight.Normal, textsize_18),
    bodyMedium = defineTextStyle(fontAmphora, FontWeight.Normal, textsize_16),
    bodySmall = defineTextStyle(fontAmphora, FontWeight.Normal, textsize_14),
    labelLarge = defineTextStyle(fontAmphora, FontWeight.Normal, textsize_18),
    labelMedium = defineTextStyle(fontAmphora, FontWeight.Normal, textsize_16),
    labelSmall = defineTextStyle(fontAmphora, FontWeight.Normal, textsize_12),
)

val euroType = defineTextStyle(fontEuroType, weight = FontWeight.Normal, textSize = textsize_18)

private fun defineTextStyle(
    fontFamily: FontFamily,
    weight: FontWeight,
    textSize: TextUnit,
    color: Color = Color.Unspecified,
    textDirection: TextDecoration? = null,
) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = weight,
    fontSize = textSize,
    color = color,
    textDecoration = textDirection
)