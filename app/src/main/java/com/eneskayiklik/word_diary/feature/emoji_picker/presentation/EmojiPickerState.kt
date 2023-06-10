package com.eneskayiklik.word_diary.feature.emoji_picker.presentation

import com.eneskayiklik.word_diary.feature.emoji_picker.domain.model.EmojiTree

data class EmojiPickerState(
    val emojiList: List<EmojiTree> = emptyList(),
    val isLoading: Boolean = true
) {
    val tabs = emojiList.map { it.title.icon }
}