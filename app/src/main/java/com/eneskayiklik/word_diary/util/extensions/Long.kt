package com.eneskayiklik.word_diary.util.extensions

import com.eneskayiklik.word_diary.R

fun Long.formatStudyTimer(): String {
    val hour = this / (1000 * 60 * 60)
    val minute = (this / (1000 * 60)) % 60
    val second = this / 1000 % 60
    val result = StringBuilder()
    if (hour > 0) result.append("$hour:")

    if (minute >= 10) result.append("$minute:")
    else result.append("0$minute:")

    if (second >= 10) result.append(second)
    else result.append("0$second")
    return result.toString()
}

fun Long.formatStatisticsTimer(): String {
    val day = this / (1000 * 60 * 60 * 24)
    val hour = this / (1000 * 60 * 60) % 24
    val minute = (this / (1000 * 60)) % 60
    val second = this / 1000 % 60
    val result = StringBuilder()

    if (day > 0) result.append("$day:")
    if (hour > 0) result.append("$hour:")

    if (minute >= 10) result.append("$minute:")
    else result.append("0$minute:")

    if (second >= 10) result.append(second)
    else result.append("0$second")
    return result.toString()
}

fun Long.getTimeShortName(): Int {
    val day = this / (1000 * 60 * 60 * 24)
    val hour = this / (1000 * 60 * 60) % 24
    val minute = (this / (1000 * 60)) % 60

    return when {
        day > 0 -> R.string.day_format
        hour > 0 -> R.string.hour_format
        minute > 0 -> R.string.minute_format
        else -> R.string.second_format
    }
}