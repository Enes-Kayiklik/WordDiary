package com.eneskayiklik.word_diary.feature.update.presentation

data class UpdateState(
    val isForceUpdate: Boolean = false,
    val features: String = "",
    val latestVersionName: String = ""
)
