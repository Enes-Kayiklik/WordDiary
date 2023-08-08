package com.eneskayiklik.word_diary.feature.settings.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsEvent
import com.eneskayiklik.word_diary.feature.settings.presentation.component.CounterView

@Composable
fun GeneralPage(
    modifier: Modifier,
    userPrefs: () -> UserPreference,
    onEvent: (SettingsEvent) -> Unit
) {
    val prefs by remember(key1 = userPrefs) { mutableStateOf(userPrefs()) }

    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 16.dp
        ),
        modifier = modifier
    ) {
        /*item("set_reminder") {
            AlarmView(
                isActive = true/*userPrefs.notification.isNotificationEnabled*/,
                selectedTime = stringResource(
                    id = R.string.notify_me_at,
                    "15:40"
                    //userPrefs.notification.notificationTime
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                onCheckedChange = { /*e -> viewModel.onEvent(GeneralEvent.EnableAlarm(e))*/ },
                onClick = { /*isClockPickerVisible = isClockPickerVisible.not()*/ }
            )
        }*/
        item("user_language") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.user_language))
                }, supportingContent = {
                    Text(text = prefs.userLanguage.readable)
                }, leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Translate,
                        contentDescription = null
                    )
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.ShowDialog(SettingsDialog.SelectMotherLanguage))
                }
            )
        }

        item("app_language") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.app_language))
                }, supportingContent = {
                    Text(text = prefs.appLanguage.readable)
                }, leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Language,
                        contentDescription = null
                    )
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.ShowDialog(SettingsDialog.SelectAppLanguage))
                }
            )
        }

        item("daily_goal_title") {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.daily_goal),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(start = 56.dp)
            )
        }

        item("new_word_goal") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.new_word))
                }, supportingContent = {
                    Text(text = stringResource(id = R.string.new_word_desc))
                }, leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.AddCircleOutline,
                        contentDescription = null
                    )
                }, trailingContent = {
                    CounterView(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        count = prefs.personalPrefs.newWordDailyGoal,
                        onDecrease = {
                            onEvent(
                                SettingsEvent.UpdateNewWordGoal(
                                    prefs.personalPrefs.newWordDailyGoal - 1
                                )
                            )
                        },
                        onIncrease = {
                            onEvent(
                                SettingsEvent.UpdateNewWordGoal(
                                    prefs.personalPrefs.newWordDailyGoal + 1
                                )
                            )
                        }
                    )
                }
            )
        }

        item("study_session_goal") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.study_session))
                }, supportingContent = {
                    Text(text = stringResource(id = R.string.study_session_desc))
                }, leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = null
                    )
                }, trailingContent = {
                    CounterView(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        count = prefs.personalPrefs.studySessionDailyGoal,
                        onDecrease = {
                            onEvent(
                                SettingsEvent.UpdateSessionGoal(
                                    prefs.personalPrefs.studySessionDailyGoal - 1
                                )
                            )
                        },
                        onIncrease = {
                            onEvent(
                                SettingsEvent.UpdateSessionGoal(
                                    prefs.personalPrefs.studySessionDailyGoal + 1
                                )
                            )
                        }
                    )
                }
            )
        }
    }
}