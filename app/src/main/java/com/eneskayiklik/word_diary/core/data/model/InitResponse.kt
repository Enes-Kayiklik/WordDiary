package com.eneskayiklik.word_diary.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class InitResponse(
    val forceUpdateVersion: Int,
    val optionalUpdateVersion: Int,
    val features: String,
    val latestVersionName: String
)