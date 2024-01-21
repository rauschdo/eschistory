package de.rauschdo.eschistory.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.eschistory.architecture.BaseViewModel
import de.rauschdo.eschistory.data.RealmHelper
import de.rauschdo.eschistory.data.mapToContests
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val realmHelper: RealmHelper
) : BaseViewModel<HomeContract.Action, HomeContract.Navigation, HomeContract.UiState>() {

    private val _uiState = MutableStateFlow(HomeContract.UiState())
    override val viewState: StateFlow<HomeContract.UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            realmHelper.contestFlow.collectLatest {
                with(it.list.toList().mapToContests()) {
                    Timber.i("$this")
                }
            }
        }
    }

    override fun handleActions(action: HomeContract.Action) {
        when (action) {
            else -> Unit
        }
    }
}