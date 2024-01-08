package de.rauschdo.eschistory.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument


enum class AppNavDest {

    Home,
    Details,
    AboutAuthor
    ;

    fun destinationTarget(vararg args: Any): String = when (this) {
        Home -> "home"
        Details -> "details"
        AboutAuthor -> "about"
    }

    fun destinationId(): String =
        destinationTarget(*arguments().map { it.destinationPlaceholder() }.toTypedArray())

    fun arguments(): List<AppNavArg> = when (this) {
        Home -> emptyList()
        Details -> emptyList()
        AboutAuthor -> emptyList()
    }

    fun namedNavArguments(): List<NamedNavArgument> = arguments().map { navArg ->
        navArgument(
            navArg.key()
        ) {
            type = navArg.type().type
            defaultValue = navArg.defaultValue()
            nullable = type.isNullableAllowed && defaultValue == null
        }
    }
}