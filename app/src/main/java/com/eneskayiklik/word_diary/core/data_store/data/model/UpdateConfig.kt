package com.eneskayiklik.word_diary.core.data_store.data.model

data class UpdateConfig(
    val forceUpdateVersion: Int,
    val optionalUpdateVersion: Int,
    val features: String,
    val latestVersionName: String,
    val lastUpdated: Long
)
