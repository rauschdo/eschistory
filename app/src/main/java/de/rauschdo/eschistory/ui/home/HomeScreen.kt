package de.rauschdo.eschistory.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rauschdo.eschistory.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.eschistory.ui.navigation.AppNav
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun HomeDestination(
    navigator: AppNav,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state: HomeContract.UiState by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(LAUNCH_EVENTS_FOR_NAVIGATION) {
        viewModel.navigator.onEach { navigationRequest ->
            when (navigationRequest) {
                else -> Unit
            }
        }.collect()
    }
    HomeScreen(
        state = state,
        forwardAction = { action -> viewModel.setAction(action) }
    )
}

@Composable
private fun HomeScreen(
    state: HomeContract.UiState,
    forwardAction: (HomeContract.Action) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
    ) {
        items(List(5) { it }) {
            ContestRow(modifier = Modifier)
        }
    }
}

@Composable
private fun ContestRow(
    modifier: Modifier
) {
    Row {
        Text(text = "TODO")
    }
}

@Composable
private fun ScreenPreview() {
    HomeScreen(
        state = HomeContract.UiState(),
        forwardAction = {}
    )
}
