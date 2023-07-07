package com.eneskayiklik.word_diary.feature.backup.presentation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.ui.components.BasicDialog
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.backup.domain.model.DriveFile
import com.eneskayiklik.word_diary.feature.destinations.PaywallScreenDestination
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.contract.GoogleLoginContract
import com.eneskayiklik.word_diary.util.extensions.plus
import com.eneskayiklik.word_diary.util.extensions.restartApp
import com.google.android.gms.common.api.ApiException
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
@Destination(style = ScreensAnim::class)
fun BackupScreen(
    navigator: DestinationsNavigator,
    viewModel: BackupViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current

    val state = viewModel.state.collectAsState().value

    val backupCreator = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip"),
        onResult = viewModel::createBackup
    )

    val backupSelector = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = viewModel::restoreBackup
    )

    val googleLoginLauncher = rememberLauncherForActivityResult(GoogleLoginContract()) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)
            viewModel.onGoogleLogin(account ?: return@rememberLauncherForActivityResult)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var activeBackup by remember { mutableStateOf<DriveFile?>(null) }

    if (state.isDialogActive) {
        when (state.dialogType) {
            BackupDialog.RemoveBackup -> BasicDialog(
                onDismiss = { viewModel.setDialog(BackupDialog.None) },
                onConfirm = {
                    viewModel.apply {
                        setDialog(BackupDialog.None)
                        deleteBackup(activeBackup?.id ?: return@BasicDialog)
                    }
                    activeBackup = null
                },
                title = "Remove backup",
                description = "Are you sure to remove this backup file permanently?",
                confirmText = stringResource(id = R.string.delete_confirm),
                dismissText = stringResource(id = R.string.dialog_cancel),
                icon = Icons.Outlined.Delete
            )

            BackupDialog.RestoreBackup -> BasicDialog(
                onDismiss = { viewModel.setDialog(BackupDialog.None) },
                onConfirm = {
                    val fileId = activeBackup?.id
                    if (fileId != null) viewModel.restoreDriveBackup(fileId)
                    else backupSelector.launch(arrayOf("application/zip"))
                    activeBackup = null
                },
                title = "Restore backup",
                description = "When you restore the backup file, your local files will be delete. Are you sure to continue?",
                confirmText = stringResource(id = R.string.dialog_continue),
                dismissText = stringResource(id = R.string.dialog_cancel)
            )

            BackupDialog.RestartApp -> BasicDialog(
                onDismiss = { context.restartApp() },
                title = "Restart needed",
                description = "To take effect this action, you must restart the application",
                dismissText = stringResource(id = R.string.dialog_restart),
                icon = Icons.Outlined.RestartAlt
            )

            else -> Unit
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is UiEvent.ShowToast -> {
                    if (it.text != null) {
                        Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
                    } else if (it.textRes != null) {
                        Toast.makeText(context, it.textRes, Toast.LENGTH_LONG).show()
                    }
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.settings_backup_title),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = navigator::popBackStack) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button_desc)
                    )
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(vertical = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "LOCAL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 56.dp)
                )
                ListItem(
                    headlineText = { Text(text = "Backup to local") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Backup,
                            contentDescription = null
                        )
                    }, trailingContent = {
                        if (state.isLocalBackupLoading) CircularProgressIndicator()
                    },
                    modifier = Modifier.clickable {
                        backupCreator.launch("Word_Diary_${System.currentTimeMillis()}.zip")
                    }
                )
                ListItem(
                    headlineText = { Text(text = "Restore from local") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Restore,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        viewModel.setDialog(BackupDialog.RestoreBackup)
                    }
                )
            }

            item {
                Text(
                    text = "GOOGLE DRIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 56.dp)
                )
                ListItem(
                    headlineText = {
                        if (state.userData.displayName != null) {
                            Text(text = state.userData.displayName)
                        } else {
                            Text(text = "Google Drive backup")
                        }
                    }, supportingText = {
                        if (state.userData.email != null) {
                            Text(text = state.userData.email)
                        } else {
                            Text(text = "You have to login to backup")
                        }
                    }, leadingContent = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_drive),
                            contentDescription = null
                        )
                    }, trailingContent = {
                        if (state.userData.email == null) {
                            TextButton(onClick = { googleLoginLauncher.launch(1881) }) {
                                Text(text = "Sign in")
                            }
                        } else {
                            IconButton(onClick = { viewModel.logOut() }) {
                                Icon(imageVector = Icons.Outlined.Logout, contentDescription = null)
                            }
                        }
                    }
                )
                ListItem(
                    headlineText = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Cloud backups")
                            AnimatedVisibility(visible = state.isDriveBackupsLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(start = 8.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    },
                    trailingContent = {
                        TextButton(onClick = {
                            if (WordDiaryApp.hasPremium) viewModel.createDriveBackup()
                            else navigator.navigate(PaywallScreenDestination)
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Create backup")
                                AnimatedVisibility(visible = state.isDriveBackingUp) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(start = 8.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }
                    }
                )
            }
            state.driveBackups.forEach { driveBackup ->
                item(key = driveBackup.id) {
                    ListItem(
                        modifier = Modifier.animateItemPlacement(),
                        headlineText = { Text(text = driveBackup.createDate) },
                        supportingText = { Text(text = driveBackup.size) },
                        trailingContent = {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.Restore,
                                    contentDescription = null,
                                    modifier = Modifier.clickable(
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        }, indication = rememberRipple(bounded = false),
                                        onClick = {
                                            activeBackup = driveBackup
                                            viewModel.setDialog(BackupDialog.RestoreBackup)
                                        }
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.clickable(
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        }, indication = rememberRipple(bounded = false),
                                        onClick = {
                                            activeBackup = driveBackup
                                            viewModel.setDialog(BackupDialog.RemoveBackup)
                                        }
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}