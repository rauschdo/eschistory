package de.rauschdo.eschistory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dagger.hilt.android.AndroidEntryPoint
import de.rauschdo.eschistory.ui.main.MainAppState
import de.rauschdo.eschistory.ui.main.rememberMainAppState
import de.rauschdo.eschistory.ui.navigation.AppNav
import de.rauschdo.eschistory.ui.navigation.AppNavDest
import de.rauschdo.eschistory.ui.navigation.BottomBarItem
import de.rauschdo.eschistory.ui.navigation.MainNavHost
import de.rauschdo.eschistory.ui.theme.EurovisionHistoryTheme
import de.rauschdo.eschistory.utility.DataSource
import de.rauschdo.eschistory.utility.browser.CustomTabActivityHelper

val LocalAppNav = compositionLocalOf { AppNav() }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var mCustomTabActivityHelper: CustomTabActivityHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCustomTabActivityHelper = CustomTabActivityHelper(this)
        DataSource.create(this)
        setContent {
            EurovisionHistoryTheme {
                MainApp()
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
) {
    val currentDestination = appState.navigator.currentDestination
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
