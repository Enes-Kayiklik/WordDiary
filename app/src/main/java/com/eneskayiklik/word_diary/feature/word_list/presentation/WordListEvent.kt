package com.eneskayiklik.word_diary.feature.word_list.presentation

import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.database.entity.WordEntity

sealed class WordListEvent {

    object CreateWord : WordListEvent()

    object OnDeleteWord : WordListEvent()

    object OnDeleteList : WordListEvent()

    object OnUndoDelete : WordListEvent()

    data class OnFilterSelected(val filterType: WordListFilterType) : WordListEvent()

    data class OnSortSelected(val sortType: WordListSortType) : WordListEvent()

    data class OnSortDirectionSelected(val direction: WordListSortDirection) : WordListEvent()

    data class OnWordAction(val word: WordEntity, val action: SwipeAction) : WordListEvent()

    data class OnWordClick(val word: WordEntity?) : WordListEvent()

    data class OnShowDialog(val type: WordListDialogType) : WordListEvent()

    data class OnSpeakEvent(
        val firstSentence: String? = null,
        val firstSource: String? = null,
        val secondSentence: String? = null,
        val secondSource: String? = null
    ) : WordListEvent()
}