package com.eneskayiklik.word_diary.feature.quiz.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.quiz.StudyEvent
import com.eneskayiklik.word_diary.feature.quiz.StudyState

@Composable
fun StudySettings(
    state: StudyState,
    onEvent: (StudyEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = 64.dp)) {
        item {
            ListItem(
                headlineText = {
                    Text(text = stringResource(id = R.string.study_settings_timer_title))
                },
                supportingText = {
                    Text(text = stringResource(id = R.string.study_settings_timer_desc))
                }, trailingContent = {
                    Switch(
                        checked = state.isTimerEnable,
                        onCheckedChange = { onEvent(StudyEvent.OnTimer) }
                    )
                }, modifier = Modifier.clickable {
                    onEvent(StudyEvent.OnTimer)
                }
            )
        }
        item {
            ListItem(
                headlineText = {
                    Text(text = stringResource(id = R.string.study_settings_looping_title))
                },
                supportingText = {
                    Text(text = stringResource(id = R.string.study_settings_looping_desc))
                }, trailingContent = {
                    Switch(
                        checked = state.isLoopingEnable,
                        onCheckedChange = { onEvent(StudyEvent.OnLooping) }
                    )
                }, modifier = Modifier.clickable {
                    onEvent(StudyEvent.OnLooping)
                }
            )
        }
        item {
            ListItem(
                headlineText = {
                    Text(text = stringResource(id = R.string.study_settings_shuffle_title))
                },
                supportingText = {
                    Text(text = stringResource(id = R.string.study_settings_shuffle_desc))
                }, trailingContent = {
                    Switch(
                        checked = state.isShuffleEnable,
                        onCheckedChange = { onEvent(StudyEvent.OnShuffle) }
                    )
                }, modifier = Modifier.clickable {
                    onEvent(StudyEvent.OnShuffle)
                }
            )
        }
        item {
            ListItem(
                headlineText = {
                    Text(text = stringResource(id = R.string.study_settings_sound_title))
                },
                supportingText = {
                    Text(text = stringResource(id = R.string.study_settings_sound_desc))
                }, trailingContent = {
                    Switch(
                        checked = state.isVoiceEnabled,
                        onCheckedChange = { onEvent(StudyEvent.OnSound) }
                    )
                }, modifier = Modifier.clickable {
                    onEvent(StudyEvent.OnSound)
                }
            )
        }
    }
}