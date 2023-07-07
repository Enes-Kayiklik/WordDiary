package com.eneskayiklik.word_diary.core.util

import com.eneskayiklik.word_diary.BuildConfig
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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

            // Encrypt file
            val inputBytes = outputFile.readBytes()
            val encrypted = inputBytes.encrypt()
            FileOutputStream(outputFile).use { outputStream ->
                outputStream.write(encrypted)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun unzipFiles(zipFile: File, destDir: File) {
        val buffer = ByteArray(1024)

        // Decrypt input file
        val inputBytes = zipFile.readBytes()
        val decryptedBytes = inputBytes.decrypt()
        FileOutputStream(zipFile).use { outputStream ->
            outputStream.write(decryptedBytes)
            outputStream.close()
        }

        val zipIn = ZipInputStream(ByteArrayInputStream(decryptedBytes))

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

    private fun ByteArray.encrypt(): ByteArray {
        val secretKeySpec: SecretKeySpec = generateKey()

        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(ByteArray(blockSize)))
        }.doFinal(this)
    }

    private fun ByteArray.decrypt(): ByteArray {
        val secretKeySpec: SecretKeySpec = generateKey()

        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(ByteArray(blockSize)))
        }.doFinal(this)
    }

    private fun generateKey(): SecretKeySpec {
        val keyBytes: ByteArray = BuildConfig.ENCRYPTION_SECRET.toByteArray().copyOf(16)
        return SecretKeySpec(keyBytes, ALGORITHM)
    }

    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val ALGORITHM = "AES"
}