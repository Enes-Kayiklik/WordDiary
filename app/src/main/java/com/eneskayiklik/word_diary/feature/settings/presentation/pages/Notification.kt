package com.eneskayiklik.word_diary.feature.settings.presentation.pages

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data.permission.NotificationPermissionProvider
import com.eneskayiklik.word_diary.core.data.permission.NotificationState
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.ui.components.dialog.PermissionDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsEvent
import com.eneskayiklik.word_diary.util.extensions.openAppSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPage(
    modifier: Modifier,
    userPrefs: () -> UserPreference,
    onEvent: (SettingsEvent) -> Unit
) {
    val context = LocalContext.current

    var selectedDialog by remember { mutableStateOf(SettingsDialog.None) }

    val notificationPrefs by remember(key1 = userPrefs) { mutableStateOf(userPrefs().notification) }

    var permissionRequested by rememberSaveable { mutableStateOf(false) }
    var notificationState by rememberSaveable { mutableStateOf(NotificationState.None) }
    var isDialogShowedOnce by rememberSaveable { mutableStateOf(false) }

    val notificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        ) {
            if (it) {
                if (selectedDialog == SettingsDialog.None) onEvent(
                    SettingsEvent.UpdateNotificationEnabled(
                        notificationPrefs.isNotificationEnabled.not()
                    )
                )
                onEvent(SettingsEvent.ShowDialog(selectedDialog))
                notificationState = NotificationState.None
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
            }, icon = {
                Icon(imageVector = Icons.Outlined.NotificationsActive, contentDescription = null)
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

    fun useEvent(event: SettingsEvent, dialog: SettingsDialog) {
        isDialogShowedOnce = false
        if (notificationPermission == null || notificationPermission.status.isGranted) {
            onEvent(event)
        } else {
            selectedDialog = dialog
            notificationPermission.launchPermissionRequest()
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 16.dp
        ),
        modifier = modifier
    ) {
        item("frequency") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.notification_frequency))
                }, supportingContent = {
                    Text(text = stringResource(id = notificationPrefs.notificationFrequency.title))
                }, leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Today,
                        contentDescription = null
                    )
                }, trailingContent = {
                    Switch(
                        checked = notificationPrefs.isNotificationEnabled,
                        onCheckedChange = {
                            useEvent(
                                SettingsEvent.UpdateNotificationEnabled(notificationPrefs.isNotificationEnabled.not()),
                                SettingsDialog.None
                            )
                        }
                    )
                }, modifier = Modifier.clickable {
                    useEvent(
                        SettingsEvent.ShowDialog(SettingsDialog.NotificationFrequency),
                        SettingsDialog.NotificationFrequency
                    )
                }
            )
        }

        item("reminding_time") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.notification_time))
                }, supportingContent = {
                    Text(text = notificationPrefs.notificationTime)
                }, leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Alarm,
                        contentDescription = null
                    )
                }, modifier = Modifier.clickable {
                    useEvent(
                        SettingsEvent.ShowDialog(SettingsDialog.RemindingTime),
                        SettingsDialog.RemindingTime
                    )
                }
            )
        }
    }
}