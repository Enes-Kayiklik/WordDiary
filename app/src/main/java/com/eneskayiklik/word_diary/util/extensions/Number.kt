package com.eneskayiklik.word_diary.util.extensions

import com.eneskayiklik.word_diary.R

fun Double.getAccuracyStringRes(): Int {
    return when (this) {
        in 75.0..100.0 -> listOf(
            R.string.study_result_kudos_1,
            R.string.study_result_kudos_2,
            R.string.study_result_kudos_3
        ).random()

        in 50.0..74.0 -> listOf(
            R.string.study_result_kudos_4,
            R.string.study_result_kudos_5,
            R.string.study_result_kudos_6
        ).random()

        else -> listOf(
            R.string.study_result_kudos_7,
            R.string.study_result_kudos_8,
            R.string.study_result_kudos_9
        ).random()
    }
}

fun Double.getHeadlineImage(): Int {
    return when (this) {
        in 75.0..100.0 -> R.drawable.ic_party_popper
        in 50.0..74.0 -> R.drawable.ic_star
        else -> R.drawable.ic_magnifying_glass
    }
}

fun Double.formatFloating(fraction: Int = 1) =
    (if (this % 1.0 == .0) "%d".format(toInt()) else "%.${fraction}f".format(this)).toDouble()