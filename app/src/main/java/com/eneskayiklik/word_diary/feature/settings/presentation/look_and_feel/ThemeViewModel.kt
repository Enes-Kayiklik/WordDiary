package com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.AppTheme
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import com.eneskayiklik.word_diary.core.data_store.data.FontFamilyStyle
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ThemeScreenState())
    val state = _state.asStateFlow()

    val userPrefs = userPreferenceRepository.userData

    fun onEvent(event: ThemeEvent) = viewModelScope.launch {
        when (event) {
            is ThemeEvent.ShowDialog -> _state.update { it.copy(activeDialog = event.type) }
            is ThemeEvent.PickTheme -> pickTheme(event.theme)
            is ThemeEvent.PickColor -> pickColor(event.color)
            is ThemeEvent.PickColorStyle -> pickColorStyle(event.style)
            is ThemeEvent.PickFontFamily -> pickFontFamily(event.style)
            ThemeEvent.OnAmoledBlack -> updateAmoledBlack()
            ThemeEvent.OnColorfulBackground -> updateColorfulBackground()
            ThemeEvent.OnWallpaperColor -> updateWallpaperColor()
            ThemeEvent.OnRandomColor -> updateRandomColor()
        }
    }

    private fun updateAmoledBlack() = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.updateAmoledBlack()
    }

    private fun updateColorfulBackground() = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.updateColorfulBackground()
    }

    private fun updateWallpaperColor() = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.updateWallpaperColor()
    }

    private fun updateRandomColor() = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.updateRandomColor()
    }

    private fun pickTheme(theme: AppTheme) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(activeDialog = ThemeDialogType.NONE) }
        userPreferenceRepository.setAppTheme(theme)
    }

    private fun pickColor(color: Int) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(activeDialog = ThemeDialogType.NONE) }
        userPreferenceRepository.setColor(color)
    }

    private fun pickColorStyle(style: ColorStyle) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(activeDialog = ThemeDialogType.NONE) }
        userPreferenceRepository.setColorStyle(style)
    }

    private fun pickFontFamily(style: FontFamilyStyle) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(activeDialog = ThemeDialogType.NONE) }
        userPreferenceRepository.setFontFamily(style)
    }
}