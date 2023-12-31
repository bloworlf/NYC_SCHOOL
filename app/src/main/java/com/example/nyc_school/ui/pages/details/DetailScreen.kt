package com.example.nyc_school.ui.pages.details

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nyc_school.R
import com.example.nyc_school.data.model.SchoolModel
import com.example.nyc_school.data.model.ScoreModel
import com.example.nyc_school.data.network.response.ErrorCode
import com.example.nyc_school.vm.SchoolViewModel
import com.example.nyc_school.vm.ScoreViewModel
import com.example.nyc_school.ui.components.BodyTextComponent
import com.example.nyc_school.ui.components.ErrorDialog
import com.example.nyc_school.ui.components.Expandable
import com.example.nyc_school.ui.components.HeaderTextComponent
import com.example.nyc_school.ui.components.IndicatorPosition
import com.example.nyc_school.ui.components.IndicatorType
import com.example.nyc_school.ui.components.TextDisplaySize
import com.example.nyc_school.ui.components.TextFieldComponent
import com.example.nyc_school.ui.components.TitleTextComponent
import com.example.nyc_school.ui.components.ViewPagerColors
import com.example.nyc_school.ui.components.ViewPagerComponent
import com.example.nyc_school.ui.components.ViewPagerContent
import com.example.nyc_school.ui.components.WaitDialog
import com.example.nyc_school.ui.navigation.AppNavigationActions
import com.example.nyc_school.ui.theme.customColorsPalette
import com.example.nyc_school.utils.Utils.openMaps
import com.example.nyc_school.utils.Utils.openUrl

@Composable
fun DetailScreen(
    dbn: String,
    navActions: AppNavigationActions,
    schoolViewModel: SchoolViewModel = hiltViewModel(),
    scoreViewModel: ScoreViewModel = hiltViewModel()
) {
    val schoolApiResponse by schoolViewModel.liveData.observeAsState()
    val scoreApiResponse by scoreViewModel.liveData.observeAsState()

    var displayLoading by remember { mutableStateOf(true) }
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

    schoolApiResponse?.let {
        displayLoading = false
        when (it.status.code) {
            ErrorCode.SUCCESS -> {
                //display data
                it.content?.let { list ->
                    DisplayDetailScreen(
                        model = list[0],
                        score = scoreApiResponse?.content?.firstOrNull()
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
        schoolViewModel.getByDBN(dbn)
        scoreViewModel.getScore(dbn)
    }
}

@Composable
fun DisplayDetailScreen(
    model: SchoolModel,
    score: ScoreModel?
) {

    val pages = listOf(
        ViewPagerContent(
            title = stringResource(R.string.info),
            content = {
                InfoPage(
                    modifier = Modifier.padding(it),
                    model = model
                )
            }
        ),
        ViewPagerContent(
            title = stringResource(R.string.scores),
            content = {
                ScorePage(
                    modifier = Modifier.padding(it),
                    score = score
                )
            }
        ),
    )

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

@Composable
fun InfoPage(
    modifier: Modifier = Modifier,
    model: SchoolModel
) {
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(8.dp)
        ) {

            HeaderTextComponent(
                modifier = Modifier.fillMaxWidth(),
                text = model.schoolName ?: model.campusName ?: "",
                textDisplaySize = TextDisplaySize.SMALL,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            model.website?.let {
                BodyTextComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.openUrl(url = it)
                        },
                    text = stringResource(R.string.school_s_website),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            BodyTextComponent(
                modifier = Modifier.fillMaxWidth(),
                text = model.overviewParagraph?.replace("+", " ") ?: ""
            )
            Spacer(modifier = Modifier.height(8.dp))
            Expandable(
                enabled = false,
                title = stringResource(R.string.extracurricular_activities),
                onValueUpdated = {},
                listValues = model.extracurricularActivities?.split(","),
                content = { value, enabled, onValueUpdated ->
                    TextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        startIcon = null,
                        labelValue = value?.trim() ?: "",
                        enabled = enabled,
                        onValueChanged = onValueUpdated
                    )
                }
            )
        }

        model.latitude?.let { lat ->
            model.longitude?.let { lon ->
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    onClick = {
                        try {
                            context.openMaps(
                                lat = lat,
                                lon = lon,
                                place = model.schoolName
                            )
                        } catch (e: Exception) {
                            Toast.makeText(context, "Cannot open Maps", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                    Icon(imageVector = Icons.Filled.Map, contentDescription = "")
                }
            }
        }

    }
}

@Composable
fun ScorePage(
    modifier: Modifier = Modifier,
    score: ScoreModel?
) {
    score?.let {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TitleTextComponent(text = stringResource(R.string.num_of_sat_test_takers))
                HeaderTextComponent(text = it.numOfSatTestTakers.toString())
            }

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TitleTextComponent(text = stringResource(R.string.sat_critical_reading_avg_score))
                HeaderTextComponent(text = it.satCriticalReadingAvgScore.toString())
            }

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TitleTextComponent(text = stringResource(R.string.sat_math_avg_score))
                HeaderTextComponent(text = it.satMathAvgScore.toString())
            }

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TitleTextComponent(text = stringResource(R.string.sat_writing_avg_score))
                HeaderTextComponent(text = it.satWritingAvgScore.toString())
            }
        }
    } ?: run {
        Box(modifier = modifier.fillMaxSize()) {
            HeaderTextComponent(
                text = "No information yet",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

}