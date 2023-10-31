package com.example.nyc_school.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nyc_school.ui.theme.customColorsPalette

@Composable
fun <T> Expandable(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String,
    onValueUpdated: (List<T>) -> Unit,
    listValues: List<T>?,
    content: @Composable (T?, Boolean, (T) -> Unit) -> Unit
//    = {
//            value,
//            contentEnabled,
//            onValueChanged,
//        ->
//        when (value) {
//            is String -> {
////                Nationality(
////                    value = value as MigrantNationaliteModel,
////                    enabled = contentEnabled,
////                    onValueChanged = onValueChanged as ((MigrantNationaliteModel) -> Unit),
////                    countryViewModel = hiltViewModel()
////                )
//            }
//        }
//    },
) {
    var expanded by remember { mutableStateOf(false) }
    var icon = if (expanded) {
        Icons.Default.ExpandLess
    } else {
        Icons.Default.ExpandMore
    }
    var contentCount by remember {
        mutableIntStateOf(
            if ((listValues?.size ?: 1) == 0) {
                if (enabled) 1 else 0
            } else {
                listValues?.size ?: 1
            }
        )
    }
    var values by remember { mutableStateOf(listValues?.toMutableList() ?: mutableListOf()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(MaterialTheme.shapes.small)
            .border(
                width = 1.dp,
                color = MaterialTheme.customColorsPalette.borderColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        expanded = !expanded
                    },
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight(400),
                letterSpacing = 3.sp,
                color = MaterialTheme.customColorsPalette.titleTextColor
            )
            Icon(
                modifier = Modifier.clickable {
                    expanded = !expanded
                },
                imageVector = icon,
                contentDescription = "Expand/Collapse",
                tint = MaterialTheme.customColorsPalette.menuIconColor
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))

            for (i in 0 until contentCount) {
                content(
                    if (values.isNotEmpty() && values.size > i) {
                        values[i]
                    } else {
                        null
                    },
                    enabled
                ) {
                    if (values.size <= i) {
                        values.add(it)
                    } else {
                        values[i] = it
                    }
                    onValueUpdated(values)
                }
//                Spacer(modifier = Modifier.height(8.dp))
            }

            if (enabled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (contentCount > 1) {
                                if (contentCount == values.size) {
                                    values.removeLast()
                                }
                                contentCount--
                            }
                        },
                        enabled = contentCount > 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove"
                        )
                    }
                    Spacer(modifier = Modifier.weight(.05f))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            contentCount++
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                }
            }
        }
    }
}