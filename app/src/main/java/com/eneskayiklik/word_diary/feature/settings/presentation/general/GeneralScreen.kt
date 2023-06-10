package com.eneskayiklik.word_diary.feature.settings.presentation.general

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.TextIncrease
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.feature.settings.presentation.general.component.CounterView
import com.eneskayiklik.word_diary.feature.settings.presentation.general.component.SwipeActionPickerDialog
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Destination(style = ScreensAnim::class)
fun GeneralScreen(
    navigator: DestinationsNavigator,
    viewModel: GeneralViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val userPrefs = viewModel.userPrefs.collectAsState(initial = UserPreference()).value
    val personalPrefs = userPrefs.personalPrefs

    val state = viewModel.state.collectAsState().value

    when (state.activeDialog) {
        GeneralDialogType.SWIPE_ACTION_PICKER -> SwipeActionPickerDialog(
            activeAction = personalPrefs.swipeAction,
            onSelected = { viewModel.onEvent(GeneralEvent.PickSwipeAction(it)) },
            onDismiss = { viewModel.onEvent(GeneralEvent.ShowDialog(GeneralDialogType.NONE)) }
        )

        else -> Unit
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.settings_general_title),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = navigator::popBackStack) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button_desc)
                    )
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(
                vertical = 24.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            /*item("word_list_title") {
                Text(
                    text = stringResource(id = R.string.settings_word_list_section_title),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 56.dp)
                )
            }
            item("swipe_action") {
                ListItem(
                    headlineText = {
                        Text(text = stringResource(id = R.string.option_swipe_action_title))
                    },
                    supportingText = {
                        Text(text = stringResource(id = personalPrefs.swipeAction.title))
                    }, leadingContent = {
                        Icon(imageVector = Icons.Filled.SwipeLeft, contentDescription = null)
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(GeneralEvent.ShowDialog(GeneralDialogType.SWIPE_ACTION_PICKER))
                    }
                )
            }*/
            item("set_reminder") {
                ListItem(
                    headlineText = {
                        Text(text = stringResource(id = R.string.option_set_reminder_title))
                    }, supportingText = {
                        Text(text = stringResource(id = R.string.option_set_reminder_desc))
                    }, leadingContent = {
                        Icon(imageVector = Icons.Outlined.Alarm, contentDescription = null)
                    }, trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = {
                            }
                        )
                    }, modifier = Modifier.clickable {
                    }
                )
            }
            item("daily_goal_title") {
                Text(
                    text = stringResource(id = R.string.settings_daily_goal_section_title),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 56.dp)
                )
            }
            item("new_word_goal") {
                ListItem(
                    headlineText = {
                        Text(text = stringResource(id = R.string.option_new_word_title))
                    }, supportingText = {
                        Text(text = stringResource(id = R.string.option_new_word_desc))
                    }, leadingContent = {
                        Icon(imageVector = Icons.Outlined.TextIncrease, contentDescription = null)
                    }, trailingContent = {
                        CounterView(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            count = personalPrefs.newWordDailyGoal,
                            onDecrease = {
                                viewModel.onEvent(
                                    GeneralEvent.SetNewWordGoal(
                                        personalPrefs.newWordDailyGoal - 1
                                    )
                                )
                            },
                            onIncrease = {
                                viewModel.onEvent(
                                    GeneralEvent.SetNewWordGoal(
                                        personalPrefs.newWordDailyGoal + 1
                                    )
                                )
                            }
                        )
                    }
                )
            }
            item("study_session_goal") {
                ListItem(
                    headlineText = {
                        Text(text = stringResource(id = R.string.option_study_session_title))
                    }, supportingText = {
                        Text(text = stringResource(id = R.string.option_study_session_desc))
                    }, leadingContent = {
                        Box(modifier = Modifier.size(24.dp))
                    }, trailingContent = {
                        CounterView(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            count = personalPrefs.studySessionDailyGoal,
                            onDecrease = {
                                viewModel.onEvent(
                                    GeneralEvent.SetStudySessionGoal(
                                        personalPrefs.studySessionDailyGoal - 1
                                    )
                                )
                            },
                            onIncrease = {
                                viewModel.onEvent(
                                    GeneralEvent.SetStudySessionGoal(
                                        personalPrefs.studySessionDailyGoal + 1
                                    )
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}