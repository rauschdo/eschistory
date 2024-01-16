package de.rauschdo.eschistory.ui.home

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.eschistory.ContestItemProto
import de.rauschdo.eschistory.ContestsProto
import de.rauschdo.eschistory.architecture.BaseViewModel
import de.rauschdo.eschistory.utility.ContextProvider
import de.rauschdo.eschistory.utility.proto.ContestItemsRepository
import de.rauschdo.eschistory.utility.proto.ContestItemsSerializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val DATA_STORE_FILE_NAME = "contest.proto"

private val Context.todoItemsStore: DataStore<ContestsProto> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = ContestItemsSerializer,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contextProvider: ContextProvider
) : BaseViewModel<HomeContract.Action, HomeContract.Navigation, HomeContract.UiState>() {

    private val contestItemsRepository: ContestItemsRepository =
        ContestItemsRepository(contextProvider.context.todoItemsStore)

    private val _uiState = MutableStateFlow(HomeContract.UiState())
    override val viewState: StateFlow<HomeContract.UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
//            getContestItems()
            contestItemsRepository.contestListFlow.collect {
                Timber.d("$it")
            }
        }
    }

    private suspend fun getContestItems(): List<ContestItemProto> =
        contestItemsRepository.fetchCachedContestList().itemsList

    override fun handleActions(action: HomeContract.Action) {
        when (action) {
            else -> Unit
        }
    }
}