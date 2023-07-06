package com.eneskayiklik.word_diary.feature.backup.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.util.BackupUtils
import com.eneskayiklik.word_diary.di.DbModule.DB_NAME
import com.eneskayiklik.word_diary.feature.backup.domain.repository.BackupRepository
import com.eneskayiklik.word_diary.util.extensions.getDriveService
import com.google.api.client.http.FileContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.util.Collections
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(

) : BackupRepository {

    override suspend fun generateBackupZip(
        context: Context,
        destination: Uri
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val db = context.getDatabasePath(DB_NAME)
            val shm = File(db.path + "-shm")
            val wal = File(db.path + "-wal")
            val zipFile = File(context.filesDir.path + "/backup.zip")
            if (zipFile.exists().not()) zipFile.createNewFile()

            val result = BackupUtils.zipFiles(listOf(db, shm, wal), zipFile)

            if (result) {
                context.contentResolver.openOutputStream(destination)?.let { outputStream ->
                    FileInputStream(zipFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    zipFile.delete()
                    true
                } ?: run {
                    false
                }
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun backupToLocal(
        context: Context,
        destination: Uri
    ): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        if (generateBackupZip(context, destination)) emit(Result.Success(true))
        else emit(Result.Error(NullPointerException()))
    }

    override suspend fun backupToDrive(context: Context): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        val drive = context.getDriveService()

        val generatedFile = File(context.cacheDir.path, "/drive_backup.wd")
        if (generatedFile.exists().not()) generatedFile.createNewFile()

        val isGenerated = generateBackupZip(context, generatedFile.toUri())
        if (isGenerated) {
            val uploadedFile = com.google.api.services.drive.model.File().apply {
                parents = Collections.singletonList("appDataFolder")
                name = generatedFile.name
            }
            val mediaContent = FileContent("", generatedFile)
            drive.files().create(uploadedFile, mediaContent).execute()
            emit(Result.Success(true))
        } else {
            emit(Result.Error(NullPointerException()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun listDriveFiles(context: Context): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading)
            val drive = context.getDriveService()
            val files = drive.files().list().setSpaces("appDataFolder")
                .setFields("nextPageToken, files(id, name)")
                .setPageSize(10)
                .execute()
            Log.e("TAGTAG", "listDriveFiles: ${files.files.joinToString { it.id }}", )
            emit(Result.Success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("TAGTAG", "listDriveFiles: $e", )
        }
    }
}