package com.eneskayiklik.word_diary.feature.emoji_picker.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.feature.emoji_picker.data.EmojiPickerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmojiPickerViewModel @Inject constructor(
    private val repository: EmojiPickerRepository,
    private val app: Application
) : ViewModel() {

    private val _state = MutableStateFlow(EmojiPickerState())
    val state = _state.asStateFlow()

    init {
        prepareEmojis()
    }

    private fun prepareEmojis() = viewModelScope.launch(Dispatchers.IO) {
        _state.update {
            it.copy(
                emojiList = repository.getEmojis(app),
                isLoading = false
            )
        }
    }

    fun tabIndexFromFirstVisibleIdemIndex(index: Int): Int {
        var result = 0
        _state.value.emojiList.foldIndexed(0) { foldIndex, value, item ->
            if (item.lineCount + value + foldIndex < index) result = foldIndex + 1
            item.lineCount + value
        }
        return result
    }

    fun lineIndexFromTabIndex(index: Int): Int = _state.value.emojiList.take(index)
        .foldRight(0) { item, value -> item.lineCount + value } + index
}