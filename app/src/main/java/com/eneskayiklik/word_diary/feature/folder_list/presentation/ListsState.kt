package com.eneskayiklik.word_diary.feature.folder_list.presentation

import android.net.Uri
import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount
import com.google.android.gms.ads.nativead.NativeAd

data class ListsState(
    val folders: List<FolderWithWordCount> = emptyList(),
    val searchResult: List<FolderWithWordCount> = emptyList(),
    val nativeAd: NativeAd? = null,
    val searchAd: NativeAd? = null,
    val isLoading: Boolean = true,
    val dialogType: ListsDialogType = ListsDialogType.NONE,
    val userData: UserData = UserData()
) {
    val showEmptyLayout = folders.isEmpty() && isLoading.not()
    val isDialogActive = dialogType != ListsDialogType.NONE
}

data class UserData(
    val photoUrl: Uri? = null,
    val displayName: String? = null,
    val email: String? = null
)

enum class ListsDialogType {
    NONE,
    DELETE_COLLECTION
}