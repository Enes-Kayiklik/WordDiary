package com.eneskayiklik.word_diary.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun UpdateLanguage(iso: String) {
    val locale = if (iso.isEmpty()) Locale.getDefault() else Locale(iso)
    val config = LocalConfiguration.current
    config.setLocale(locale)
    val res = LocalContext.current.resources
    res.updateConfiguration(config, res.displayMetrics)
}