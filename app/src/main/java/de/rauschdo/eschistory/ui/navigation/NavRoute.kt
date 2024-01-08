package de.rauschdo.eschistory.ui.navigation

import androidx.annotation.Discouraged


class NavRoute(val dest: AppNavDest, vararg args: Any) {
    val route: String = dest.destinationTarget(args = args)
}

@Discouraged("Dedicated proxy navigation functions should be used where possible. See in AppNavAccessor.kt")
fun AppNavDest.navRoute() = NavRoute(this)