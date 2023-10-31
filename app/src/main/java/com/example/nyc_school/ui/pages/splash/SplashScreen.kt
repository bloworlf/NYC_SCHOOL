package com.example.nyc_school.ui.pages.splash

import androidx.compose.runtime.Composable
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.navigation.Screen

@Composable
fun SplashScreen(navActions: AppNavigationActions) {
    navActions.navigateTo(Screen.Main)
}