package com.eneskayiklik.word_diary.feature.folder_list.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.ui.OnLifecycleEvent
import com.eneskayiklik.word_diary.core.ui.components.BasicDialog
import com.eneskayiklik.word_diary.core.ui.components.StatusSearchBar
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.destinations.StudyScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.EmptyDataView
import com.eneskayiklik.word_diary.feature.destinations.WordListScreenDestination
import com.eneskayiklik.word_diary.core.ui.components.ad.MediumNativeAdView
import com.eneskayiklik.word_diary.core.ui.components.ad.SmallNativeAdView
import com.eneskayiklik.word_diary.core.ui.components.clipToMaterialShape
import com.eneskayiklik.word_diary.feature.destinations.PaywallScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.SettingsScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.SingleFolderRow
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import com.eneskayiklik.word_diary.util.extensions.navigationBarPadding
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Destination(start = true, style = ScreensAnim::class)
@Composable
fun ListsScreen(
    navigator: DestinationsNavigator,
    viewModel: ListsViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = viewModel.state.collectAsState().value

    var query by remember { mutableStateOf("") }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var lastSelectedFolder by remember { mutableIntStateOf(-1) }

    var isStudyListVisible by remember { mutableStateOf(false) }

    val showSearchTrailingIcon by remember {
        derivedStateOf { query.isNotEmpty() || isSearchActive.not() }
    }

    val lazyListState = rememberLazyListState()

    val firstItemVisible by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex == 0 }
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> viewModel.onEvent(FolderListEvent.OnAdEvent(true))
            Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(FolderListEvent.OnAdEvent(false))
            else -> Unit
        }
    }

    LaunchedEffect(key1 = isSearchActive) {
        if (isSearchActive) return@LaunchedEffect
        try {
            query = ""
            focusManager.clearFocus(true)
            keyboardController?.hide()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is UiEvent.OnNavigate -> navigator.navigate(it.route)
                else -> Unit
            }
        }
    }

    if (state.isDialogActive) {
        when (state.dialogType) {
            ListsDialogType.DELETE_COLLECTION -> BasicDialog(
                onDismiss = { viewModel.onEvent(FolderListEvent.OnShowDialog(ListsDialogType.NONE)) },
                onConfirm = {
                    viewModel.onEvent(
                        FolderListEvent.OnDeleteCollection(
                            lastSelectedFolder
                        )
                    )
                },
                title = stringResource(id = R.string.delete_collection),
                description = stringResource(id = R.string.delete_collection_desc),
                confirmText = stringResource(id = R.string.delete_confirm),
                dismissText = stringResource(id = R.string.dialog_cancel),
                icon = Icons.Outlined.Delete
            )

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            AnimatedVisibility(
                visible = firstItemVisible && state.showEmptyLayout.not(),
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(FolderListEvent.AddFolder) },
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(id = R.string.add_list_content_desc)
                    )
                }
            }
        },
        topBar = {
            StatusSearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    viewModel.onEvent(FolderListEvent.OnSearchQueryChanged(it))
                },
                onSearch = { },
                placeholder = { Text(text = stringResource(id = R.string.search_collections)) },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                leadingIcon = {
                    if (isSearchActive) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }, indication = rememberRipple(bounded = false),
                                onClick = { isSearchActive = false }
                            )
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    if (showSearchTrailingIcon) {
                        if (query.isNotEmpty() || isSearchActive) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable(
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    }, indication = rememberRipple(bounded = false),
                                    onClick = { query = "" }
                                )
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null,
                                modifier = Modifier.clickable(
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    }, indication = rememberRipple(bounded = false),
                                    onClick = { navigator.navigate(SettingsScreenDestination) }
                                )
                            )
                        }
                    }
                },
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (state.searchAd != null && WordDiaryApp.hasPremium.not()) stickyHeader(
                            key = "search_ad"
                        ) {
                            SmallNativeAdView(
                                nativeAd = state.searchAd.nativeAd,
                                onAdShownOnScreen = { viewModel.onEvent(UiEvent.OnAdShown(state.searchAd.id)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
                        }
                        state.searchResult.forEach { folder ->
                            item(key = folder.folder.folderId) {
                                SingleFolderRow(folderWithCount = folder, modifier = Modifier
                                    .animateItemPlacement()
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(
                                        MaterialTheme.colorScheme
                                            .surfaceColorAtElevation(12.dp)
                                    )
                                    .clickable {
                                        navigator.navigate(WordListScreenDestination(folderId = folder.folder.folderId))
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                    onAddWord = { folderId ->
                                        viewModel.onEvent(FolderListEvent.AddWordToFolder(folderId))
                                    },
                                    onPractise = { folderId ->
                                        lastSelectedFolder = folderId
                                        isStudyListVisible = true
                                        //isStudyListVisible = true
                                    },
                                    onEdit = { folderId ->
                                        viewModel.onEvent(FolderListEvent.EditFolder(folderId))
                                    },
                                    onDelete = { folderId ->
                                        lastSelectedFolder = folderId
                                        viewModel.onEvent(
                                            FolderListEvent.OnShowDialog(
                                                ListsDialogType.DELETE_COLLECTION
                                            )
                                        )
                                    },
                                    onFavorite = { folderId, isFavorite ->
                                        viewModel.onEvent(
                                            FolderListEvent.OnFavorite(
                                                folderId,
                                                isFavorite
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        when {
            state.showEmptyLayout -> EmptyDataView(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(id = R.string.oops),
                subtitle = stringResource(id = R.string.empty_collection_desc),
                icon = Icons.Outlined.Folder,
                actionText = stringResource(id = R.string.create_collection),
                onAction = { viewModel.onEvent(FolderListEvent.AddFolder) }
            )

            else -> LazyColumn(
                contentPadding = it + PaddingValues(
                    bottom = 96.dp,
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp
                ) + WindowInsets.navigationBars.asPaddingValues(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                state.folders.forEachIndexed { index, folder ->
                    item(key = folder.folder.folderId) {
                        SingleFolderRow(folderWithCount = folder, modifier = Modifier
                            .animateItemPlacement()
                            .fillMaxWidth()
                            .clipToMaterialShape(
                                index = index,
                                lastIndex = state.folders.lastIndex,
                                smallShape = MaterialTheme.shapes.extraSmall,
                                largeShape = MaterialTheme.shapes.medium
                            )
                            .background(
                                MaterialTheme.colorScheme
                                    .surfaceColorAtElevation(12.dp)
                            )
                            .clickable {
                                navigator.navigate(WordListScreenDestination(folderId = folder.folder.folderId))
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                            onAddWord = { folderId ->
                                viewModel.onEvent(FolderListEvent.AddWordToFolder(folderId))
                            },
                            onPractise = { folderId ->
                                lastSelectedFolder = folderId
                                isStudyListVisible = true
                                //isStudyListVisible = true
                            },
                            onEdit = { folderId ->
                                viewModel.onEvent(FolderListEvent.EditFolder(folderId))
                            },
                            onDelete = { folderId ->
                                lastSelectedFolder = folderId
                                viewModel.onEvent(
                                    FolderListEvent.OnShowDialog(
                                        ListsDialogType.DELETE_COLLECTION
                                    )
                                )
                            },
                            onFavorite = { folderId, isFavorite ->
                                viewModel.onEvent(
                                    FolderListEvent.OnFavorite(
                                        folderId,
                                        isFavorite
                                    )
                                )
                            }
                        )
                    }

                    if (index == 0 && state.nativeAd != null && WordDiaryApp.hasPremium.not()) {
                        item(key = "ad_item") {
                            MediumNativeAdView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clipToMaterialShape(
                                        index = 1,
                                        lastIndex = state.folders.lastIndex + 1,
                                        smallShape = MaterialTheme.shapes.extraSmall,
                                        largeShape = MaterialTheme.shapes.medium
                                    )
                                    .background(
                                        MaterialTheme.colorScheme
                                            .surfaceColorAtElevation(12.dp)
                                    ),
                                nativeAd = state.nativeAd.nativeAd,
                                onVisibleOnScreen = {
                                    viewModel.onEvent(UiEvent.OnAdShown(state.nativeAd.id))
                                },
                                onRemoveAds = {
                                    viewModel.onEvent(UiEvent.OnNavigate(PaywallScreenDestination))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (isStudyListVisible) {
        ModalBottomSheet(
            onDismissRequest = { isStudyListVisible = false },
            windowInsets = WindowInsets(0)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarPadding()
                    .padding(bottom = 16.dp)
            ) {
                StudyType.values().forEach { study ->
                    item {
                        ListItem(
                            headlineContent = { Text(text = stringResource(id = study.title)) },
                            supportingContent = { Text(text = stringResource(id = study.subtitle)) },
                            leadingContent = {
                                Icon(
                                    imageVector = study.icon,
                                    contentDescription = stringResource(id = study.title)
                                )
                            }, modifier = Modifier.clickable {
                                isStudyListVisible = false
                                val folder =
                                    lastSelectedFolder.takeIf { it >= 0 } ?: return@clickable
                                lastSelectedFolder = -1
                                navigator.navigate(
                                    StudyScreenDestination(folderId = folder, studyType = study)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}