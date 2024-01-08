package de.rauschdo.eschistory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.rauschdo.eschistory.ui.about.AboutDestination
import de.rauschdo.eschistory.ui.home.HomeDestination

@Composable
fun MainNavHost(
    modifier: Modifier,
    navigator: AppNav,
) {
    AppNavHost(
        modifier = modifier,
        navController = navigator.navController
    ) {
        composable(AppNavDest.Home) {
            HomeDestination(navigator)
        }
        composable(AppNavDest.AboutAuthor) {
            AboutDestination(navigator)
        }
    }
}