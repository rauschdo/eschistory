package de.rauschdo.eschistory.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val purpleMain = Color(0xFF662cb6)
val purpleSecondary = Color(0xFFc0c0c0)
val purpleLight = Color(0xFF8d58d7)

val purpleBgDark = Color(0xFF3f1b72)
val yellowHighlight = Color(0xFFefff00)
val orange = Color(0xFFff7716)

val errorRed = Color(0xFFFF9494)

val screenBackground = Color(0xFFEBEBEB)
val shadowDefault = Color(0xFFC9C9C9)
val disabledGrey = Color(0xFF707070)

val gradientPink = Color(0xFFff47e0)
val gradientOrange = Color(0xFFf89e67)
val gradientYellow = Color(0xFFf4db14)
val gradientGreen = Color(0xFFa4d160)
val gradientBlue = Color(0xFF21c1da)
val gradientPurple = Color(0xFF5849be)

val gradientPinkOrange =
    Brush.horizontalGradient(listOf(gradientPink, gradientOrange, gradientYellow))
val gradientBlueYellow =
    Brush.verticalGradient(listOf(gradientPurple, gradientBlue, gradientGreen, gradientYellow))