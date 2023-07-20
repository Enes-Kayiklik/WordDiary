package com.eneskayiklik.word_diary.feature.quiz

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.components.BasicDialog
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.quiz.component.StudyResultView
import com.eneskayiklik.word_diary.feature.quiz.component.StudySession
import com.eneskayiklik.word_diary.feature.quiz.component.StudySettings
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

data class StudyScreenNavArgs(
    val folderId: Int,
    val studyType: StudyType
)

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)
@Destination(style = ScreensAnim::class, navArgsDelegate = StudyScreenNavArgs::class)
@Composable
fun StudyScreen(
    navigator: DestinationsNavigator,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = viewModel.state.collectAsState().value
    var isFilterMenuVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler(isFilterMenuVisible || state.quizState == QuizState.Started) {
        isFilterMenuVisible = false
        if (state.quizState == QuizState.Started) viewModel.onEvent(
            StudyEvent.ShowDialog(
                StudyDialogType.Quit
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is UiEvent.OnNavigate -> navigator.navigate(it.route)
                is UiEvent.ShowToast -> if (it.textRes != null) {
                    Toast.makeText(context, it.textRes, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, it.text ?: "", Toast.LENGTH_LONG).show()
                }

                UiEvent.ClearBackstack -> navigator.navigateUp()
                else -> Unit
            }
        }
    }


    if (state.isDialogActive) {
        when (state.dialogType) {
            StudyDialogType.Quit -> BasicDialog(
                onDismiss = {
                    viewModel.onEvent(StudyEvent.ShowDialog(StudyDialogType.None))
                    navigator.popBackStack()
                },
                onConfirm = {
                    viewModel.onEvent(StudyEvent.ShowDialog(StudyDialogType.None))
                    viewModel.onEvent(StudyEvent.FinishStudy)
                },
                onDismissRequest = {
                    viewModel.onEvent(StudyEvent.ShowDialog(StudyDialogType.None))
                },
                title = stringResource(id = R.string.finish_study_dialog),
                description = stringResource(id = R.string.finish_study_dialog_desc),
                confirmText = stringResource(id = R.string.see_results),
                dismissText = stringResource(id = R.string.finish_without_saving)
            )

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = when (state.quizState) {
                        QuizState.Initial -> stringResource(id = R.string.study_settings)
                        QuizState.Finished -> "Well done!"
                        else -> ""
                    },
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    if (state.quizState == QuizState.Started) viewModel.onEvent(
                        StudyEvent.ShowDialog(
                            StudyDialogType.Quit
                        )
                    ) else navigator.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
            }, actions = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null
                    )
                }
            }, scrollBehavior = if (state.quizState != QuizState.Started) scrollBehavior else null)
        }
    ) { padding ->
        AnimatedContent(targetState = state.quizState, transitionSpec = {
            slideInHorizontally { it } with slideOutHorizontally { -it }
        }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (it) {
                    QuizState.Initial -> {
                        StudySettings(
                            state = state,
                            onEvent = viewModel::onEvent,
                            modifier = Modifier.fillMaxSize()
                        )
                        Button(
                            onClick = { viewModel.onEvent(StudyEvent.StartStudy) },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.BottomCenter),
                            enabled = state.isStartActive
                        ) {
                            Text(text = stringResource(id = R.string.start_study))
                            Text(text = "(${state.currentTotal} words)")
                        }
                    }

                    QuizState.Started -> StudySession(
                        state = state,
                        onEvent = viewModel::onEvent
                    )

                    QuizState.Finished -> StudyResultView(
                        modifier = Modifier.fillMaxSize(),
                        resultState = state.studyResult
                    )
                }
            }
        }
    }
}