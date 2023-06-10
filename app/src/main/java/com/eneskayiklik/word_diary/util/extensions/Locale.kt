package com.eneskayiklik.word_diary.util.extensions

import java.util.Locale

fun String.getLocaleFromIsoCode() = when (this) {
    "" -> Locale.getDefault()
    else -> Locale.getDefault()
}