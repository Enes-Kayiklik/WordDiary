package com.eneskayiklik.word_diary.feature.settings.presentation.licenses

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.feature.settings.domain.model.toLicence
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LicensesViewModel @Inject constructor(
    private val app: Application
) : ViewModel() {

    private val _state = MutableStateFlow(LicensesState())
    val state = _state.asStateFlow()

    init {
        setupLibs()
    }

    private fun setupLibs() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _state.update {
                it.copy(
                    libs = Libs.Builder().withContext(app)
                        .build().libraries.mapNotNull { lib -> lib.toLicence() })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}