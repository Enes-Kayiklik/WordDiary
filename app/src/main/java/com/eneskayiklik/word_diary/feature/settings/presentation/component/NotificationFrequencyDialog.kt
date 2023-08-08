package com.eneskayiklik.word_diary.feature.settings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.NotificationFrequency

@Composable
fun NotificationFrequencyDialog(
    activeFrequency: NotificationFrequency,
    onSelected: (NotificationFrequency) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_theme),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 24.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            NotificationFrequency.values().forEach { frequency ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelected(frequency)
                        }
                        .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = frequency == activeFrequency,
                        onClick = { onSelected(frequency) }
                    )

                    Text(
                        text = stringResource(id = frequency.title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.dialog_ok))
            }
        }
    }
}