package com.eneskayiklik.word_diary.feature.backup.presentation

import com.eneskayiklik.word_diary.feature.folder_list.presentation.UserData

data class BackupState(
    val isLocalBackupLoading: Boolean = false,
    val userData: UserData = UserData()
)
