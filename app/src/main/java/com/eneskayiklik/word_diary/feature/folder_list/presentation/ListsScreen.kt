package com.eneskayiklik.word_diary.feature.folder_list.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.EmptyDataView
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.feature.destinations.WordListScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.SingleFolderRow
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)
@Destination(start = true, style = ScreensAnim::class)
@Composable
fun ListsScreen(
    navigator: DestinationsNavigator,
    viewModel: ListsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = viewModel.state.collectAsState().value

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
            CenterAlignedTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.destination_lists_title),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, scrollBehavior = scrollBehavior)
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