package com.example.nyc_school.ui.components

import android.view.KeyEvent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nyc_school.R
import com.example.nyc_school.ui.theme.customColorsPalette
import kotlinx.coroutines.delay

enum class TextDisplaySize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun HeaderTextComponent(
    modifier: Modifier = Modifier,
    textDisplaySize: TextDisplaySize = TextDisplaySize.SMALL,
    text: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = when (textDisplaySize) {
            TextDisplaySize.SMALL -> MaterialTheme.typography.headlineSmall
            TextDisplaySize.MEDIUM -> MaterialTheme.typography.headlineMedium
            TextDisplaySize.LARGE -> MaterialTheme.typography.headlineLarge
        },
        color = MaterialTheme.customColorsPalette.titleTextColor,
        textAlign = textAlign
    )
}

@Composable
fun TitleTextComponent(
    modifier: Modifier = Modifier,
    textDisplaySize: TextDisplaySize = TextDisplaySize.LARGE,
    text: String,
    maxLines: Int = 1,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = when (textDisplaySize) {
            TextDisplaySize.SMALL -> MaterialTheme.typography.titleSmall
            TextDisplaySize.MEDIUM -> MaterialTheme.typography.titleMedium
            TextDisplaySize.LARGE -> MaterialTheme.typography.titleLarge
        },
        color = MaterialTheme.customColorsPalette.titleTextColor,
        textAlign = textAlign,
        maxLines = maxLines,
        fontWeight = fontWeight
    )
}

@Composable
fun BodyTextComponent(
    modifier: Modifier = Modifier,
    textDisplaySize: TextDisplaySize = TextDisplaySize.MEDIUM,
    text: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = when (textDisplaySize) {
            TextDisplaySize.SMALL -> MaterialTheme.typography.bodySmall
            TextDisplaySize.MEDIUM -> MaterialTheme.typography.bodyMedium
            TextDisplaySize.LARGE -> MaterialTheme.typography.bodyLarge
        },
        color = MaterialTheme.customColorsPalette.bodyTextColor,
        textAlign = textAlign
    )
}

@Composable
fun SubtitleTextComponent(
    text: String
) {
}

@Composable
fun LabelTextComponent(
    modifier: Modifier = Modifier,
    textDisplaySize: TextDisplaySize = TextDisplaySize.MEDIUM,
    text: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = when (textDisplaySize) {
            TextDisplaySize.SMALL -> MaterialTheme.typography.labelSmall
            TextDisplaySize.MEDIUM -> MaterialTheme.typography.labelMedium
            TextDisplaySize.LARGE -> MaterialTheme.typography.labelLarge
        },
        color = MaterialTheme.customColorsPalette.bodyTextColor,
        textAlign = textAlign
    )
}

@Composable
fun WatermarkTextComponent(
    text: String
) {
}

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    colors: TextFieldColors? = null,
    labelValue: String,
    textValue: String? = null,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.None,
    onActionKey: (KeyboardActionScope.() -> Unit)? = null,
    startIcon: ImageVector? = Icons.Filled.TextFields,
    endIcon: ImageVector? = null,
    iconAction: () -> Unit = {},
    iconTint: Color? = null,
    pattern: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    errorEnabled: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val isError = remember {
        mutableStateOf(false)
    }
    var value by remember { mutableStateOf(textValue ?: "") }
    var valueChanged by remember { mutableStateOf(value.trim().isNotEmpty()) }

//    LaunchedEffect(valueChanged) {
//        delay(1500L)
//        isError.value = value.isNotEmpty() && !value.trim().isValidEmail()
//        valueChanged = false
//    }

    OutlinedTextField(
        visualTransformation = visualTransformation,
        enabled = enabled,
        readOnly = !enabled,
        supportingText = if (errorEnabled) {
            {
                if (isError.value) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorMessage ?: "The text doesn't match $pattern",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        } else {
            null
        },
        singleLine = singleLine,
        maxLines = 5,
        modifier = if (errorEnabled) {
            modifier
        } else {
            modifier.padding(bottom = 12.dp)
        }
            .clip(ComponentShape.small),
        label = { Text(text = labelValue) },
        colors = colors
            ?: OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.customColorsPalette.titleTextColor,
                disabledTextColor = MaterialTheme.customColorsPalette.bodyTextColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.customColorsPalette.menuIconColor,
                focusedBorderColor = MaterialTheme.customColorsPalette.foregroundColor,
                disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
                focusedLeadingIconColor = MaterialTheme.customColorsPalette.menuIconColor,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
                focusedLabelColor = MaterialTheme.customColorsPalette.foregroundColor,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
            ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = if (keyboardType == KeyboardType.Text && singleLine && imeAction == ImeAction.None) {
                ImeAction.Next
            } else {
                imeAction
            }
        ),
        keyboardActions = KeyboardActions(
            onDone = onActionKey,
            onGo = onActionKey,
            onNext = onActionKey,
            onPrevious = onActionKey,
            onSend = onActionKey,
            onSearch = onActionKey,
        ),
        value = value,
        onValueChange = { text ->
//            pattern?.let {
//                isError.value = it.isNotEmpty() && !text.matches(Regex(it))
//            }
            valueChanged = text.trim().isNotEmpty()
            value = text
            onValueChanged(text)
        },
        leadingIcon = {
            startIcon?.let {
                Icon(
                    tint = iconTint ?: LocalContentColor.current,
                    imageVector = it,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        iconAction()
                    }
                )
            }
        },
        trailingIcon = {
            endIcon?.let {
                Icon(
                    tint = iconTint ?: LocalContentColor.current,
                    imageVector = it,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        iconAction()
                    }
                )
            }
        },
    )
}

@Composable
fun SearchBar(
//    keyboardController: SoftwareKeyboardController?,
    onSearchQueryChanged: (String) -> Unit,
    onSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var expanded by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current

    Surface(
        color = Color.Transparent,
        modifier = if (expanded) {
            modifier.fillMaxWidth()
        } else {
            modifier//.width(32.dp)
        }
//            .fillMaxHeight()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .background(color = MaterialTheme.customColorsPalette.backgroundColor,),
    ) {
        if (!expanded) {
            //search btn
            keyboardController?.hide()
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                ),
                modifier = modifier
                    .background(color = Color.Transparent)
                    .width(32.dp)
//                    .fillMaxHeight()
//                    .fillMaxSize()
                ,
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Open search view",
                    tint = MaterialTheme.customColorsPalette.menuIconColor,
                )
            }
        } else {
            TextFieldComponent(
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFF132C44),
                    unfocusedTextColor = Color(0xFF132C44),
                ),
                labelValue = stringResource(id = R.string.search),
                textValue = text,
                onValueChanged = {
                    text = it
                    if (it.isNotEmpty()) {
                        onSearchQueryChanged(it)
                    }
                },
                startIcon = Icons.Filled.Close,
                iconAction = {
                    if (text.isNotEmpty()) {
                        text = ""
                    } else {
                        expanded = !expanded
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
//                    .animateContentSize(
//                        animationSpec = tween(
//                            durationMillis = 300,
//                            easing = LinearOutSlowInEasing
//                        )
//                    )
                    .background(color = Color.Transparent)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onSearchQuery(text)
                            true
                        }
                        false
                    },
                singleLine = true,
                errorEnabled = false,
                imeAction = ImeAction.Search,
                onActionKey = {
                    keyboardController?.hide()
                    //or hide keyboard
                    focusManager.clearFocus()
                    onSearchQuery(text)
                },
            )
        }
    }
}

enum class Orientation {
    VERTICAL,
    HORIZONTAL
}

@Composable
fun <T> RadioGroupComponent(
    orientation: Orientation = Orientation.VERTICAL,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (Modifier, String, Boolean, Boolean, () -> Unit) -> Unit = { _modifier, label, selected, _enabled, onClick ->
        RadioButtonComponent(
            modifier = _modifier,
            label = label,
            selected = selected,
            enabled = _enabled,
            onClick = onClick
        )
    }
) {
    var selectedItemIndex by remember { mutableIntStateOf(selectedIndex) }
    if (orientation == Orientation.VERTICAL) {
        Column(modifier = modifier) {
            label?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }
            for (i in items.indices) {
                drawItem(
                    Modifier,
                    selectedItemToString.invoke(items[i]),
                    i == selectedItemIndex,
                    enabled
                ) {
                    if (enabled) {
                        selectedItemIndex = i
                        onItemSelected(i, items[i])
                    }
                }
            }
        }
    } else {
        Column(modifier = modifier) {
            label?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }

            Row {
                for (i in items.indices) {
                    drawItem(
                        Modifier.weight(1f),
                        selectedItemToString.invoke(items[i]),
                        i == selectedItemIndex,
                        enabled
                    ) {
                        if (enabled) {
                            selectedItemIndex = i
                            onItemSelected(i, items[i])
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RadioButtonComponent(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    selected: Boolean,
    onClick: () -> Unit
) {
//    var itemSelected by remember { mutableStateOf(selected) }
    val icon =
        if (selected) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides selected) {
        TextFieldComponent(
            errorEnabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.customColorsPalette.titleTextColor,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.high),
                focusedLabelColor = MaterialTheme.customColorsPalette.bodyTextColor,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.high),
            ),
            labelValue = label,
//        textValue = null,
            onValueChanged = {},
            startIcon = icon,
            iconAction = {
                if (enabled) {
                    onClick()
//                if (!itemSelected) {
////                    itemSelected = !itemSelected
//                    onCheckedChanged(itemSelected)
//                }
                }
            },
            enabled = false,
            singleLine = true,
            modifier = if (enabled) {
                modifier.clickable {
                    onClick()
//                if (!itemSelected) {
////                    itemSelected = !itemSelected
//                    onCheckedChanged(itemSelected)
//                }
                }
            } else {
                modifier
            }
                .fillMaxWidth()
                .clip(ComponentShape.small)
                .focusable(false),
        )
    }
}