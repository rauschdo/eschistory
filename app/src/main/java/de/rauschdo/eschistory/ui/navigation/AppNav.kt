package de.rauschdo.eschistory.ui.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState

@Stable
class AppNav {

    lateinit var navController: NavHostController

    val currentArgs: Bundle?
        get() = navController.currentBackStackEntry?.arguments

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentRoute: AppNavDest
        @Composable get() = currentDestination?.route?.let { destinationId ->
            AppNavDest.values().firstOrNull { it.destinationId() == destinationId }
        } ?: START_DESTINATION

    fun navigateRoot(
        dest: AppNavDest, vararg args: Any
    ) = navigateRoot(navRoute = NavRoute(dest, *args))

    @SuppressLint("RestrictedApi")
    fun navigateRoot(
        navRoute: NavRoute
    ) {
        navController.navigate(route = navRoute.route) {
            navController.currentBackStack.value.firstOrNull()?.let { topRoute ->
                popUpTo(topRoute.destination.id) {
                    saveState = false
                    inclusive = true
                }
            }
            launchSingleTop = true
            restoreState = false
        }
    }

    fun navigate(
        dest: AppNavDest, vararg args: Any
    ) = navigate(navRoute = NavRoute(dest, *args))

    @SuppressLint("RestrictedApi")
    fun navigate(
        navRoute: NavRoute,
    ) {
        navController.currentBackStack.value.firstOrNull { it.destination.route == navRoute.route }
            ?.let {
                popUpTo(
                    navRoute = navRoute,
                    popTo = navRoute.dest,
                    inclusive = true,
                )
            } ?: navController.navigate(route = navRoute.route)
    }

    fun popUpTo(
        dest: AppNavDest,
        popTo: AppNavDest = START_DESTINATION,
        inclusive: Boolean = true,
        vararg args: Any
    ) = popUpTo(navRoute = NavRoute(dest, *args), popTo = popTo, inclusive = inclusive)

    fun popUpTo(
        navRoute: NavRoute,
        popTo: AppNavDest = START_DESTINATION,
        inclusive: Boolean = true
    ) = navController.navigate(
        route = navRoute.route,
        navOptions = NavOptions.Builder().setPopUpTo(
            route = popTo.destinationId(),
            inclusive = inclusive
        ).build()
    )

    fun onBackClick() = navController.popBackStack()

}