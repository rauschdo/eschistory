package de.rauschdo.eschistory.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom bar items, their icons and their respective texts.
 */
sealed class BottomBarItem(
    val label: String,
    val icon: ImageVector,
    val iconActive: ImageVector,
    val dest: AppNavDest
) {
    data object Home : BottomBarItem(
        "Home",
        Icons.Outlined.Home,
        iconActive = Icons.Filled.Home,
        AppNavDest.Home
    )

    data object About : BottomBarItem(
        "About",
        Icons.Outlined.AccountBox,
        iconActive = Icons.Filled.AccountBox,
        AppNavDest.AboutAuthor
    )
}