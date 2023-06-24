package com.eneskayiklik.word_diary.feature.word_list.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.database.entity.WordEntity

@Composable
fun WordListItem(
    word: WordEntity,
    modifier: Modifier = Modifier,
    onAction: (SwipeAction) -> Unit,
    onClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(modifier = Modifier.weight(1F), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = (word.proficiency / 100).toFloat(),
                        modifier = Modifier.size(44.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(.3F),
                        strokeWidth = 4.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${word.proficiency.toInt()}%",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Column(
                    modifier = Modifier.weight(1F)
                ) {
                    Text(
                        text = word.meaning,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        }, indication = rememberRipple(bounded = false),
                        onClick = { onAction(SwipeAction.SPEECH_LOUD) }
                    ), tint = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (word.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        }, indication = rememberRipple(bounded = false),
                        onClick = { onAction(SwipeAction.ADD_FAVORITES) }
                    ), tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Box {
            Icon(imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = { isMenuExpanded = isMenuExpanded.not() }
                )
            )

            WordDropdownMenu(
                expanded = isMenuExpanded,
                onDismiss = { isMenuExpanded = false },
                onAction = onAction,
                fixedWidth = 144.dp
            )
        }
    }
}