package com.eneskayiklik.word_diary.feature.quiz.component.session_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.eneskayiklik.word_diary.core.ui.theme.GreenYellow
import com.eneskayiklik.word_diary.core.ui.theme.SuccessGreen

@Composable
fun MultipleChoiceSession(
    activeWord: WordEntity,
    choices: List<WordEntity>,
    onWordCorrect: (word: WordEntity, timeSpent: Long) -> Unit,
    onWrongAnswer: (WordEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var initialTime by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

    var canSelectChoice by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = activeWord) {
        canSelectChoice = true
        initialTime = System.currentTimeMillis()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = activeWord.word,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Column(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            choices.forEach { choice ->
                var isCorrectAnswer by remember { mutableStateOf<Boolean?>(null) }

                val inputColorAnim by animateColorAsState(
                    targetValue = when (isCorrectAnswer) {
                        true -> SuccessGreen
                        false -> GreenYellow
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    },
                    animationSpec = tween(durationMillis = 400),
                    finishedListener = {
                        if (isCorrectAnswer == true) {
                            onWordCorrect(activeWord, System.currentTimeMillis() - initialTime)
                        }
                        isCorrectAnswer = null
                    }
                )

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(inputColorAnim)
                        .clickable(
                            enabled = canSelectChoice,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            isCorrectAnswer = activeWord == choice
                            canSelectChoice = isCorrectAnswer != true
                            if (isCorrectAnswer != true) onWrongAnswer(activeWord)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = choice.meaning,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}