package com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel

import com.eneskayiklik.word_diary.core.data_store.data.AppTheme
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import com.eneskayiklik.word_diary.core.data_store.data.FontFamilyStyle

sealed class ThemeEvent {

    data class ShowDialog(val type: ThemeDialogType) : ThemeEvent()

    data class PickTheme(val theme: AppTheme) : ThemeEvent()

    data class PickColorStyle(val style: ColorStyle) : ThemeEvent()

    data class PickFontFamily(val style: FontFamilyStyle) : ThemeEvent()

    data class PickColor(val color: Int) : ThemeEvent()

    object OnAmoledBlack : ThemeEvent()

    object OnColorfulBackground : ThemeEvent()

    object OnWallpaperColor : ThemeEvent()

    object OnRandomColor : ThemeEvent()
}