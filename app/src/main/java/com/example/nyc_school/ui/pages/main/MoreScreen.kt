package com.example.nyc_school.ui.pages.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nyc_school.R
import com.example.nyc_school.ui.navigation.Screen
import com.example.nyc_school.ui.theme.customColorsPalette

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    onItemClick: (Screen) -> Unit
) {

    val moreItems = listOf(
        //settings
        MoreItemData(
            title = stringResource(id = R.string.settings),
            subTitle = stringResource(R.string.change_the_app_s_configuration),
            icon = Icons.Filled.Settings,
            destination = Screen.Settings
        ),
    )

    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        content = {
            items(moreItems) { item ->
                MoreItem(
                    icon = item.icon,
                    title = item.title,
                    subTitle = item.subTitle
                ) {
                    onItemClick(item.destination)
                }
            }
        }
    )
}

data class MoreItemData(
    val title: String,
    val subTitle: String?,
    val icon: ImageVector,
    val destination: Screen
)

@Composable
fun MoreItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subTitle: String?,
    onItemClick: () -> Unit
) {
    Card(
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(10.dp),
        // modifier = modifier.size(280.dp, 240.dp)
        modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 10.dp)
            .clickable {
                onItemClick()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customColorsPalette.onBackgroundColor
        )
    ) {
        Column(
            modifier
//                .background(Color.White)
        ) {

            Image(
                imageVector = icon,
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.customColorsPalette.iconColor
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.customColorsPalette.titleTextColor
                )
                if (!subTitle.isNullOrEmpty()) {
                    Text(
                        text = subTitle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.customColorsPalette.bodyTextColor
                    )
                }
            }
        }
    }
}