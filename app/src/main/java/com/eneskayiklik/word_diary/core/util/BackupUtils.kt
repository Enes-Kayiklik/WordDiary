package com.eneskayiklik.word_diary.core.util

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object BackupUtils {

    fun zipFiles(sourceFiles: List<File>, outputFile: File): Boolean {
        return try {
            val zipOut = ZipOutputStream(FileOutputStream(outputFile))

            for (sourceFile in sourceFiles) {
                val zipEntry = ZipEntry(sourceFile.name)
                zipOut.putNextEntry(zipEntry)

                sourceFile.inputStream().buffered().use { input ->
                    input.copyTo(zipOut)
                }
                zipOut.closeEntry()
            }
            zipOut.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun unzipFiles(zipFile: File, destDir: File) {
        val buffer = ByteArray(1024)
        val zipIn = ZipInputStream(zipFile.inputStream())

        var entry = zipIn.nextEntry
        while (entry != null) {
            val newFile = File(destDir, entry.name)
            if (entry.isDirectory) {
                newFile.mkdirs()
            } else {
                newFile.parentFile?.mkdirs()
                newFile.outputStream().use { output ->
                    var len = zipIn.read(buffer)
                    while (len > 0) {
                        output.write(buffer, 0, len)
                        len = zipIn.read(buffer)
                    }
                }
            }
            zipIn.closeEntry()
            entry = zipIn.nextEntry
        }
        zipIn.close()
    }
}