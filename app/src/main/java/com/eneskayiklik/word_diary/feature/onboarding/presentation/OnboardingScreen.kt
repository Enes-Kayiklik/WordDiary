package com.eneskayiklik.word_diary.feature.onboarding.presentation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.ui.components.PageIndicator
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.core.util.popSlideOutAnim
import com.eneskayiklik.word_diary.core.util.slideInAnim
import com.eneskayiklik.word_diary.feature.onboarding.presentation.pages.AlarmPage
import com.eneskayiklik.word_diary.feature.onboarding.presentation.pages.LanguageSelectPage
import com.eneskayiklik.word_diary.feature.onboarding.presentation.pages.TextToSpeechPage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
@Destination(style = ScreensAnim::class)
fun OnboardingScreen(
    navigator: DestinationsNavigator,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val userPrefs = viewModel.userPrefs.collectAsState(initial = UserPreference()).value
    val pagerState = rememberPagerState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isPagerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                UiEvent.ClearBackstack -> navigator.popBackStack()
                is UiEvent.ShowToast -> if (it.textRes != null) {
                    Toast.makeText(context, it.textRes, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }

    fun backButtonCallback() {
        try {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    maxOf(0, pagerState.currentPage - 1),
                    animationSpec = tween(400)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    BackHandler(userPrefs.showOnboarding, onBack = ::backButtonCallback)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = isPagerVisible,
                enter = slideInAnim(),
                exit = popSlideOutAnim()
            ) {
                BottomAppBar(
                    actions = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = pagerState.currentPage != 0,
                                modifier = Modifier.align(Alignment.CenterStart),
                                enter = expandHorizontally(),
                                exit = shrinkHorizontally()
                            ) {
                                TextButton(
                                    onClick = ::backButtonCallback
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ChevronLeft,
                                        contentDescription = null
                                    )
                                    Text(text = stringResource(id = R.string.previous))
                                }
                            }

                            PageIndicator(
                                numberOfPages = state.pages.size,
                                selectedPage = pagerState.currentPage,
                                modifier = Modifier.align(Alignment.Center)
                            )
                            TextButton(
                                onClick = {
                                    if (pagerState.currentPage == state.pages.size - 1) {
                                        viewModel.finishOnboarding()
                                    } else {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(
                                                pagerState.currentPage + 1,
                                                animationSpec = tween(400)
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                if (pagerState.currentPage == state.pages.size - 1) {
                                    Text(text = stringResource(id = R.string.finish))
                                } else {
                                    Text(text = stringResource(id = R.string.next))
                                }
                                Icon(
                                    imageVector = Icons.Outlined.ChevronRight,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_evaluation),
                contentDescription = null,
                modifier = Modifier.size(148.dp)
            )

            Text(
                text = stringResource(id = R.string.onboarding_title),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = stringResource(id = R.string.onboarding_desc),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            FloatingActionButton(onClick = { isPagerVisible = true }) {
                Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null)
            }
        }

        AnimatedVisibility(
            visible = isPagerVisible,
            enter = slideInAnim(),
            exit = popSlideOutAnim()
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                pageCount = state.pages.size,
                state = pagerState
            ) { currentPage ->
                when (state.pages[currentPage]) {
                    OnboardingPage.LanguageSelect -> LanguageSelectPage(
                        modifier = Modifier.fillMaxSize(),
                        userLanguage = userPrefs.userLanguage,
                        selectUserLanguage = viewModel::selectUserLanguage,
                        onEvent = viewModel::onEvent
                    )

                    OnboardingPage.TextToSpeechSetup -> TextToSpeechPage(
                        modifier = Modifier.fillMaxSize()
                    )

                    OnboardingPage.AlarmSetup -> AlarmPage(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(bottom = 80.dp, start = 16.dp, end = 16.dp),
                        notificationSettings = userPrefs.notification,
                        setAlarm = viewModel::setAlarm,
                        onCheckedChange = viewModel::enableAlarm
                    )
                }
            }
        }
    }
}