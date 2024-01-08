package de.rauschdo.eschistory.ui.home

import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.eschistory.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() :
    BaseViewModel<HomeContract.Action, HomeContract.Navigation, HomeContract.UiState>() {

    private val _uiState = MutableStateFlow(HomeContract.UiState())
    override val viewState: StateFlow<HomeContract.UiState>
        get() = _uiState

    override fun handleActions(action: HomeContract.Action) {
        when (action) {
            else -> Unit
        }
    }
}