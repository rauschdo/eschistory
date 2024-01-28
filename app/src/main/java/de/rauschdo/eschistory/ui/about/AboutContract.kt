package de.rauschdo.eschistory.ui.about

import de.rauschdo.eschistory.architecture.NavigationRequest
import de.rauschdo.eschistory.architecture.ViewEvent
import de.rauschdo.eschistory.architecture.ViewState

class AboutContract {

    sealed class Action : ViewEvent {
        data object OnButtonClicked : Action()
    }

    sealed class Navigation : NavigationRequest

    data class UiState(
        val param1: Any? = null
    ) : ViewState
}