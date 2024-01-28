package de.rauschdo.eschistory.ui.about

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rauschdo.eschistory.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.eschistory.ui.main.BaseButton
import de.rauschdo.eschistory.ui.navigation.AppNav
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun AboutDestination(
    navigator: AppNav,
    viewModel: AboutViewModel = hiltViewModel()
) {
    val state: AboutContract.UiState by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(LAUNCH_EVENTS_FOR_NAVIGATION) {
        viewModel.navigator.onEach { navigationRequest ->
            when (navigationRequest) {
                else -> Unit
            }
        }.collect()
    }
    AboutScreen(
        state = state,
        forwardAction = { action -> viewModel.setAction(action) }
    )
}

@Composable
private fun AboutScreen(
    state: AboutContract.UiState,
    forwardAction: (AboutContract.Action) -> Unit
) {
    val context = LocalContext.current
    Text(text = "About")
    BaseButton(
        modifier = Modifier.padding(top = 16.dp),
        text = "Dialog",
        onClick = { forwardAction(AboutContract.Action.OnButtonClicked) }
    )
}

@Composable
private fun ScreenPreview() {
    AboutScreen(
        state = AboutContract.UiState(),
        forwardAction = {}
    )
}
