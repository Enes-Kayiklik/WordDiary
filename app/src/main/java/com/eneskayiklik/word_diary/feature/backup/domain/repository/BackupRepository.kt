package com.eneskayiklik.word_diary.feature.backup.domain.repository

import android.content.Context
import android.net.Uri
import com.eneskayiklik.word_diary.core.data.Result
import kotlinx.coroutines.flow.Flow

interface BackupRepository {

    suspend fun generateBackupZip(context: Context, destination: Uri): Boolean

    suspend fun backupToLocal(context: Context, destination: Uri): Flow<Result<Boolean>>

    suspend fun backupToDrive(context: Context): Flow<Result<Boolean>>

    suspend fun listDriveFiles(context: Context): Flow<Result<Boolean>>
}