package com.eneskayiklik.word_diary.feature.folder_list.presentation

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class FolderListEvent {

    object AddFolder : FolderListEvent()
    data class OnDeleteCollection(val id: Int) : FolderListEvent()
    data class EditFolder(val id: Int) : FolderListEvent()
    data class OnSearchQueryChanged(val q: String) : FolderListEvent()
    data class OnFavorite(val id: Int, val isFavorite: Boolean) : FolderListEvent()
    data class OnShowDialog(val type: ListsDialogType) : FolderListEvent()
    data class AddWordToFolder(val id: Int) : FolderListEvent()
    data class OnAdEvent(val startAd: Boolean) : FolderListEvent()
    data class OnGoogleLogin(val account: GoogleSignInAccount) : FolderListEvent()
}
