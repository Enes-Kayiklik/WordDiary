package com.eneskayiklik.word_diary.feature.quiz.component.session_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.ui.theme.GreenYellow
import com.eneskayiklik.word_diary.core.ui.theme.SuccessGreen
import com.eneskayiklik.word_diary.util.extensions.takeHint

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WritingSession(
    activeWord: WordEntity,
    onWordCorrect: (word: WordEntity, timeSpent: Long) -> Unit,
    onHintTaken: (WordEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var initialTime by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textInputValue by remember { mutableStateOf(TextFieldValue()) }
    val isInputValid by remember(key1 = activeWord) {
        derivedStateOf {
            textInputValue.text.trim().contains(activeWord.word, true)
        }
    }
    var hintTaken by remember { mutableStateOf(false) }

    val inputColorAnim by animateColorAsState(
        targetValue = when {
            isInputValid -> SuccessGreen
            hintTaken -> GreenYellow
            else -> MaterialTheme.colorScheme.background
        },
        animationSpec = tween(durationMillis = 300),
        finishedListener = {
            if (hintTaken) hintTaken = false
            if (isInputValid) onWordCorrect(activeWord, System.currentTimeMillis() - initialTime)
        }
    )

    LaunchedEffect(key1 = isInputValid) {
        try {
            focusRequester.requestFocus()
            keyboardController?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(key1 = activeWord) {
        textInputValue = TextFieldValue()
        initialTime = System.currentTimeMillis()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = activeWord.meaning,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Divider(modifier = Modifier.fillMaxWidth())
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = textInputValue,
            onValueChange = {
                if (isInputValid.not()) textInputValue = it
            },
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(inputColorAnim)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )
        TextButton(onClick = {
            if (isInputValid.not()) {
                val newValue = textInputValue.text.takeHint(activeWord.word)
                textInputValue = textInputValue.copy(
                    text = newValue,
                    selection = TextRange(newValue.length)
                )
                hintTaken = true
                onHintTaken(activeWord)
            }
        }) {
            Text(text = "Hint")
        }
    }
}