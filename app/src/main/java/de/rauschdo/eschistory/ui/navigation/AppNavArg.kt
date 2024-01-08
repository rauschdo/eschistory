package de.rauschdo.eschistory.ui.navigation

import android.os.Bundle

enum class AppNavArg {

    /**
     * Define NavigationArguments here
     */
    Arg1;

    fun key(): String = when (this) {
        else -> ""
    }

    fun destinationPlaceholder() = "{${key()}}"

    fun type(): AppNavArgType = when (this) {
        else -> AppNavArgType.BoolType
    }

    fun defaultValue(): Any = when (this) {
        else -> false
    }

    fun value(bundle: Bundle?): Any = if (bundle?.containsKey(this.key()) == true) {
        when (this.type()) {
            AppNavArgType.BoolType -> bundle.getBoolean(this.key())
            AppNavArgType.IntType -> bundle.getInt(this.key())
        }
    } else {
        this.defaultValue()
    }
}