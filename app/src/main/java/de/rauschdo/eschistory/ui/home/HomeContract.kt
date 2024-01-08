package de.rauschdo.eschistory.ui.home

import de.rauschdo.eschistory.architecture.NavigationRequest
import de.rauschdo.eschistory.architecture.ViewEvent
import de.rauschdo.eschistory.architecture.ViewState

class HomeContract {

    sealed class Action : ViewEvent

    sealed class Navigation : NavigationRequest

    data class UiState(
        val param1: Any? = null
    ) : ViewState
}