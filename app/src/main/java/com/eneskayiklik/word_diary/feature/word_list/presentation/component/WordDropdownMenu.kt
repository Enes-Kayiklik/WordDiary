package com.eneskayiklik.word_diary.feature.word_list.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.saket.cascade.CascadeDropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.unit.Dp
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction

@Composable
fun WordDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAction: (SwipeAction) -> Unit,
    modifier: Modifier = Modifier,
    fixedWidth: Dp = 196.dp
) {
    CascadeDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier,
        fixedWidth = fixedWidth
    ) {
        SwipeAction.values().forEach { action ->
            val icon = when (action) {
                SwipeAction.EDIT_WORD -> Icons.Outlined.Edit
                SwipeAction.DELETE_WORD -> Icons.Outlined.Delete
                else -> return@forEach
            }
            val title = stringResource(action.actionDesc)
            DropdownMenuItem(
                text = { Text(title) },
                onClick = {
                    onDismiss()
                    onAction(action)
                },
                leadingIcon = {
                    Icon(
                        icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}