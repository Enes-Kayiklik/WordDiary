package com.eneskayiklik.word_diary.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BasicDialog(
    onDismiss: () -> Unit,
    onDismissRequest: () -> Unit = onDismiss,
    onConfirm: (() -> Unit)? = null,
    title: String,
    description: String,
    confirmText: String? = null,
    dismissText: String,
    icon: ImageVector? = null
) {
    AlertDialog(onDismissRequest = onDismissRequest, confirmButton = {
        if (onConfirm != null && confirmText != null) {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(text = dismissText)
        }
    }, icon = {
        if (icon != null) Icon(imageVector = icon, contentDescription = null)
    }, title = {
        Text(text = title)
    }, text = {
        Text(text = description)
    })
}