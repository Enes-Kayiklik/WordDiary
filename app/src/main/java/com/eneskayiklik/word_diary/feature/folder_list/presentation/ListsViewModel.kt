package com.eneskayiklik.word_diary.feature.folder_list.presentation

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.helper.ad.AdLoaderHelper
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.destinations.CreateFolderScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.CreateWordScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val folderRepo: FolderRepository,
    private val adLoader: AdLoaderHelper,
    private val app: Application,
    userPrefs: UserPreferenceRepository,
) : ViewModel() {
    private val _userPrefs = userPrefs.userData

    private val _state = MutableStateFlow(ListsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private var _isScreenVisible = false

    private var _isFeedAdActive: Boolean = false
    private var _isSearchAdActive: Boolean = false

    init {
        collectUserLang()
        getLastGoogleUser()
    }

    private fun onAdEvent(isStart: Boolean) = viewModelScope.launch {
        _isScreenVisible = isStart
        if (isStart) {
            if (_isFeedAdActive.not()) startGetFeedAd()
            if (_isSearchAdActive.not()) startGetSearchAd()
        }
    }

    private fun startGetSearchAd() = viewModelScope.launch(Dispatchers.IO) {
        _isFeedAdActive = true
        while (_isScreenVisible) {
            val ad = adLoader.getNativeAd(1)
            _state.value.searchAd?.destroy()
            _state.update { it.copy(searchAd = ad) }
            if (_state.value.searchAd != null) {
                delay(AD_RELOAD_INTERVAL * 2)
            } else {
                delay(5000)
            }
        }
        _isFeedAdActive = false
    }

    private fun startGetFeedAd() = viewModelScope.launch(Dispatchers.IO) {
        _isFeedAdActive = true
        while (_isScreenVisible) {
            val folderSize = _state.value.folders.size
            if (folderSize != 0) {
                val ad = adLoader.getNativeAd(1)
                _state.value.nativeAd?.destroy()
                _state.update { it.copy(nativeAd = ad) }
            }
            if (_state.value.nativeAd != null) {
                delay(AD_RELOAD_INTERVAL)
            } else {
                delay(5000)
            }
        }
        _isFeedAdActive = false
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun onEvent(event: FolderListEvent) {
        when (event) {
            FolderListEvent.AddFolder -> onEvent(UiEvent.OnNavigate(CreateFolderScreenDestination()))
            is FolderListEvent.EditFolder -> onEvent(
                UiEvent.OnNavigate(
                    CreateFolderScreenDestination(folderId = event.id)
                )
            )

            is FolderListEvent.AddWordToFolder -> onEvent(
                UiEvent.OnNavigate(
                    CreateWordScreenDestination(folderId = event.id)
                )
            )

            is FolderListEvent.OnShowDialog -> _state.update { it.copy(dialogType = event.type) }
            is FolderListEvent.OnDeleteCollection -> removeCollection(event.id)
            is FolderListEvent.OnFavorite -> addToFavorite(event.id, event.isFavorite)
            is FolderListEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.q)
            is FolderListEvent.OnAdEvent -> onAdEvent(event.startAd)
            is FolderListEvent.OnGoogleLogin -> onGoogleLogin(event.account)
        }
    }

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        _event.emit(event)
    }

    private fun removeCollection(folderId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(dialogType = ListsDialogType.NONE) }
        if (folderId == -1) return@launch
        folderRepo.removeFolder(folderId)
    }

    private fun addToFavorite(folderId: Int, isFavorite: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            if (folderId == -1) return@launch
            folderRepo.updateFolderFavoriteState(folderId, isFavorite.not())
        }

    private fun onSearchQueryChanged(q: String) = viewModelScope.launch {
        val everyFolder = _state.value.folders
        val searchResult = if (q.isEmpty()) {
            everyFolder.filter { it.folder.isFavorite }
        } else {
            everyFolder.filter { it.folder.title.contains(q, true) }
                .sortedBy { it.folder.isFavorite }
        }

        _state.update { it.copy(searchResult = searchResult) }
    }

    private fun collectUserLang() = viewModelScope.launch(Dispatchers.IO) {
        _userPrefs.collectLatest {
            if (it.userLanguage != UserLanguage.NOT_SPECIFIED) getFolders(it.userLanguage.isoCode)
        }
    }

    private fun getFolders(
        userLangIso: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        folderRepo.getFoldersWithWordCount(userLangIso).collectLatest { new ->
            _state.update { it.copy(folders = new, isLoading = false) }
        }
    }

    private fun onGoogleLogin(account: GoogleSignInAccount) = viewModelScope.launch {
        _state.update {
            it.copy(
                userData = it.userData.copy(
                    photoUrl = account.photoUrl,
                    displayName = account.displayName,
                    email = account.email
                )
            )
        }
    }

    private fun getLastGoogleUser() = viewModelScope.launch(Dispatchers.IO) {
        onEvent(
            FolderListEvent.OnGoogleLogin(
                GoogleSignIn.getLastSignedInAccount(app) ?: return@launch
            )
        )
    }

    companion object {
        const val AD_RELOAD_INTERVAL = 61_293L
    }

    override fun onCleared() {
        _state.value.apply {
            nativeAd?.destroy()
            searchAd?.destroy()
        }
        super.onCleared()
    }
}