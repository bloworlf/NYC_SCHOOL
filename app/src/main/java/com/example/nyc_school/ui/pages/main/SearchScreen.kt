package com.example.nyc_school.ui.pages.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nyc_school.data.model.school.SchoolModel
import com.example.nyc_school.data.network.response.ErrorCode
import com.example.nyc_school.data.vm.SchoolViewModel
import com.example.nyc_school.ui.components.ErrorDialog
import com.example.nyc_school.ui.components.SearchBar
import com.example.nyc_school.ui.components.WaitDialog
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.pages.details.DisplayDetailScreen

@Composable
fun SearchScreen(
    navActions: AppNavigationActions,
    schoolViewModel: SchoolViewModel = hiltViewModel()
) {

    val apiResponse by schoolViewModel.liveData.observeAsState()
    var displayLoading by remember { mutableStateOf(false) }
    if (displayLoading) {
        WaitDialog()
    }

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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.End),
            onSearchQueryChanged = {},
            onSearchQuery = {
                if (it.length < 3) return@SearchBar

                displayLoading = true
                schoolViewModel.getByName(it)
            }
        )

        apiResponse?.let {
            displayLoading = false
            when (it.status.code) {
                ErrorCode.SUCCESS -> {
                    //display data
                    it.content?.let { list ->
                        DisplayData(
                            content = list,
                            onItemClick = {},
                            onFooterClick = {},
                            onRefresh = {}
                        )
                    }
                }

                else -> {
                    LaunchedEffect(key1 = it.status, block = {
                        errorMessage = it.status.message!!
                        displayError = true
                    })
                }
            }
        } ?: run {

        }
    }

}
