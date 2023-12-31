package com.eneskayiklik.word_diary.feature.quiz.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.util.components.FilterChip
import com.eneskayiklik.word_diary.feature.quiz.StudyEvent
import com.eneskayiklik.word_diary.feature.quiz.StudyState
import com.eneskayiklik.word_diary.feature.word_list.presentation.WordListFilterType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudySettings(
    state: StudyState,
    onEvent: (StudyEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = 64.dp)) {
        item("timer") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.enable_timer))
                },
                supportingContent = {
                    Text(text = stringResource(id = R.string.enable_timer_desc))
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
        item("looping") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.enable_looping))
                },
                supportingContent = {
                    Text(text = stringResource(id = R.string.enable_looping_desc))
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
        item("shuffle") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.shuffle_words))
                },
                supportingContent = {
                    Text(text = stringResource(id = R.string.shuffle_words_desc))
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
        item("sound") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.sound_transcription))
                },
                supportingContent = {
                    Text(text = stringResource(id = R.string.sound_transcription_desc))
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
        item("filter") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.filter_by))
                },
                supportingContent = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.filter_cards_desc))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            WordListFilterType.values().forEach { item ->
                                FilterChip(
                                    isSelected = item in state.selectedFilters,
                                    content = {
                                        Text(
                                            text = stringResource(id = item.title),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    onClick = { onEvent(StudyEvent.OnFilterSelected(item)) },
                                    modifier = Modifier.clip(MaterialTheme.shapes.small)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}