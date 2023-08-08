package com.eneskayiklik.word_diary.feature.backup.presentation

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.components.BasicDialog
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.backup.domain.model.DriveFile
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.plus
import com.eneskayiklik.word_diary.util.extensions.restartApp
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    val state = viewModel.state.collectAsState().value

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
                title = stringResource(id = R.string.remove_backup),
                description = stringResource(id = R.string.remove_backup_desc),
                confirmText = stringResource(id = R.string.delete_confirm),
                dismissText = stringResource(id = R.string.dialog_cancel),
                icon = Icons.Outlined.Delete
            )

            BackupDialog.RestoreBackup -> BasicDialog(
                onDismiss = { viewModel.setDialog(BackupDialog.None) },
                onConfirm = {
                    val fileId = activeBackup?.id
                    if (fileId != null) viewModel.restoreDriveBackup(fileId)
                    activeBackup = null
                },
                title = stringResource(id = R.string.restore_backup),
                description = stringResource(id = R.string.restore_backup_desc),
                confirmText = stringResource(id = R.string.dialog_continue),
                dismissText = stringResource(id = R.string.dialog_cancel)
            )

            BackupDialog.RestartApp -> BasicDialog(
                onDismiss = { context.restartApp() },
                title = stringResource(id = R.string.restart_needed),
                description = stringResource(id = R.string.restart_needed_desc),
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

                UiEvent.ClearBackstack -> navigator.navigateUp()

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.cloud_backups),
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
            if (state.showLoading) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            state.driveBackups.forEach { driveBackup ->
                item(key = driveBackup.id) {
                    ListItem(
                        modifier = Modifier.animateItemPlacement(),
                        headlineContent = { Text(text = driveBackup.createDate) },
                        supportingContent = { Text(text = driveBackup.size) },
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