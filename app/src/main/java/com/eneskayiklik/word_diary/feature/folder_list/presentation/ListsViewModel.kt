package com.eneskayiklik.word_diary.feature.folder_list.presentation

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.ad_manager.AdManager
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val folderRepo: FolderRepository,
    private val app: Application,
    userPrefs: UserPreferenceRepository,
) : ViewModel() {
    private val _userPrefs = userPrefs.userData

    private val _state = MutableStateFlow(ListsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private var _isVisibleOnScreen: Boolean = false

    init {
        collectUserLang()
        getLastGoogleUser()
    }

    private fun onAdEvent(isStart: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _isVisibleOnScreen = isStart
        if (isStart.not()) {
            _state.update { st ->
                AdManager.borrowOrRelease(st.searchAd?.id, false)
                AdManager.borrowOrRelease(st.nativeAd?.id, false)
                delay(500)
                st.copy(
                    searchAd = null,
                    nativeAd = null
                )
            }
        } else collectAds()
    }

    private fun collectAds() {
        AdManager.activeAds.onEach {
            if (it.isNotEmpty() && _isVisibleOnScreen) {
                val searchAd = it.getOrNull(0)
                val nativeAd = it.getOrNull(1)

                _state.update { st ->
                    if (st.searchAd == null || st.nativeAd == null) {
                        AdManager.borrowOrRelease(searchAd?.id, true)
                        AdManager.borrowOrRelease(nativeAd?.id, true)

                        st.copy(
                            searchAd = searchAd,
                            nativeAd = nativeAd
                        )
                    } else st
                }
            }
        }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun onEvent(event: FolderListEvent) {
        when (event) {
            FolderListEvent.AddFolder -> onEvent(UiEvent.OnNavigate(CreateFolderScreenDestination(-1)))
            is FolderListEvent.EditFolder -> onEvent(
                UiEvent.OnNavigate(
                    CreateFolderScreenDestination(folderId = event.id)
                )
            )

            is FolderListEvent.AddWordToFolder -> onEvent(
                UiEvent.OnNavigate(
                    CreateWordScreenDestination(folderId = event.id, wordId = -1)
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
        if (event is UiEvent.OnAdShown) {
            AdManager.increaseSeenCount(event.adId)
        } else _event.emit(event)
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
}