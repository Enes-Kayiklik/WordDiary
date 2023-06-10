package com.eneskayiklik.word_diary.feature.settings.presentation.licenses

import com.eneskayiklik.word_diary.feature.settings.domain.model.License

data class LicensesState(
    val libs: List<License> = emptyList()
)

