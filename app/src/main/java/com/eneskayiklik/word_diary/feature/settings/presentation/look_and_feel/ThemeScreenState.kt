package com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel

import android.graphics.Color

data class ThemeScreenState(
    val activeDialog: ThemeDialogType = ThemeDialogType.NONE,
    val themeColors: List<Int> = generateThemeColors()
)

enum class ThemeDialogType {
    NONE,
    THEME_PICKER,
    COLOR_PICKER,
    COLOR_STYLE_PICKER,
    FONT_FAMILY_PICKER
}

fun generateThemeColors() = listOf(
    Color.parseColor("#FF1744"),
    Color.parseColor("#D50000"),
    Color.parseColor("#C51162"),
    Color.parseColor("#D500F9"),
    Color.parseColor("#AA00FF"),
    Color.parseColor("#651FFF"),
    Color.parseColor("#6200EA"),
    Color.parseColor("#00B0FF"),
    Color.parseColor("#0091EA"),
    Color.parseColor("#1DE9B6"),
    Color.parseColor("#00BFA5"),
    Color.parseColor("#00E676"),
    Color.parseColor("#00C853"),
    Color.parseColor("#C6FF00"),
    Color.parseColor("#AEEA00"),
    Color.parseColor("#DDEA00"),
    Color.parseColor("#FFD600"),
    Color.parseColor("#FFC400"),
    Color.parseColor("#FFAB00"),
    Color.parseColor("#FF6E40"),
    Color.parseColor("#B9F6CA")
)