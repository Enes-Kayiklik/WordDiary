package com.eneskayiklik.word_diary.feature.settings.presentation.about

import com.eneskayiklik.word_diary.feature.settings.domain.model.License

data class AboutState(
    val libs: List<License> = emptyList()
)

