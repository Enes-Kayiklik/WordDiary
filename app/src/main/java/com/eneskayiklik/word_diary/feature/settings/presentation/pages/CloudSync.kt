package com.eneskayiklik.word_diary.feature.settings.presentation.pages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.feature.destinations.PaywallScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.EmptyDataView
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CloudSyncPage(
    modifier: Modifier,
    userPrefs: () -> UserPreference,
    navigator: DestinationsNavigator,
    onEvent: (SettingsEvent) -> Unit
) {

    val backupCreator = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip"),
        onResult = { onEvent(SettingsEvent.CreateBackup(it)) }
    )

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
            Text(
                text = stringResource(id = R.string.google_drive),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(start = 56.dp)
            )

            if (WordDiaryApp.hasPremium.not()) {
                EmptyDataView(
                    modifier = Modifier.fillMaxSize(),
                    title = stringResource(id = R.string.premium_feature),
                    subtitle = stringResource(id = R.string.premium_feature_desc),
                    icon = Icons.Outlined.Diamond,
                    actionText = stringResource(id = R.string.enable_premium_feature),
                    onAction = { navigator.navigate(PaywallScreenDestination) },
                    actionIcon = Icons.Outlined.WorkspacePremium
                )
            } else {

            }
        }
    }
}