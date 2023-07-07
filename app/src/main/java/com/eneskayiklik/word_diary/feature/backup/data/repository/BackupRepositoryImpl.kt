package com.eneskayiklik.word_diary.feature.backup.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.util.BackupUtils
import com.eneskayiklik.word_diary.di.DbModule.DB_NAME
import com.eneskayiklik.word_diary.feature.backup.domain.model.DriveFile
import com.eneskayiklik.word_diary.feature.backup.domain.model.toDriveFile
import com.eneskayiklik.word_diary.feature.backup.domain.repository.BackupRepository
import com.eneskayiklik.word_diary.util.extensions.createIfNotExists
import com.eneskayiklik.word_diary.util.extensions.getDriveService
import com.google.api.client.http.FileContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(

) : BackupRepository {

    private var driveFolder: String? = null

    override suspend fun generateBackupZip(
        context: Context,
        destination: Uri
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val db = context.getDatabasePath(DB_NAME)
            val shm = File(db.path + "-shm")
            val wal = File(db.path + "-wal")
            val zipFile = File(context.filesDir.path + "/backup.zip").createIfNotExists()

            val result = BackupUtils.zipFiles(listOf(db, shm, wal), zipFile)

            if (result) {
                context.contentResolver.openOutputStream(destination)?.let { outputStream ->
                    FileInputStream(zipFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                        inputStream.close()
                    }
                    outputStream.close()
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

    override suspend fun getOrCreateDriveFolder(context: Context, folderName: String): String? {
        val driveService = context.getDriveService()
        // Check if the folder exists
        val query =
            "mimeType='application/vnd.google-apps.folder' and trashed=false and name='$folderName'"
        val folders = driveService.files().list().setQ(query).execute()
        if (folders.files.isNullOrEmpty().not()) {
            driveFolder = folders.files[0].id
            return folders.files[0].id
        }

        // Create the folder if it doesn't exist
        val folderMetadata = com.google.api.services.drive.model.File().apply {
            name = folderName
            mimeType = "application/vnd.google-apps.folder"
        }

        try {
            val createdFolder = driveService.files().create(folderMetadata).execute()
            driveFolder = createdFolder.id
            return createdFolder.id
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun backupToLocal(
        context: Context,
        destination: Uri
    ): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        if (generateBackupZip(context, destination)) emit(Result.Success(true))
        else emit(Result.Error(NullPointerException()))
    }

    override suspend fun restoreFromLocal(
        context: Context,
        from: Uri
    ): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading)
            val output = File(context.dataDir.path + "/databases")
            val destination =
                File(context.dataDir.path + "/databases/backup.zip").createIfNotExists()

            context.contentResolver.openInputStream(from)?.let { inputStream ->
                FileOutputStream(destination).use { outputStream ->
                    outputStream.write(inputStream.readBytes())
                    outputStream.close()
                }
                inputStream.close()
            }
            BackupUtils.unzipFiles(destination, output)
            destination.delete()
            emit(Result.Success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun backupToDrive(context: Context): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        val drive = context.getDriveService()

        val driveFolderId =
            driveFolder ?: getOrCreateDriveFolder(context, "Word Diary") ?: "appDataFolder"
        val generatedFile =
            File(context.cacheDir.path, "/${System.currentTimeMillis()}.zip").createIfNotExists()

        val isGenerated = generateBackupZip(context, generatedFile.toUri())
        if (isGenerated) {
            val uploadedFile = com.google.api.services.drive.model.File().apply {
                parents = Collections.singletonList(driveFolderId)
                name = generatedFile.name
            }
            val mediaContent = FileContent("", generatedFile)
            drive.files().create(uploadedFile, mediaContent).execute()
            emit(Result.Success(true))
        } else {
            emit(Result.Error(NullPointerException()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteBackupFile(
        context: Context, fileId: String
    ): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading)
            val drive = context.getDriveService()
            drive.files().delete(fileId)
                .execute()

            emit(Result.Success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }

    override suspend fun listDriveFiles(context: Context): Flow<Result<List<DriveFile>>> = flow {
        try {
            emit(Result.Loading)
            val drive = context.getDriveService()
            val driveFolderId =
                driveFolder ?: getOrCreateDriveFolder(context, "Word Diary") ?: "appDataFolder"
            val query = "'$driveFolderId' in parents and trashed=false"
            val files = drive.files().list()
                .setQ(query)
                .setFields("nextPageToken, files(*)")
                .setPageSize(20)
                .execute()

            emit(Result.Success(files.files
                .sortedByDescending { it.createdTime.value }
                .mapNotNull { it.toDriveFile() })
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }

    override suspend fun restoreFromDrive(
        context: Context,
        fileId: String
    ): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading)
            val drive = context.getDriveService()

            val output = File(context.dataDir.path + "/databases")
            val destination =
                File(context.dataDir.path + "/databases/backup.zip").createIfNotExists()

            FileOutputStream(destination).use { outputStream ->
                drive.files().get(fileId).executeMediaAndDownloadTo(outputStream)
                outputStream.close()
            }

            BackupUtils.unzipFiles(destination, output)
            destination.delete()
            emit(Result.Success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}