package de.rauschdo.eschistory.ui.navigation

import androidx.navigation.NavType

enum class AppNavArgType(val type: NavType<*>) {
    BoolType(NavType.BoolType),
    IntType(NavType.IntType),
    ;
}