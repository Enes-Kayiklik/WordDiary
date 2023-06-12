package com.eneskayiklik.word_diary.feature.folder_list.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount
import com.eneskayiklik.word_diary.feature.word_list.presentation.component.WordDropdownMenu

@Composable
fun SingleFolderRow(
    folderWithCount: FolderWithWordCount,
    modifier: Modifier = Modifier
) {
    val folder = folderWithCount.folder
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = folder.emoji ?: "", style = MaterialTheme.typography.titleLarge)
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val supportingText = when {
                folderWithCount.wordCount == 1 -> stringResource(
                    id = R.string.list_item_word_count,
                    folderWithCount.wordCount
                )

                folderWithCount.wordCount > 1 -> stringResource(
                    id = R.string.list_item_word_counts,
                    folderWithCount.wordCount
                )

                else -> stringResource(id = R.string.empty_folder)
            }
            Text(
                text = folder.title,
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_feather),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(folder.color)
                )
                Text(text = supportingText, style = MaterialTheme.typography.bodyLarge)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = "Add word")
                }
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = "Practise")
                }
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
                onAction = { },
                fixedWidth = 144.dp
            )
        }
    }
}