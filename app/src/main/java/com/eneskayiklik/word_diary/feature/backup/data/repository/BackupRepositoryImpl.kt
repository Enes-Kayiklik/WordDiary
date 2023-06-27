package com.eneskayiklik.word_diary.feature.backup.data.repository

import android.content.Context
import android.net.Uri
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.util.BackupUtils
import com.eneskayiklik.word_diary.di.DbModule.DB_NAME
import com.eneskayiklik.word_diary.feature.backup.domain.repository.BackupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(

) : BackupRepository {

    override suspend fun backupToLocal(
        context: Context,
        destination: Uri
    ): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
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
            } ?: run {
                emit(Result.Error(NullPointerException()))
            }
            zipFile.delete()
            emit(Result.Success(true))
        } else {
            emit(Result.Error(NullPointerException()))
        }
        //BackupUtils.unzipFiles(customOutputFile, File(context.filesDir.path + "/deneme"))
    }.flowOn(Dispatchers.IO)

    override suspend fun backupToDrive(): Flow<Result<Boolean>> {
        TODO("Not yet implemented")
    }
}