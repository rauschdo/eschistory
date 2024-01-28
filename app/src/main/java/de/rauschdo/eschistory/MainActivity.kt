package de.rauschdo.eschistory

import android.os.Bundle
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dagger.hilt.android.AndroidEntryPoint
import de.rauschdo.eschistory.data.RealmHelper
import de.rauschdo.eschistory.ui.main.DialogController
import de.rauschdo.eschistory.ui.main.MainAppState
import de.rauschdo.eschistory.ui.main.rememberMainAppState
import de.rauschdo.eschistory.ui.navigation.AppNav
import de.rauschdo.eschistory.ui.navigation.AppNavDest
import de.rauschdo.eschistory.ui.navigation.BottomBarItem
import de.rauschdo.eschistory.ui.navigation.MainNavHost
import de.rauschdo.eschistory.ui.theme.EurovisionHistoryTheme
import de.rauschdo.eschistory.utility.DataSource
import de.rauschdo.eschistory.utility.browser.CustomTabActivityHelper
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

val LocalAppNav = compositionLocalOf { AppNav() }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var realmHelper: RealmHelper

    @Inject
    lateinit var dialogController: DialogController

    private var mCustomTabActivityHelper: CustomTabActivityHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCustomTabActivityHelper = CustomTabActivityHelper(this)
        with(realmHelper) {
            DataSource.create(this@MainActivity)?.let {
                handleContestDataset(it)
            }
        }

        enableEdgeToEdge()

        setContent {
            EurovisionHistoryTheme {
                MainApp(
                    dialogController = dialogController
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mCustomTabActivityHelper?.bindCustomTabsService()
    }

    override fun onStop() {
        super.onStop()
        mCustomTabActivityHelper?.unbindCustomTabsService()
    }
}

@Composable
fun MainApp(
    appState: MainAppState = rememberMainAppState(),
    dialogController: DialogController
) {
    val coroutineScope = rememberCoroutineScope()
    val currentDestination = appState.navigator.currentDestination

    // When dialog is shown we handle back differently
    if (dialogController.dialogQueue.value.isNotEmpty()) {
        with(dialogController.dialogQueue.value[0]) {
            this.content()
        }
        BackHandler {
            dialogController.consumeDialog(coroutineScope)
            return@BackHandler
        }
    } else {
        PredictiveBackHandler { progress: Flow<BackEventCompat> ->
            try {
                progress.collect { backevent ->
                    Timber.tag("back").i(backevent.toString())
                }
                //completion
                if (!appState.navigator.onBackClick()) {
                    // TODO app exit dialog or snackbar
                }
            } catch (e: CancellationException) {
                // nothing
            }
        }
    }

    CompositionLocalProvider(LocalAppNav provides appState.navigator) {
        Scaffold(
            topBar = {
                // TODO
            },
            bottomBar = {
                NavigationBar {
                    listOf(BottomBarItem.Home, BottomBarItem.About).forEach {
                        NavigationBarItem(
                            modifier = Modifier.semantics(true) {
                                contentDescription = buildString {
                                    append(it.label)
                                }
                            },
                            selected = currentDestination.isSelected(it.dest),
                            colors = NavigationBarItemDefaults.colors(
                                selectedTextColor = Color.Blue,
                                unselectedTextColor = Color.Black,
                                indicatorColor = Color.LightGray
                            ),
                            icon = {
                                Icon(
                                    imageVector = if (currentDestination.isSelected(it.dest)) it.iconActive else it.icon,
                                    contentDescription = it.label,
                                    tint = Color.Unspecified
                                )
                            },
                            label = {
                                Text(text = it.label)
                            },
                            onClick = {
                                when (it) {
                                    is BottomBarItem.Home -> appState.navigator.navigateRoot(
                                        AppNavDest.Home
                                    )

                                    is BottomBarItem.About -> appState.navigator.navigateRoot(
                                        AppNavDest.AboutAuthor
                                    )

                                    else -> Unit
                                }
                            }
                        )
                    }
                }
            }
        ) {
            MainNavHost(
                modifier = Modifier.padding(it),
                navigator = appState.navigator,
            )
        }
    }
}

private fun NavDestination?.isSelected(current: AppNavDest): Boolean {
    return this?.hierarchy?.any { it.route == current.destinationId() } == true
}
