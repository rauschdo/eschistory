package de.rauschdo.eschistory.ui.about

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.eschistory.architecture.BaseViewModel
import de.rauschdo.eschistory.ui.dialog.Dialogs
import de.rauschdo.eschistory.ui.main.DialogController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val dialogController: DialogController
) :
    BaseViewModel<AboutContract.Action, AboutContract.Navigation, AboutContract.UiState>() {

    private val _uiState = MutableStateFlow(AboutContract.UiState())
    override val viewState: StateFlow<AboutContract.UiState>
        get() = _uiState

    override fun handleActions(action: AboutContract.Action) {
        when (action) {
            AboutContract.Action.OnButtonClicked -> with(dialogController) {
                requestDialog(
                    Dialogs.sample(onDismissRequest = { this.consumeDialog(viewModelScope) })
                )
            }
        }
    }
}