package com.eneskayiklik.word_diary.core.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data.permission.PermissionProvider

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onGoToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                if (isPermanentlyDeclined) onGoToSettings() else onConfirm()
            }) {
                Text(
                    text = if (isPermanentlyDeclined) stringResource(id = R.string.dialog_grant_permission)
                    else stringResource(id = R.string.dialog_ok)
                )
            }
        }, dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.dialog_cancel)
                )
            }
        },
        title = {
            Text(text = stringResource(id = R.string.permission_required))
        },
        text = {
            Text(
                text = stringResource(
                    id = permissionTextProvider.getDescription(
                        isPermanentlyDeclined = isPermanentlyDeclined
                    )
                )
            )
        },
        modifier = modifier
    )
}