package com.eneskayiklik.word_diary.feature.folder_list.presentation

import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount

data class ListsState(
    val folders: List<FolderWithWordCount> = emptyList(),
    val isLoading: Boolean = true
) {
    val showEmptyLayout = folders.isEmpty() && isLoading.not()
}
