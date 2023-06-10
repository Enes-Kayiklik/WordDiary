package com.eneskayiklik.word_diary.feature.settings.presentation.about.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutCard(
    cardTitle: String,
    modifier: Modifier = Modifier,
    sectionTitle: (@Composable () -> Unit)? = null,
    sectionDescription: (@Composable () -> Unit)? = null,
    sectionIcon: (@Composable () -> Unit)? = null,
    actionContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cardTitle,
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        )
        if (sectionIcon != null) {
            Spacer(modifier = Modifier.height(16.dp))
            sectionIcon()
        }
        if (sectionTitle != null) {
            Spacer(modifier = Modifier.height(16.dp))
            sectionTitle()
        }
        if (sectionDescription != null) {
            Spacer(modifier = Modifier.height(8.dp))
            sectionDescription()
        }
        if (actionContent != null) {
            Spacer(modifier = Modifier.height(16.dp))
            actionContent()
        }
    }
}