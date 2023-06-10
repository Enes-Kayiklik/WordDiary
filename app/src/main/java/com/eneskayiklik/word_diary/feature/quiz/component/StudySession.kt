package com.eneskayiklik.word_diary.feature.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.feature.quiz.StudyEvent
import com.eneskayiklik.word_diary.feature.quiz.StudyState
import com.eneskayiklik.word_diary.feature.quiz.WordStudyAction
import com.eneskayiklik.word_diary.feature.quiz.component.session_components.FlashcardSession
import com.eneskayiklik.word_diary.feature.quiz.component.session_components.MultipleChoiceSession
import com.eneskayiklik.word_diary.feature.quiz.component.session_components.WritingSession
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType

@Composable
fun StudySession(
    state: StudyState,
    studyType: StudyType,
    onEvent: (StudyEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (state.isTimerEnable) {
            Text(
                text = state.timerText,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleSmall
            )
        }

        when (studyType) {
            StudyType.FlashCard -> {
                FlashcardSession(
                    modifier = Modifier.weight(1F),
                    cardModifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                    items = state.words,
                    onWordSwiped = { word, direction, timeSpent ->
                        onEvent(
                            StudyEvent.OnWordStudyAction(
                                word = word,
                                direction = direction,
                                totalSpentTime = timeSpent
                            )
                        )
                    },
                    onItemVisible = { word ->
                        if (word != null && state.isVoiceEnabled) onEvent(StudyEvent.SpeakLoud(word))
                    }
                )
            }

            StudyType.Write -> state.words.firstOrNull()?.let {
                WritingSession(
                    activeWord = it,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .imePadding()
                        .weight(1F)
                        .fillMaxWidth(),
                    onWordCorrect = { word, timeSpent ->
                        onEvent(
                            StudyEvent.OnWordStudyAction(
                                word,
                                timeSpent
                            )
                        )
                    },
                    onHintTaken = { word ->
                        onEvent(
                            StudyEvent.OnWordStudyAction(
                                word = word,
                                actionType = WordStudyAction.HintTaken
                            )
                        )
                    }
                )
            }

            StudyType.MultipleChoice -> state.words.firstOrNull()?.let {
                MultipleChoiceSession(
                    activeWord = it,
                    choices = state.choices,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    onWordCorrect = { word, timeSpent ->
                        onEvent(
                            StudyEvent.OnWordStudyAction(
                                word = word,
                                totalSpentTime = timeSpent
                            )
                        )
                    },
                    onWrongAnswer = { word ->
                        onEvent(
                            StudyEvent.OnWordStudyAction(
                                word,
                                actionType = WordStudyAction.WrongAnswer
                            )
                        )
                    }
                )
            }
        }
    }
}