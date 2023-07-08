package com.eneskayiklik.word_diary.feature.onboarding.presentation.component

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data.permission.NotificationPermissionProvider
import com.eneskayiklik.word_diary.core.data.permission.NotificationState
import com.eneskayiklik.word_diary.core.ui.components.dialog.PermissionDialog
import com.eneskayiklik.word_diary.util.extensions.openAppSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AlarmView(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    selectedTime: String
) {
    val context = LocalContext.current

    var permissionRequested by rememberSaveable { mutableStateOf(false) }
    var notificationState by rememberSaveable { mutableStateOf(NotificationState.None) }
    var isDialogShowedOnce by rememberSaveable { mutableStateOf(false) }

    val notificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        ) {
            if (it) {
                notificationState = NotificationState.None
                onClick()
            }
            permissionRequested = true
        } else null

    if (notificationState != NotificationState.None) {
        PermissionDialog(
            permissionTextProvider = NotificationPermissionProvider(),
            isPermanentlyDeclined = notificationState == NotificationState.Settings,
            onDismiss = {
                isDialogShowedOnce = true
                notificationState = NotificationState.None
            },
            onConfirm = {
                notificationPermission?.launchPermissionRequest()
            },
            onGoToSettings = {
                context.openAppSettings()
            }
        )
    }

    if (notificationPermission != null && permissionRequested && isDialogShowedOnce.not()) {
        when {
            notificationPermission.status.shouldShowRationale -> notificationState =
                NotificationState.Rationale

            notificationPermission.status.isGranted.not() -> notificationState =
                NotificationState.Settings
        }
    }

    Row(
        modifier = modifier
            .clickable {
                isDialogShowedOnce = false
                if (notificationPermission == null || notificationPermission.status.isGranted) {
                    onClick()
                } else {
                    notificationPermission.launchPermissionRequest()
                }
            }
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(id = R.string.notify_me),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(text = selectedTime, style = MaterialTheme.typography.bodyMedium)
        }
        Switch(
            checked = isActive,
            onCheckedChange = { value ->
                isDialogShowedOnce = false
                if (notificationPermission == null || notificationPermission.status.isGranted) {
                    onCheckedChange(value)
                } else {
                    notificationPermission.launchPermissionRequest()
                }
            }
        )
    }
}