package com.eneskayiklik.word_diary.feature.create_folder.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SelectEmojiView(
    selectedEmoji: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize().clickable(onClick = onClick), contentAlignment = Alignment.Center) {
            if (selectedEmoji != null) {
                Text(text = selectedEmoji, fontSize = 24.sp)
            } else {
                Icon(imageVector = Icons.Outlined.Mood, contentDescription = null)
            }
        }
    }
}