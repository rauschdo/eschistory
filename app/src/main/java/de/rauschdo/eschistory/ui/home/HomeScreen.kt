@file:OptIn(ExperimentalMaterial3Api::class)

package de.rauschdo.eschistory.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.rauschdo.eschistory.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.eschistory.data.ContestsList
import de.rauschdo.eschistory.ui.main.BaseText
import de.rauschdo.eschistory.ui.navigation.AppNav
import de.rauschdo.eschistory.ui.theme.TypeDefinition
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
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        items(
            items = state.contests,
            key = { it.year }
        ) { contest ->
            ContestListItem(
                contest = contest,
                onClick = {
                    // TODO
                }
            )
        }
    }
}

@Composable
fun ContestListItem(contest: ContestsList.Contest, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp),
        onClick = onClick
    ) {
        with(contest) {
            val resourceId = context.resources.getIdentifier(
                "esc_logo_$year",
                "drawable",
                context.packageName
            )
            Column(Modifier.fillMaxWidth()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(resourceId)
                        .build(),
                    contentScale = ContentScale.Inside,
                    placeholder = rememberVectorPainter(image = Icons.Default.Close),
                    contentDescription = "",
                )
                BaseText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    text = "$issue ${location.name} $year",
                    maxLines = 1,
                    style = TypeDefinition.labelMedium,
                )
            }
        }
    }
}

@Composable
private fun ScreenPreview() {
    HomeScreen(
        state = HomeContract.UiState(),
        forwardAction = {}
    )
}
