package com.eneskayiklik.word_diary.core.ui.theme

import android.app.Application
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.destinations.OnboardingScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val app: Application
) : ViewModel() {

    val userPrefs = userPreferenceRepository.userData

    private val _keepScreen = MutableStateFlow(true)
    val keepScreen = _keepScreen.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        waitForInitialData()
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun waitForInitialData() = viewModelScope.launch {
        val data = userPreferenceRepository.userData.first()

        if (data.themePrefs.extractWallpaperColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) userPreferenceRepository.setColorFromWallpaper(
            app
        )
        else if (data.themePrefs.randomColor) userPreferenceRepository.setRandomColor()

        if (data.userLanguage == UserLanguage.NOT_SPECIFIED || data.showOnboarding) _event.emit(
            /*UiEvent.OnNavigate(
                UserLanguageScreenDestination
            )*/
            UiEvent.OnNavigate(OnboardingScreenDestination)
        )

        _keepScreen.update { false }
    }
}