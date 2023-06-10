package com.eneskayiklik.word_diary.feature.settings.domain.model

import com.mikepenz.aboutlibraries.entity.Library

data class License(
    val title: String,
    val license: String?,
    val version: String?,
    val author: String?
)

fun Library.toLicence() = try {
    License(
        title = name,
        license = licenses.firstOrNull()?.name,
        version = artifactVersion,
        author = developers.firstOrNull()?.name
    )
} catch (e: Exception) {
    e.printStackTrace()
    null
}
