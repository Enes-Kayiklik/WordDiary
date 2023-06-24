package com.eneskayiklik.word_diary.feature.update.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eneskayiklik.word_diary.BuildConfig
import com.eneskayiklik.word_diary.feature.destinations.UpdateScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@HiltViewModel
class UpdateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(UpdateState())
    val state = _state.asStateFlow()

    init {
        val data = UpdateScreenDestination.argsFrom(savedStateHandle).config
        _state.update {
            it.copy(
                isForceUpdate = data.forceUpdateVersion > BuildConfig.VERSION_CODE,
                features = data.features,
                latestVersionName = data.latestVersionName
            )
        }
    }
}