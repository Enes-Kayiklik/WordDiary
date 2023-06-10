package com.eneskayiklik.word_diary.feature.word_list.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.saket.cascade.CascadeDropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.res.stringResource
import com.eneskayiklik.word_diary.R

@Composable
fun WordListDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onFilter: () -> Unit,
    onQuiz: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    CascadeDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier,
        offset = DpOffset(x = 0.dp, y = (-24).dp)
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.study_option_title)) },
            onClick = {
                onDismiss()
                onQuiz()
            },
            leadingIcon = {
                Icon(
                    Icons.Default.HistoryEdu,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.filter_option_title)) },
            onClick = {
                onDismiss()
                onFilter()
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.FilterList,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.delete_option_title)) },
            onClick = {
                onDismiss()
                onDelete()
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.DeleteOutline,
                    contentDescription = null
                )
            }
        )
    }
}