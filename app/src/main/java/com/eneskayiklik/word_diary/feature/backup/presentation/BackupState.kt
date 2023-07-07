package com.eneskayiklik.word_diary.feature.backup.presentation

import com.eneskayiklik.word_diary.feature.backup.domain.model.DriveFile
import com.eneskayiklik.word_diary.feature.folder_list.presentation.UserData

data class BackupState(
    val isLocalBackupLoading: Boolean = false,
    val userData: UserData = UserData(),
    val isDriveBackupsLoading: Boolean = false,
    val isDriveBackingUp: Boolean = false,
    val driveBackups: List<DriveFile> = emptyList(),
    val dialogType: BackupDialog = BackupDialog.None
) {
    val isDialogActive = dialogType != BackupDialog.None
}

enum class BackupDialog {
    None,
    RemoveBackup,
    RestoreBackup,
    RestartApp
}