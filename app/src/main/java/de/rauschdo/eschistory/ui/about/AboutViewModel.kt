package de.rauschdo.eschistory.ui.about

import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.eschistory.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor() :
    BaseViewModel<AboutContract.Action, AboutContract.Navigation, AboutContract.UiState>() {

    private val _uiState = MutableStateFlow(AboutContract.UiState())
    override val viewState: StateFlow<AboutContract.UiState>
        get() = _uiState

    override fun handleActions(action: AboutContract.Action) {
        when (action) {
            else -> Unit
        }
    }
}