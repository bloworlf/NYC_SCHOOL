package com.example.nyc_school.ui.pages

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nyc_school.ui.components.AppBar
import com.example.nyc_school.ui.components.AppNavigation
import com.example.nyc_school.ui.components.BottomBar
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.navigation.Screen
import com.example.nyc_school.ui.theme.NYC_SCHOOLTheme
import com.example.nyc_school.ui.theme.customColorsPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    NYC_SCHOOLTheme(
        darkTheme = isNightMode(),
    ) {
        val navController: NavHostController = rememberNavController()
        val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
        var currentDestination by remember {
            mutableStateOf(currentNavBackStackEntry?.destination)
        }
        val navigationActions = remember(navController) {
            AppNavigationActions(navController)
        }

        val appBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)

        var displayAppBar by remember {
            mutableStateOf(false)
        }

        var displayBottomBar by remember {
            mutableStateOf(false)
        }

        val destinationChangedListener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                when (destination.route) {

                    Screen.Splash.route -> {
                        displayAppBar = false
                        displayBottomBar = false
                    }

                    Screen.Settings.route -> {
                        displayAppBar = true
                        displayBottomBar = false
                    }

                    "${Screen.Details.route}/{dbn}" -> {
                        displayAppBar = true
                        displayBottomBar = false
                    }

                    else -> {
                        displayAppBar = true
                        displayBottomBar = true
                    }
                }
                currentDestination = destination
            }
        LaunchedEffect(Unit) {
            navController.addOnDestinationChangedListener(destinationChangedListener)
        }

        Scaffold (
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.customColorsPalette.backgroundColor,
            topBar = {
                if (displayAppBar) {
                    AppBar(
                        currentDestination = currentDestination,
                        scrollBehavior = scrollBehavior,
//                        color = statusBarColor,
                        onNavigationClick = {
                            navigationActions.navigateBack()
                        },
                        onSearchQuery = { query ->

                        },
                    )
                }
            },
            content = {
                AppNavigation(
                    modifier = Modifier.padding(it),
                    navController = navController,
                    navigationActions = navigationActions,
                )
            },
            bottomBar = {
                if (displayBottomBar) {
                    BottomBar(
                        navActions = navigationActions,
                        items = listOf(
                            Screen.Home,
                            Screen.Category,
                            Screen.Search,
                            Screen.More,
                        ),
                        currentDestination = currentDestination
                    )
                }
            }
        )
    }
}

@Composable
private fun isNightMode() = when (AppCompatDelegate.getDefaultNightMode()) {
    AppCompatDelegate.MODE_NIGHT_NO -> false
    AppCompatDelegate.MODE_NIGHT_YES -> true
    else -> isSystemInDarkTheme()
}