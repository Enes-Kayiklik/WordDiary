package com.eneskayiklik.word_diary.feature.backup.domain.model

import com.eneskayiklik.word_diary.util.extensions.toHumanReadableSize
import com.google.api.services.drive.model.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

data class DriveFile(
    val id: String = "",
    val createDate: String = "",
    val size: String = ""
)

fun File.toDriveFile() = try {
    DriveFile(
        id = id,
        createDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(createdTime.value),
            TimeZone.getDefault().toZoneId()
        ).format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")),
        size = getSize().toHumanReadableSize()
    )
} catch (e: Exception) {
    e.printStackTrace()
    null
}