package com.eneskayiklik.word_diary.feature.folder_list.presentation

import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount
import com.google.android.gms.ads.nativead.NativeAd

data class ListsState(
    val folders: List<FolderWithWordCount> = emptyList(),
    val searchResult: List<FolderWithWordCount> = emptyList(),
    val nativeAd: NativeAd? = null,
    val searchAd: NativeAd? = null,
    val isLoading: Boolean = true,
    val dialogType: ListsDialogType = ListsDialogType.NONE
) {
    val showEmptyLayout = folders.isEmpty() && isLoading.not()
    val isDialogActive = dialogType != ListsDialogType.NONE
}

enum class ListsDialogType {
    NONE,
    DELETE_COLLECTION
}