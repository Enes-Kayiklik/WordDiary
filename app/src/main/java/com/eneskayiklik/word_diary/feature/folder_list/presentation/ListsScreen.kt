package com.eneskayiklik.word_diary.feature.folder_list.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.components.StatusSearchBar
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.EmptyDataView
import com.eneskayiklik.word_diary.feature.destinations.WordListScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.SingleFolderRow
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class
)
@Destination(start = true, style = ScreensAnim::class)
@Composable
fun ListsScreen(
    navigator: DestinationsNavigator,
    viewModel: ListsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = viewModel.state.collectAsState().value
    var query by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = isSearchActive) {
        if (isSearchActive) return@LaunchedEffect
        try {
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(FolderListEvent.AddFolder) },
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(id = R.string.add_list_content_desc)
                )
            }
        },
        topBar = {
            StatusSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { },
                placeholder = { Text(text = "Search items")},
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
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null
                    )
                },
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(4) { idx ->
                            val resultText = "Suggestion $idx"
                            ListItem(
                                headlineText = { Text(resultText) },
                                supportingText = { Text("Additional info") },
                                leadingContent = {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.clickable {
                                    query = resultText
                                    isSearchActive = false
                                }
                            )
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
                subtitle = stringResource(id = R.string.empty_folder_desc),
                icon = Icons.Outlined.Folder,
                showArrow = true
            )

            else -> LazyColumn(
                contentPadding = it + PaddingValues(top = 24.dp, bottom = 64.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                state.folders.forEach { folder ->
                    item(key = folder.folder.folderId) {
                        SingleFolderRow(folderWithCount = folder, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(WordListScreenDestination(folderId = folder.folder.folderId))
                            }
                        )
                        Spacer(modifier = Modifier.height(12495.dp))
                    }
                }
            }

            /*else -> LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = it + PaddingValues(vertical = 24.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                state.folders.forEach { folder ->
                    item(key = folder.folder.folderId) {
                        SingleFolderItem(folderWithCount = folder, modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                navigator.navigate(WordListScreenDestination(folderId = folder.folder.folderId))
                            }
                            .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                }
            }*/
        }
    }
}