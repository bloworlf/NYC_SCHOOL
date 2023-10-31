package com.example.nyc_school.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager
import com.example.nyc_school.ui.pages.MainScreen
import com.example.nyc_school.ui.theme.NYC_SCHOOLTheme
import com.example.nyc_school.utils.BaseContextWrapper
import com.example.nyc_school.utils.Common
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get the applied theme
        AppCompatDelegate.setDefaultNightMode(
            Common.preferences(this)
                .getInt(
                    Common.PREFERENCES.THEME_KEY,
                    Common.DEFAULT_THEME
                )
        )

        setContent {
            MainScreen()
        }
    }

    override fun attachBaseContext(base: Context) {
        // fetch from shared preference also save the same when applying. Default here is en = English
        val language = Common.preferences(base)
            .getString(Common.PREFERENCES.LANGUAGE_KEY, Common.DEFAULT_LANGUAGE)!!
        super.attachBaseContext(BaseContextWrapper.wrap(base, language))
    }
}