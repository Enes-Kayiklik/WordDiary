package com.eneskayiklik.word_diary.feature.emoji_picker.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.feature.emoji_picker.presentation.component.EmojiTab
import com.eneskayiklik.word_diary.util.MAX_EMOJI_COUNT_LINE
import com.eneskayiklik.word_diary.core.util.components.gridItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun EmojiPickerSheet(
    resultNavigator: ResultBackNavigator<String>,
    viewModel: EmojiPickerViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val firstVisibleItemIndex by remember {
        derivedStateOf {
            viewModel.tabIndexFromFirstVisibleIdemIndex(lazyColumnState.firstVisibleItemIndex)
        }
    }

    LaunchedEffect(key1 = Unit) {
        try {
            keyboardController?.hide()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Surface(
        modifier = Modifier.animateContentSize(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(400.dp)
        ) {
            if (state.tabs.isNotEmpty()) Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                state.tabs.forEachIndexed { index, item ->
                    EmojiTab(
                        icon = item,
                        isSelected = firstVisibleItemIndex == index,
                        modifier = Modifier
                            .weight(1F)
                            .aspectRatio(1F, true)
                    ) {
                        coroutineScope.launch {
                            lazyColumnState.scrollToItem(
                                viewModel.lineIndexFromTabIndex(
                                    index
                                )
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = lazyColumnState,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                state.emojiList.forEach { emojiTree ->
                    item {
                        Text(
                            text = stringResource(id = emojiTree.title.value),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                    gridItems(
                        data = emojiTree.emojis,
                        columnCount = MAX_EMOJI_COUNT_LINE,
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        modifier = Modifier.fillMaxSize(),
                    ) { emoji ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1F, true)
                                .clip(MaterialTheme.shapes.small)
                                .clickable {
                                    resultNavigator.navigateBack(emoji.emoji)
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji.emoji,
                                fontSize = 28.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}