package com.example.nyc_school.ui.pages.settings

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nyc_school.R
import com.example.nyc_school.ui.components.RadioGroupComponent
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.theme.customColorsPalette
import com.example.nyc_school.utils.Common

enum class Themes(@StringRes val title: Int, val value: Int) {
    LIGHT(R.string.light, AppCompatDelegate.MODE_NIGHT_NO),
    DARK(R.string.dark, AppCompatDelegate.MODE_NIGHT_YES);

    fun valueOf(): Int = value
    @StringRes
    fun titleOf(): Int = title

    companion object {
        fun fromValue(value: Int): Themes {
            return Themes.values().find { it.value == value } ?: LIGHT
        }
    }
}
data class Language(
    val title: String,
    val value: String,
    val country: String
)
@Composable
fun SettingsScreen(
    navActions: AppNavigationActions
) {
    val context = LocalContext.current

    val languageList = listOf(
        Language(stringResource(id = R.string.english), "en", "US"),
        Language(stringResource(id = R.string.french), "fr", "FR"),
//        Language(stringResource(id = R.string.creole), "ht", "HT"),
    )

    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        OptionsItem(
            title = stringResource(R.string.language),
            subTitle = stringResource(R.string.change_the_application_s_display_language),
            icon = Icons.Filled.Language,
//            onItemClick = {},
            content = { paddingValues ->
                var selectedIndex by remember {
                    mutableIntStateOf(
                        languageList.indexOf(
                            languageList.find {
                                it.value == Common.preferences(context)
                                    .getString(
                                        Common.PREFERENCES.LANGUAGE_KEY,
                                        Common.DEFAULT_LANGUAGE
                                    )
                            }
                        )
                    )
                }

                RadioGroupComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    items = languageList,
                    selectedIndex = selectedIndex,
                    selectedItemToString = { it.title },
                    onItemSelected = { index, item ->
                        if (selectedIndex != index) {
                            selectedIndex = index
                            Common.preferences(context)
                                .edit()
                                .putString(Common.PREFERENCES.LANGUAGE_KEY, item.value)
                                .apply()
                            (context as? Activity)?.recreate()
                        }
                    }
                )
            }
        )

        OptionsItem(
            title = stringResource(R.string.theme),
            subTitle = stringResource(R.string.change_the_application_s_display_theme),
            icon = Icons.Filled.DarkMode,
//            onItemClick = {
//                appNavAction.navigateTo(Screen.Theme)
//            },
//            enabled = false
            content = { paddingValues ->
                var selectedIndex by remember {
                    mutableIntStateOf(
                        Themes.values().indexOf(
                            Themes.fromValue(
                                Common.preferences(context)
                                    .getInt(
                                        Common.PREFERENCES.THEME_KEY,
                                        Common.DEFAULT_THEME
                                    )
                            )
                        )
                    )
                }

                RadioGroupComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    items = Themes.values().toList(),
                    selectedIndex = selectedIndex,
                    selectedItemToString = { context.getString(it.title) },
                    onItemSelected = { index, item ->
                        if (selectedIndex != index) {
                            selectedIndex = index
                            Common.preferences(context)
                                .edit()
                                .putInt(Common.PREFERENCES.THEME_KEY, item.value)
                                .apply()
                            (context as? Activity)?.recreate()
                        }
                    }
                )
            }
        )
    }
}

@Composable
private fun OptionsItem(
    enabled: Boolean = true,
    title: String,
    subTitle: String,
    icon: ImageVector,
    onItemClick: (() -> Unit)? = null,
    content: @Composable (ColumnScope.(PaddingValues) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val endIcon = if (content == null) {
        Icons.Outlined.ChevronRight
    } else {
        if (expanded) {
            Icons.Outlined.ExpandLess
        } else {
            Icons.Outlined.ExpandMore
        }
    }

    Surface(
        modifier = if (enabled) {
            Modifier
        } else {
            Modifier
                .alpha(.45f)
        }
            .animateContentSize()
            .fillMaxWidth(),
        elevation = if (expanded) 7.dp else 0.dp,
        color = MaterialTheme.customColorsPalette.backgroundColor
    ) {
        Column {
            Row(
                modifier = if (enabled) {
                    Modifier
                        .clickable(enabled = true) {
                            if (content != null) {
                                expanded = !expanded
                            } else {
                                onItemClick?.invoke()
                            }
                        }
                } else {
                    Modifier
                }
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Icon
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.customColorsPalette.iconColor
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(weight = 3f, fill = false)
                            .padding(start = 16.dp)
                    ) {

                        // Title
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.customColorsPalette.titleTextColor
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Sub title
                        Text(
                            text = subTitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.customColorsPalette.bodyTextColor
                        )
                    }

                    // Right arrow icon
                    Icon(
                        modifier = Modifier
                            .weight(weight = 1f, fill = false),
                        imageVector = endIcon,
                        contentDescription = title,
                        tint = MaterialTheme.customColorsPalette.menuIconColor
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            if (expanded && content != null) {
                content(PaddingValues(start = 24.dp, end = 24.dp, bottom = 8.dp))
            }
        }
    }

}