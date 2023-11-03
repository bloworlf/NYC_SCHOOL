package com.example.nyc_school.ui.pages.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nyc_school.data.model.SchoolModel
import com.example.nyc_school.data.network.response.ErrorCode
import com.example.nyc_school.vm.SchoolViewModel
import com.example.nyc_school.ui.components.ErrorDialog
import com.example.nyc_school.ui.components.IndicatorPosition
import com.example.nyc_school.ui.components.IndicatorType
import com.example.nyc_school.ui.components.ViewPagerColors
import com.example.nyc_school.ui.components.ViewPagerComponent
import com.example.nyc_school.ui.components.ViewPagerContent
import com.example.nyc_school.ui.components.WaitDialog
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.navigation.Screen
import com.example.nyc_school.ui.theme.customColorsPalette

@Composable
fun CategoryScreen(
    navActions: AppNavigationActions,
    schoolViewModel: SchoolViewModel = hiltViewModel(),
) {
    val apiResponse by schoolViewModel.liveData.observeAsState()

    var displayError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    if (displayError) {
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
                DisplayCategory(
                    content = it.content,
                    onItemClick = { dbn ->
                        navActions.navigateTo(
                            screen = Screen.Details,
                            arguments = mapOf(
                                "dbn" to dbn
                            )
                        )
                    },
//                    onFooterClick = { letter ->
//                        navActions.navigateTo(
//                            screen = Screen.Category,
//                            arguments = mapOf(
//                                "letter" to letter
//                            )
//                        )
//                    }
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

@Composable
fun DisplayCategory(
    content: List<SchoolModel>?,
    onItemClick: (String?) -> Unit,
) {
    val pages: MutableList<ViewPagerContent> = mutableListOf()

    val groupedSchools = content.orEmpty().groupBy { it.schoolName?.first() }
    groupedSchools.forEach { map ->
        pages.add(
            ViewPagerContent(
                title = map.key.toString(),
                content = {
                    LazyColumn(
//                        modifier = Modifier.padding(it)
                        contentPadding = it,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        items(map.value) { school ->
                            SchoolItem(
                                model = school,
                                onItemClick = {
                                    onItemClick.invoke(school.dbn)
                                }
                            )
                        }
                    }
                }
            )
        )
    }

    var selectedPage by remember { mutableIntStateOf(0) }
    ViewPagerComponent(
        modifier = Modifier.fillMaxSize(),
        scrollableIndicator = true,
        indicatorPosition = IndicatorPosition.TOP,
        indicatorType = IndicatorType.Line,
        viewPagerColors = ViewPagerColors(
            containerColor = MaterialTheme.customColorsPalette.navigationColor,
            indicatorSelectedColor = MaterialTheme.customColorsPalette.buttonContainerColor,
            indicatorColor = MaterialTheme.customColorsPalette.onBackgroundColor,
            textSelectedColor = MaterialTheme.customColorsPalette.titleTextColor,
            textColor = MaterialTheme.customColorsPalette.bodyTextColor,
        ),
        pages = pages,
        selectedPage = selectedPage,
        onPageSelected = {
//            selectedPage = it
        }
    )
}
