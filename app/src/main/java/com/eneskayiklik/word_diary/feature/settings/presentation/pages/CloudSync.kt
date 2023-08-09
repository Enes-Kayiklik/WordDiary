package com.eneskayiklik.word_diary.feature.settings.presentation.pages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.feature.destinations.BackupScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.PaywallScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.presentation.UserData
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsEvent
import com.eneskayiklik.word_diary.feature.settings.presentation.component.DriveConfigView
import com.eneskayiklik.word_diary.util.contract.GoogleLoginContract
import com.google.android.gms.common.api.ApiException
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CloudSyncPage(
    modifier: Modifier,
    userData: () -> UserData,
    isDriveBackingUp: () -> Boolean,
    navigator: DestinationsNavigator,
    onEvent: (SettingsEvent) -> Unit
) {

    val backupCreator = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip"),
        onResult = { onEvent(SettingsEvent.CreateBackup(it)) }
    )

    val googleLoginLauncher = rememberLauncherForActivityResult(GoogleLoginContract()) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)
            onEvent(
                SettingsEvent.OnGoogleLogin(
                    account ?: return@rememberLauncherForActivityResult
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 16.dp
        ),
        modifier = modifier
    ) {
        item("local_backup") {
            Text(
                text = stringResource(id = R.string.local),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(start = 56.dp)
            )
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.backup_to_local)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Backup,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable {
                    backupCreator.launch("Word_Diary_${System.currentTimeMillis()}.zip")
                }
            )
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.restore_from_local)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Restore,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable {
                    onEvent(SettingsEvent.ShowDialog(SettingsDialog.RestoreBackup))
                }
            )
        }

        item("drive_backup") {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.google_drive),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(start = 56.dp)
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(if (WordDiaryApp.hasPremium) 1F else .3F)
                ) {
                    DriveConfigView(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(16.dp),
                        onAction = {
                            if (WordDiaryApp.hasPremium.not()) return@DriveConfigView

                            if (userData().email.isNullOrEmpty().not()) onEvent(SettingsEvent.CreateDriveBackup)
                            else googleLoginLauncher.launch(1881)
                        }, isSignedIn = userData().email.isNullOrEmpty().not(),
                        isDriveBackingUp = isDriveBackingUp
                    )

                    if (userData().email.isNullOrEmpty().not()) {
                        ListItem(
                            headlineContent = { Text(text = stringResource(id = R.string.restore_backup)) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Download,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {
                                if (WordDiaryApp.hasPremium.not()) return@clickable
                                navigator.navigate(BackupScreenDestination)
                            }
                        )

                        ListItem(
                            headlineContent = { Text(text = stringResource(id = R.string.sign_out)) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Logout,
                                    contentDescription = null
                                )
                            }, supportingContent = {
                                Text(text = userData().email ?: "")
                            },
                            modifier = Modifier.clickable {
                                if (WordDiaryApp.hasPremium.not()) return@clickable

                                onEvent(SettingsEvent.OnGoogleLogout)
                            }
                        )
                    }
                }

                if (WordDiaryApp.hasPremium.not()) {
                    ExtendedFloatingActionButton(
                        onClick = { navigator.navigate(PaywallScreenDestination) },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Diamond,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.enable_premium_feature)) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}