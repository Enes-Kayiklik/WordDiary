package com.eneskayiklik.word_diary.feature.word_list.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.OnLifecycleEvent
import com.eneskayiklik.word_diary.core.ui.components.BasicDialog
import com.eneskayiklik.word_diary.core.ui.components.ad.SmallNativeAdView
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.EmptyDataView
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.destinations.StudyScreenDestination
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import com.eneskayiklik.word_diary.feature.word_list.presentation.component.FilterMenu
import com.eneskayiklik.word_diary.feature.word_list.presentation.component.ListsStatisticView
import com.eneskayiklik.word_diary.feature.word_list.presentation.component.WordListItem
import com.eneskayiklik.word_diary.feature.word_list.presentation.component.word_queue.WordQueueView
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalSheetApi::class,
    ExperimentalFoundationApi::class
)
@Destination(style = ScreensAnim::class)
@Composable
fun WordListScreen(
    folderId: Int,
    navigator: DestinationsNavigator,
    viewModel: WordListViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.collectAsState().value
    var isFilterMenuVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val itemsScale by animateFloatAsState(
        targetValue = if (state.isWordQueueVisible) 0F else 1F
    )
    var isStudyListVisible by remember { mutableStateOf(false) }

    val firstItemVisible by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex == 0 }
    }

    if (state.isDialogActive) {
        when (state.dialogType) {
            WordListDialogType.DELETE -> BasicDialog(
                onDismiss = { viewModel.onEvent(WordListEvent.OnShowDialog(WordListDialogType.NONE)) },
                onConfirm = { viewModel.onEvent(WordListEvent.OnDeleteWord) },
                title = stringResource(id = R.string.delete_word_title),
                description = stringResource(id = R.string.delete_word_desc),
                confirmText = stringResource(id = R.string.delete_confirm),
                dismissText = stringResource(id = R.string.dialog_cancel),
                icon = Icons.Outlined.Delete
            )

            WordListDialogType.DELETE_LIST -> BasicDialog(
                onDismiss = { viewModel.onEvent(WordListEvent.OnShowDialog(WordListDialogType.NONE)) },
                onConfirm = { viewModel.onEvent(WordListEvent.OnDeleteList) },
                title = stringResource(id = R.string.delete_option_title),
                description = stringResource(id = R.string.delete_option_desc),
                confirmText = stringResource(id = R.string.delete_confirm),
                dismissText = stringResource(id = R.string.dialog_cancel),
                icon = Icons.Outlined.Delete
            )

            else -> Unit
        }
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> viewModel.onEvent(WordListEvent.OnAdEvent(true))
            Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(WordListEvent.OnAdEvent(false))
            else -> Unit
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(it.visuals)
                UiEvent.HideSnackbar -> snackbarHostState.currentSnackbarData?.dismiss()
                is UiEvent.OnNavigate -> navigator.navigate(it.route)
                is UiEvent.ShowToast -> if (it.textRes != null) {
                    Toast.makeText(context, it.textRes, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, it.text ?: "", Toast.LENGTH_LONG).show()
                }

                UiEvent.ClearBackstack -> navigator.navigateUp()
                else -> Unit
            }
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
                    onClick = { viewModel.onEvent(WordListEvent.CreateWord) },
                    modifier = Modifier.scale(itemsScale)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(id = R.string.add_list_content_desc)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    action = {
                        TextButton(
                            onClick = { viewModel.onEvent(WordListEvent.OnUndoDelete) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = SnackbarDefaults.actionColor
                            ),
                        ) { Text(snackbarData.visuals.actionLabel ?: "") }
                    },
                    content = {
                        Text(snackbarData.visuals.message)
                    },
                    dismissAction = {
                        IconButton(onClick = { viewModel.onEvent(UiEvent.HideSnackbar) }) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                        }
                    })
            }
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = state.title,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    when {
                        state.isWordQueueVisible -> viewModel.onEvent(WordListEvent.OnWordClick(null))
                        else -> navigator.navigateUp()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.back_button_desc
                        )
                    )
                }
            }, actions = {
                IconButton(
                    onClick = { isFilterMenuVisible = true },
                    modifier = Modifier.scale(itemsScale)
                ) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = null)
                }

                /*WordListDropdownMenu(
                    expanded = isMenuExpanded,
                    onFilter = { isFilterMenuVisible = isFilterMenuVisible.not() },
                    onQuiz = { isStudyListVisible = isStudyListVisible.not() },
                    onDelete = { viewModel.onEvent(WordListEvent.OnShowDialog(WordListDialogType.DELETE_LIST)) },
                    onDismiss = { isMenuExpanded = false }
                )*/
            }, scrollBehavior = scrollBehavior)
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.showEmptyLayout -> {
                EmptyDataView(
                    modifier = Modifier
                        .fillMaxSize(),
                    title = stringResource(id = R.string.oops),
                    subtitle = stringResource(id = R.string.empty_word_desc),
                    icon = Icons.Outlined.FolderOpen,
                    actionText = stringResource(id = R.string.destination_create_word_title),
                    onAction = { viewModel.onEvent(WordListEvent.CreateWord) }
                )
            }

            else -> LazyColumn(
                contentPadding = padding + PaddingValues(vertical = 24.dp, horizontal = 16.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item("simple_statistics") {
                    ListsStatisticView(
                        statistic = state.listStatistic,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp),
                        onStudyClick = { isStudyListVisible = true }
                    )
                }
                state.words.forEachIndexed { index, word ->
                    item(key = word.wordId) {
                        WordListItem(
                            word = word,
                            modifier = Modifier
                                .animateItemPlacement()
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp)),
                            onAction = { action ->
                                viewModel.onEvent(
                                    WordListEvent.OnWordAction(
                                        word = word,
                                        action = action
                                    )
                                )
                            },
                            onClick = {
                                viewModel.onEvent(
                                    WordListEvent.OnWordClick(
                                        word = word
                                    )
                                )
                            }
                        )
                    }

                    if (index == 0 && state.nativeAd != null) {
                        item(key = "ad_view") {
                            SmallNativeAdView(
                                nativeAd = state.nativeAd,
                                showActionButton = true,
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = state.isWordQueueVisible,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it }
        ) {
            WordQueueView(
                startIndex = state.initialWordIndex,
                words = state.words,
                userLangIso = state.userLangIso,
                folderLangIso = state.folderLangIso,
                onWordListEvent = viewModel::onEvent,
                onAction = { word, action ->
                    viewModel.onEvent(
                        WordListEvent.OnWordAction(
                            word = word,
                            action = action
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.surface),
                onDismiss = {
                    viewModel.onEvent(
                        WordListEvent.OnWordClick(word = null)
                    )
                }
            )
        }

        ModalSheet(
            visible = isFilterMenuVisible,
            onVisibleChange = { isFilterMenuVisible = it },
            backgroundColor = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.large
        ) {
            BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

            FilterMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
                selectedFilterType = state.filterType,
                selectedSortType = state.sortType,
                selectedSortDirection = state.sortDirection,
                onFilterSelected = { viewModel.onEvent(WordListEvent.OnFilterSelected(it)) },
                onSortSelected = { viewModel.onEvent(WordListEvent.OnSortSelected(it)) },
                onSortDirectionSelected = {
                    viewModel.onEvent(
                        WordListEvent.OnSortDirectionSelected(
                            it
                        )
                    )
                }
            )
        }

        ModalSheet(
            visible = isStudyListVisible,
            onVisibleChange = { isStudyListVisible = it },
            backgroundColor = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.large
        ) {
            BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = WindowInsets.navigationBars.asPaddingValues()
            ) {
                StudyType.values().forEach { study ->
                    item {
                        ListItem(
                            headlineText = { Text(text = stringResource(id = study.title)) },
                            supportingText = { Text(text = stringResource(id = study.subtitle)) },
                            leadingContent = {
                                Icon(
                                    imageVector = study.icon,
                                    contentDescription = stringResource(id = study.title)
                                )
                            }, modifier = Modifier.clickable {
                                isStudyListVisible = false
                                navigator.navigate(
                                    StudyScreenDestination(folderId = folderId, studyType = study)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}