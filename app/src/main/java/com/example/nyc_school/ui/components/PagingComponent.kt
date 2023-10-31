package com.example.nyc_school.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

sealed class IndicatorType {
    object Dot : IndicatorType()
    object Line : IndicatorType()
    object Number : IndicatorType()
    object None : IndicatorType()
}

enum class IndicatorPosition {
    TOP,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
}

enum class TitleDisplay {
    ALL,
    SELECTED,
    NONE
}

data class ViewPagerContent(
    val title: String? = null,
    val content: @Composable (PaddingValues) -> Unit
)

data class ViewPagerColors(
    val containerColor: Color = Color.White,
    val indicatorSelectedColor: Color = Color.White,
    val indicatorColor: Color = Color.LightGray,
    val textSelectedColor: Color = Color.Black,
    val textColor: Color = Color.LightGray,
    val iconSelectedColor: Color = Color.Black,
    val iconColor: Color = Color.LightGray,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPagerComponent(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    viewPagerColors: ViewPagerColors = ViewPagerColors(),
    titleDisplay: TitleDisplay = TitleDisplay.SELECTED,
//    displayTitle: Boolean = true,
//    displayIndicator: Boolean = true,
    scrollableIndicator: Boolean = false,
    indicatorType: IndicatorType = IndicatorType.Line,
    indicatorPosition: IndicatorPosition = IndicatorPosition.BOTTOM,
    animated: Boolean = true,
    pages: List<ViewPagerContent>,
    selectedPage: Int = 0,
    onPageSelected: (Int) -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()
    //needs the number of pages
    val pagerState = rememberPagerState(pageCount = { pages.size })
    LaunchedEffect(
        key1 = pagerState.currentPage,
        block = { onPageSelected.invoke(pagerState.currentPage) }
    )
    LaunchedEffect(
        key1 = selectedPage,
        block = {
            if (selectedPage != pagerState.currentPage) {
                coroutine.launch {
                    pagerState.scrollToPage(selectedPage)
                }
            }
        }
    )

    var innerPadding by remember {
        mutableStateOf(
            PaddingValues(
                top = 8.dp,
                end = 8.dp,
                bottom = 8.dp,
                start = 8.dp
            )
        )
    }

    Box(
        modifier = modifier
//            .background(color = viewPagerColors.containerColor)
    ) {
        HorizontalPager(
            userScrollEnabled = enabled,
            modifier = Modifier.fillMaxSize().align(Alignment.TopCenter),
            state = pagerState
        ) { page ->
            pages[page].content.invoke(innerPadding)
        }

        //indicator
        IndicatorComponent(
            modifier = Modifier.fillMaxWidth(),
            scrollableIndicator = scrollableIndicator,
            viewPagerColors = viewPagerColors,
            indicatorPosition = indicatorPosition,
            indicatorType = indicatorType,
            titleDisplay = titleDisplay,
            title = pages[pagerState.currentPage].title,
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
            animated = animated,
            navigateTo = {
                coroutine.launch {
                    pagerState.animateScrollToPage(page = it)
                }
            },
            innerPadding = {
                innerPadding = it
            }
        )
    }
}

@Composable
fun BoxScope.IndicatorComponent(
    modifier: Modifier = Modifier,
    indicatorType: IndicatorType,
    indicatorPosition: IndicatorPosition,
    scrollableIndicator: Boolean,
    viewPagerColors: ViewPagerColors,
    title: String?,
    titleDisplay: TitleDisplay,
    pageCount: Int,
    currentPage: Int,
    animated: Boolean,
    navigateTo: (Int) -> Unit,
    innerPadding: (PaddingValues) -> Unit
) {
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .align(
                when (indicatorPosition) {
                    IndicatorPosition.TOP -> {
                        Alignment.TopCenter
                    }

                    IndicatorPosition.TOP_LEFT -> {
                        Alignment.TopStart
                    }

                    IndicatorPosition.TOP_RIGHT -> {
                        Alignment.TopEnd
                    }

                    IndicatorPosition.BOTTOM -> {
                        Alignment.BottomCenter
                    }

                    IndicatorPosition.BOTTOM_LEFT -> {
                        Alignment.BottomStart
                    }

                    IndicatorPosition.BOTTOM_RIGHT -> {
                        Alignment.BottomEnd
                    }
                }
            )
            .onGloballyPositioned { coordinates ->
                innerPadding.invoke(with(density) {
                    PaddingValues(
                        top = coordinates.size.height.toDp(),
                        end = 8.dp,
                        bottom = 8.dp,
                        start = 8.dp
                    )
                })
            }
    ) {
        when (indicatorType) {
            IndicatorType.Line -> {
                LinePagerIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    scrollableIndicator = scrollableIndicator,
                    viewPagerColors = viewPagerColors,
                    indicatorPosition = indicatorPosition,
                    titleDisplay = titleDisplay,
                    title = title,
                    pageCount = pageCount,
                    currentPage = currentPage,
                    animated = animated,
                    navigateTo = navigateTo
                )
            }

            IndicatorType.Dot -> {
                CirclePagerIndicator(
                    viewPagerColors = viewPagerColors,
                    titleDisplay = titleDisplay,
                    title = title,
                    pageCount = pageCount,
                    currentPage = currentPage,
                    animated = animated
                )
            }

            IndicatorType.Number -> {
                NumberPagerIndicator(
                    viewPagerColors = viewPagerColors,
                    pageCount = pageCount,
                    currentPage = currentPage,
                    animated = animated
                )
            }

            IndicatorType.None -> {}
        }
    }
}

@Composable
fun LinePagerIndicator(
    modifier: Modifier = Modifier,
    scrollableIndicator: Boolean,
    indicatorPosition: IndicatorPosition,
    viewPagerColors: ViewPagerColors,
    pageCount: Int,
    currentPage: Int,
    title: String? = null,
    titleDisplay: TitleDisplay = if (title.isNullOrEmpty()) TitleDisplay.NONE else TitleDisplay.ALL,
    animated: Boolean = true,
    navigateTo: (Int) -> Unit
) {
//    val scrollState = rememberScrollState()
//    if (scrollableIndicator) {
//        LazyRow(
//            modifier = Modifier
//            .height(50.dp)
//            .fillMaxWidth()
//            .background(color = viewPagerColors.containerColor),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.Bottom
//        ){
//
//        }
//    } else {
//
//    }
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(color = viewPagerColors.containerColor),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(pageCount) { iteration ->
            val textOffset by animateFloatAsState(
                targetValue = if (currentPage == iteration) 0f else 50f, // Adjust the value as needed
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = ""
            )
            val textAlpha by animateFloatAsState(
                targetValue = if (currentPage == iteration) 1f else 0f,
                animationSpec = tween(
                    durationMillis = if (currentPage == iteration) 500 else 125,
                    easing = if (currentPage == iteration) EaseInOut else FastOutSlowInEasing
                ),
                label = ""
            )
            val lineWeight = animateFloatAsState(
                targetValue = if (animated) {
                    if (currentPage == iteration) {
                        1.5f
//                            pageCount / 2.67f
                    } else {
                        if (iteration < currentPage) {
                            0.5f
                        } else {
                            1f
                        }
                    }
                } else {
                    1f
                }, label = "weight", animationSpec = tween(300, easing = EaseInOut)
            )
            val color =
                if (currentPage == iteration) viewPagerColors.indicatorSelectedColor else viewPagerColors.indicatorColor

            Column(
                modifier = Modifier
                    .weight(lineWeight.value)
                    .clickable {
                        navigateTo.invoke(iteration)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let {
                    Text(
                        color = viewPagerColors.textSelectedColor,
                        text = it,
                        maxLines = 1,
                        softWrap = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(
                                y = when (titleDisplay) {
                                    TitleDisplay.ALL -> {
                                        0.dp
                                    }

                                    TitleDisplay.SELECTED -> {
                                        textOffset.dp
                                    }

                                    TitleDisplay.NONE -> {
                                        50.dp
                                    }
                                }
                            )
                            .alpha(
                                alpha = when (titleDisplay) {
                                    TitleDisplay.ALL -> {
                                        1f
                                    }

                                    TitleDisplay.SELECTED -> {
                                        textAlpha
                                    }

                                    TitleDisplay.NONE -> {
                                        0f
                                    }
                                }
                            ), // Fade in/out effect
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                            .width(50.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
//                    .weight(if (animated) lineWeight.value else 1f)
                        .height(4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CirclePagerIndicator(
    modifier: Modifier = Modifier,
    viewPagerColors: ViewPagerColors,
    pageCount: Int,
    currentPage: Int,
    title: String? = null,
    titleDisplay: TitleDisplay = if (title.isNullOrEmpty()) TitleDisplay.NONE else TitleDisplay.ALL,
    animated: Boolean = true,
) {
    Column(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(color = viewPagerColors.containerColor),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (titleDisplay) {
            TitleDisplay.ALL,
            TitleDisplay.SELECTED -> {
                title?.let {
                    AnimatedContent(
                        targetState = it,
                        transitionSpec = {
                            EnterTransition.None togetherWith ExitTransition.None
                        },
                        label = ""
                    ) { targetValue ->
                        Text(
                            color = viewPagerColors.textSelectedColor,
                            text = targetValue,
                            maxLines = 1,
                            softWrap = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateEnterExit(
                                    enter = scaleIn(),
                                    exit = scaleOut()
                                ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            TitleDisplay.NONE -> {}
        }
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { iteration ->
                val circleSize = animateDpAsState(
                    targetValue = when (currentPage) {
                        iteration -> {
                            16.dp
                        }

                        iteration + 1, iteration - 1 -> {
                            12.dp
                        }

                        else -> {
                            8.dp
                        }
                    }, label = "weight", animationSpec = tween(300, easing = EaseInOut)
                )
                val color =
                    if (currentPage == iteration) viewPagerColors.indicatorSelectedColor else viewPagerColors.indicatorColor
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(if (animated) circleSize.value else 16.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NumberPagerIndicator(
    modifier: Modifier = Modifier,
    viewPagerColors: ViewPagerColors,
    pageCount: Int,
    currentPage: Int,
    animated: Boolean = true,
) {
    Column(
        modifier = modifier
            .height(50.dp)
            .background(color = viewPagerColors.containerColor)
//            .fillMaxWidth()
        ,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (animated) {
            AnimatedContent(
                targetState = currentPage + 1,
                transitionSpec = {
                    EnterTransition.None togetherWith ExitTransition.None
                },
                label = ""
            ) { targetValue ->
                Text(
                    color = viewPagerColors.textSelectedColor,
                    text = "$targetValue",
                    maxLines = 1,
                    softWrap = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateEnterExit(
                            enter = scaleIn(),
                            exit = scaleOut()
                        ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                color = viewPagerColors.textSelectedColor,
                text = "${currentPage + 1}",
                maxLines = 1,
                softWrap = true,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}