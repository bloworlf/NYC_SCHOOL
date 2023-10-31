package com.example.nyc_school.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.nyc_school.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Destinations {
    const val SPLASH = "Splash"
    const val MAIN = "main"
    const val HOME = "Home_Screen"
    const val SEARCH = "Search"
    const val MORE = "More"
    const val DETAILS = "Details"
    const val CATEGORY = "Category"
    const val SETTINGS = "Settings"
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector?) {
    object Splash : Screen(Destinations.SPLASH, R.string.splash, Icons.Filled.Home)
    object Main : Screen(Destinations.MAIN, R.string.home, Icons.Filled.Home)
    object Home : Screen(Destinations.HOME, R.string.home, Icons.Filled.Home)
    object Search : Screen(Destinations.SEARCH, R.string.search, Icons.Filled.Search)
    object More : Screen(Destinations.MORE, R.string.more, Icons.Filled.MoreHoriz)
    object Details : Screen(Destinations.DETAILS, R.string.details, Icons.Filled.Details)
    object Category : Screen(Destinations.CATEGORY, R.string.category, Icons.Filled.Category)
    object Settings : Screen(Destinations.SETTINGS, R.string.settings, Icons.Filled.Settings)
}

class AppNavigationActions(private val navController: NavHostController) {
    fun navigateTo(route: String, arguments: Map<String, Any?>) {
        var args1 = ""
        var args2 = ""
        val iterator = arguments.entries.iterator()
        while (iterator.hasNext()) {
            var (key, value) = iterator.next()
            if (value == null) {
                continue
            }
            if (value is String && value.contains("/")) {
                value = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
            }
            args1 += "$value/"
            args2 += "$key=$value/"
        }
        args1 = args1.substring(0, args1.length - 1)
        args2 = args2.substring(0, args2.length - 1)
        try {
            navController.navigate("${route}/$args1") {
                popUpTo(route)
            }
        } catch (e: Exception) {
            navController.navigate("${route}/$args2") {
                popUpTo(route)
            }
        }
    }

    fun navigateTo(screen: Screen, arguments: Map<String, Any?>) {
        var args1 = ""
        var args2 = ""
        val iterator = arguments.entries.iterator()
        while (iterator.hasNext()) {
            var (key, value) = iterator.next()
            if (value == null) {
                continue
            }
            if (value is String && value.contains("/")) {
                value = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
            }
            args1 += "$value/"
            args2 += "$key=$value/"
        }
        args1 = args1.substring(0, args1.length - 1)
        args2 = args2.substring(0, args2.length - 1)
        try {
            navController.navigate("${screen.route}/$args1") {
                popUpTo(screen.route)
            }
        } catch (e: Exception) {
            navController.navigate("${screen.route}/$args2") {
                popUpTo(screen.route)
            }
        }
    }

    fun navigateTo(screen: Screen) {
        navController.navigate(screen.route) {
            popUpTo(screen.route)
        }
    }

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(route)
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}