package com.example.nyc_school.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.navigation.Screen
import com.example.nyc_school.ui.pages.details.DetailScreen
import com.example.nyc_school.ui.pages.main.CategoryScreen
import com.example.nyc_school.ui.pages.main.HomeScreen
import com.example.nyc_school.ui.pages.main.MoreScreen
import com.example.nyc_school.ui.pages.main.SearchScreen
import com.example.nyc_school.ui.pages.settings.SettingsScreen
import com.example.nyc_school.ui.pages.splash.SplashScreen
import com.example.nyc_school.ui.theme.customColorsPalette
import com.example.nyc_school.utils.Utils.isDark
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationActions: AppNavigationActions,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Splash.route,
        builder = {
            composable(
                route = Screen.Splash.route
            ) {
                SplashScreen(
                    navActions = navigationActions,
                )
            }
            mainGraph(
                navActions = navigationActions
            )
            detailsGraph(
                navActions = navigationActions
            )
//            settingsGraph(
//                navActions = navigationActions
//            )
            verticalSlideComposable(
                route = Screen.Settings.route
            ) {
                SettingsScreen(
                    navActions = navigationActions
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentDestination: NavDestination?,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationClick: () -> Unit,
    onSearchQuery: (String) -> Unit,
) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    var title by remember { mutableStateOf(currentDestination?.route.toString()) }
    var displayBackButton by remember { mutableStateOf(false) }
    when (currentDestination?.route) {

        else -> {
            displayBackButton = false
        }
    }
//    var showLogout by remember {
//        mutableStateOf(false)
//    }
    var showSearch by remember {
        mutableStateOf(true)
    }
//    var displayLogoutDialog by remember {
//        mutableStateOf(false)
//    }

    title = when (currentDestination?.route) {
        Screen.Home.route -> {
            stringResource(id = Screen.Home.resourceId)
        }

        Screen.Search.route -> {
            stringResource(id = Screen.Search.resourceId)
        }

        Screen.More.route -> {
            stringResource(id = Screen.More.resourceId)
        }

        Screen.Details.route -> {
            stringResource(id = Screen.Details.resourceId)
        }

        Screen.Settings.route -> {
            stringResource(id = Screen.Settings.resourceId)
        }

        "${Screen.Details.route}/{dbn}" -> {
            stringResource(id = Screen.Details.resourceId)
        }

        else -> {
//            currentDestination?.label.toString()
            currentDestination?.route.toString().split("/")[0]
        }
    }

    when (currentDestination?.route) {

        Screen.Home.route -> {
            showSearch = false
            displayBackButton = false
        }

        Screen.Settings.route -> {
            showSearch = false
            displayBackButton = true
        }

        "${Screen.Details.route}/{dbn}" -> {
            showSearch = false
            displayBackButton = true
        }

        else -> {
            showSearch = true
        }
    }

//    val keyboardController = LocalSoftwareKeyboardController.current

//    if (displayLogoutDialog) {
//        DialogComponent(
//            cancellable = false,
//            title = stringResource(id = R.string.logout_title),
//            message = stringResource(id = R.string.logout_message),
//            positiveText = "Ok",
//            onPositiveClick = {
//                userViewModel.logout(
//                    onLogout = onLogout
//                )
//            },
//            negativeText = stringResource(id = R.string.cancel),
//            onNegativeClick = {},
//            onDismiss = {
//                displayLogoutDialog = false
//            }
//        )
//    }

    rememberSystemUiController().setStatusBarColor(
        color = MaterialTheme.customColorsPalette.navigationColor,
        darkIcons = !MaterialTheme.customColorsPalette.navigationColor.isDark()
    )

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier
//                    .fillMaxWidth()
                    .padding(end = 12.dp),
//                color = if (color.value.isDark()) Color.White else Color.Black,
                color = MaterialTheme.customColorsPalette.titleTextColor,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            if (displayBackButton) {
                Button(
                    onClick = onNavigationClick,
                    Modifier
                        .background(Color.Transparent)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        modifier = Modifier,
//                        tint = if (color.value.isDark()) Color.White else Color.Black,
                        tint = MaterialTheme.customColorsPalette.titleTextColor,
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
//            if (showLogout) {
//                Icon(
//                    modifier = Modifier
//                        .width(32.dp)
//                        .fillMaxHeight()
////                        .padding(8.dp)
//                        .clickable {
////                            userViewModel.logout(
////                                onLogout = onLogout
////                            )
//                            displayLogoutDialog = true
//                        },
//                    imageVector = Icons.Filled.Logout,
//                    contentDescription = "",
//                    tint = MaterialTheme.customColorsPalette.menuIconColor,
//                )
//            }
            if (showSearch) {
//                SearchBar(
//                    modifier = Modifier.fillMaxHeight(),
//                    onSearchQueryChanged = {},
//                    onSearchQuery = {
//                        searchQuery = it
//                        onSearchQuery(it)
//                    },
//                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier
            .background(color = Color.White),
        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = color.value
            containerColor = MaterialTheme.customColorsPalette.navigationColor,
        )
    )
}

@Composable
fun BottomBar(
    navActions: AppNavigationActions,
    items: List<Screen>,
    currentDestination: NavDestination?
) {
    rememberSystemUiController().setNavigationBarColor(
        color = MaterialTheme.customColorsPalette.navigationColor,
        darkIcons = !MaterialTheme.customColorsPalette.navigationColor.isDark()
    )
    BottomNavigation(
//        backgroundColor = color.value,
//        contentColor = color.
        backgroundColor = MaterialTheme.customColorsPalette.bottomNavigationBarColor,
        contentColor = MaterialTheme.customColorsPalette.menuIconColor,
    ) {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon ?: Icons.Filled.Favorite,
                        contentDescription = null,
//                        tint = if (color.value.isDark()) Color.White else Color.Black
                        tint = MaterialTheme.customColorsPalette.menuIconColor
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.resourceId),
                        maxLines = 1,
//                        color = if (color.value.isDark()) Color.White else Color.Black,
                        color = MaterialTheme.customColorsPalette.titleTextColor,
                        softWrap = true,
                    )
                },
                alwaysShowLabel = false,
                selected = screen.route == currentDestination?.route,
                onClick = {
                    if (currentDestination?.route == screen.route) {
                        return@BottomNavigationItem
                    }
//                    navController.navigate(screen.route) {
//                        // Pop up to the start destination of the graph to
//                        // avoid building up a large stack of destinations
//                        // on the back stack as users select items
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        // Avoid multiple copies of the same destination when
//                        // reselecting the same item
//                        launchSingleTop = true
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                    }
                    navActions.navigateTo(screen)
                }
            )
        }
    }
}

fun NavGraphBuilder.mainGraph(
    navActions: AppNavigationActions
) {
    navigation(startDestination = Screen.Home.route, route = Screen.Main.route) {
        horizontalSlideComposable(Screen.Home.route) {
            HomeScreen(
                navActions = navActions
            )
        }
        horizontalSlideComposable(Screen.Category.route) {
            CategoryScreen(
                navActions = navActions
            )
        }
        horizontalSlideComposable(Screen.Search.route) {
            SearchScreen(
                navActions = navActions
            )
        }
        horizontalSlideComposable(Screen.More.route) {
            MoreScreen {
                navActions.navigateTo(it)
            }
        }
    }
}

fun NavGraphBuilder.detailsGraph(
    navActions: AppNavigationActions
) {
    navigation(startDestination = Screen.Details.route, route = "details") {
        verticalSlideComposable(
            route = "${Screen.Details.route}/{dbn}",
            arguments = listOf(
                navArgument("dbn") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
//            val str = navBackStackEntry.arguments?.getString("school")
//            str?.let {
//                val pre = "school="
//                var value = str
//                if (value.startsWith(pre)) {
//                    value = value.substring(pre.length)
//                }
//                val school = Gson().fromJson(
//                    value,
//                    SchoolModel::class.java
//                )
//                DetailScreen(model = school)
//            }
            val dbn = navBackStackEntry.arguments?.getString("dbn")
            dbn?.let {
                DetailScreen(
                    dbn = it,
                    navActions = navActions
                )
            }
        }
    }
}

fun NavGraphBuilder.settingsGraph(
    navActions: AppNavigationActions
) {
    navigation(startDestination = Screen.Settings.route, route = "settings") {
        verticalSlideComposable(
            route = Screen.Settings.route
        ) {
            SettingsScreen(
                navActions = navActions
            )
        }
    }
}

fun NavGraphBuilder.verticalSlideComposable(
    route: String,
    arguments: List<NamedNavArgument> = listOf(),
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInVertically(
                animationSpec = tween(400),
                initialOffsetY = { 100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutVertically(
                animationSpec = tween(400),
                targetOffsetY = { -100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutVertically(
                animationSpec = tween(400),
                targetOffsetY = { 100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInVertically(
                animationSpec = tween(400),
                initialOffsetY = { -100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        exitTransition = exitTransition
    ) {
        content(it)
    }
}

fun NavGraphBuilder.horizontalSlideComposable(
    route: String,
    arguments: List<NamedNavArgument> = listOf(),
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { 100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { 100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        exitTransition = exitTransition
    ) {
        content(it)
    }
}