package com.eneskayiklik.word_diary.feature.settings.presentation.general.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction

@Composable
fun SwipeActionPickerDialog(
    activeAction: SwipeAction,
    onSelected: (SwipeAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Text(
                text = stringResource(id = R.string.option_swipe_action_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(24.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .heightIn(max = 192.dp)
            ) {
                SwipeAction.values().forEach { action ->
                    item(key = action) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelected(action)
                                }
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = action == activeAction,
                                onClick = { onSelected(action) }
                            )

                            Text(
                                text = stringResource(id = action.title),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}