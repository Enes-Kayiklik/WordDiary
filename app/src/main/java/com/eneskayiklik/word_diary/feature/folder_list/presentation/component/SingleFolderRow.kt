package com.eneskayiklik.word_diary.feature.folder_list.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount

@Composable
fun SingleFolderRow(
    folderWithCount: FolderWithWordCount,
    modifier: Modifier = Modifier
) {
    val folder = folderWithCount.folder

    ListItem(
        headlineText = {
            Text(text = folder.title)
        }, supportingText = {
            if (folderWithCount.wordCount == 1) {
                Text(
                    text = stringResource(
                        id = R.string.list_item_word_count,
                        folderWithCount.wordCount
                    )
                )
            } else if (folderWithCount.wordCount > 1) {
                Text(
                    text = stringResource(
                        id = R.string.list_item_word_counts,
                        folderWithCount.wordCount
                    )
                )
            } else {
                Text(text = stringResource(id = R.string.empty_folder))
            }
        }, modifier = modifier, trailingContent = {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(16.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                trackColor = MaterialTheme.colorScheme.primary.copy(.4F),
                progress = .5F
            )
        }, leadingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_folder),
                contentDescription = null,
                tint = Color(folder.color)
            )
        }
    )
}