package com.example.nyc_school.ui.pages.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nyc_school.data.model.school.SchoolModel
import com.example.nyc_school.data.network.response.ErrorCode
import com.example.nyc_school.data.vm.SchoolViewModel
import com.example.nyc_school.ui.components.BodyTextComponent
import com.example.nyc_school.ui.components.ErrorDialog
import com.example.nyc_school.ui.components.HeaderTextComponent
import com.example.nyc_school.ui.components.TextDisplaySize
import com.example.nyc_school.ui.components.TitleTextComponent
import com.example.nyc_school.ui.components.WaitDialog
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.navigation.Screen
import com.example.nyc_school.ui.theme.customColorsPalette

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navActions: AppNavigationActions,
    schoolViewModel: SchoolViewModel = hiltViewModel()
) {
//    val context = LocalContext.current
    val apiResponse by schoolViewModel.liveData.observeAsState()
    val isRefreshing by schoolViewModel.isRefreshing.observeAsState()

    var displayError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    if (displayError/* && errorMessage.isNotEmpty()*/) {
        ErrorDialog(
            message = errorMessage,
            onClick = {
                errorMessage = ""
                displayError = false
            },
//            onDismiss = {displayError = false}
        )
    }
    var displayDialog by remember { mutableStateOf(false) }
    if (displayDialog) {
        WaitDialog()
    }

    apiResponse?.let {
        displayDialog = false
        when (it.status.code) {
            ErrorCode.SUCCESS -> {
                //display data
                DisplayData(
                    isRefreshing = isRefreshing ?: false,
                    content = it.content,
                    onItemClick = { dbn ->
                        navActions.navigateTo(
                            screen = Screen.Details,
                            arguments = mapOf(
                                "dbn" to dbn
                            )
                        )
                    },
                    onFooterClick = { letter ->
                        navActions.navigateTo(
                            screen = Screen.Category,
                            arguments = mapOf(
                                "letter" to letter
                            )
                        )
                    },
                    onRefresh = {
                        schoolViewModel.getSchoolInfo()
                    }
                )
            }

            else -> {
//                Toast.makeText(context, it.status.message, Toast.LENGTH_SHORT).show()
                LaunchedEffect(key1 = it.status, block = {
                    errorMessage = it.status.message!!
                    displayError = true
                })
            }
        }
    } ?: run {
        schoolViewModel.getSchoolInfo()
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DisplayData(
    isRefreshing: Boolean = false,
    content: List<SchoolModel>?,
    onItemClick: (String?) -> Unit,
    onFooterClick: (String) -> Unit,
    onRefresh: (String?) -> Unit
) {
    val groupedSchools = content.orEmpty().groupBy { it.schoolName?.first() }
//        .map { (letter, schools) ->
//            letter to schools.take(10)
//        }
    val ptrState =
        rememberPullRefreshState(isRefreshing, { onRefresh.invoke(null) })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(ptrState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            groupedSchools.forEach { (letter, schools) ->
                stickyHeader {
                    Header(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = letter.toString(),
                    )
                }
                items(schools.take(10)) { school ->
                    SchoolItem(
                        model = school,
                        onItemClick = { onItemClick.invoke(school.dbn) }
                    )
                }
                if (schools.size > 10) {
                    item {
                        Footer(
                            modifier = Modifier.fillMaxWidth(),
                            text = letter.toString(),
                            onClick = { onFooterClick.invoke(letter.toString()) }
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(isRefreshing, ptrState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String,
) {
    Surface(
        modifier = modifier
            .height(56.dp)
//            .background(color = MaterialTheme.customColorsPalette.onBackgroundColor),
        ,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = modifier
//                .height(56.dp)
                .background(color = MaterialTheme.customColorsPalette.onBackgroundColor)
        ) {
            HeaderTextComponent(
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                    .align(Alignment.CenterStart),
                text = text,
                textDisplaySize = TextDisplaySize.SMALL
            )
        }
    }
}

@Composable
fun Footer(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = 12.dp, bottom = 12.dp, end = 4.dp)
            .clickable {
                onClick.invoke()
            }
//            .background(color = MaterialTheme.customColorsPalette.onBackgroundColor)
    ) {
        TitleTextComponent(
            modifier = Modifier.align(Alignment.Center),
//            text = "Load More From \"$text\"..."
            text = "Load More..."
        )

        Icon(
            modifier = Modifier.align(Alignment.CenterEnd),
            imageVector = Icons.Filled.ChevronRight, contentDescription = ""
        )
    }
}

@Composable
fun SchoolItem(
    model: SchoolModel,
    onItemClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customColorsPalette.onBackgroundColor
        ),
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(10.dp),
        // modifier = modifier.size(280.dp, 240.dp)
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 6.dp)
            .clickable { onItemClick.invoke() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TitleTextComponent(
                text = model.schoolName.toString(),
                textDisplaySize = TextDisplaySize.MEDIUM
            )
            model.schoolEmail?.let {
                Spacer(modifier = Modifier.height(8.dp))
                BodyTextComponent(
                    text = it
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            BodyTextComponent(
                modifier = Modifier.align(Alignment.End),
                text =
                "${model.primaryAddressLine1.toString()}, ${model.city}, ${model.stateCode} ${model.zip}"
            )
        }
    }

}
