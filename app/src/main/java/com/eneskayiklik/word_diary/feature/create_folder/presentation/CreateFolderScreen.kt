package com.eneskayiklik.word_diary.feature.create_folder.presentation

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.feature.create_folder.presentation.component.SelectEmojiView
import com.eneskayiklik.word_diary.feature.create_folder.presentation.component.SelectedIndicator
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.core.util.getDefaultAnimationSpec
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import com.eneskayiklik.word_diary.feature.create_folder.presentation.component.LanguageChip
import com.eneskayiklik.word_diary.feature.destinations.EmojiPickerSheetDestination

data class CreateFolderScreenArgs(
    val folderId: Int
)

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class, ExperimentalLayoutApi::class
)
@Destination(
    style = ScreensAnim::class,
    navArgsDelegate = CreateFolderScreenArgs::class
)
@Composable
fun CreateFolderScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<EmojiPickerSheetDestination, String>,
    viewModel: CreateFolderViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = viewModel.state.collectAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        delay(200)
        try {
            focusRequester.requestFocus()
            keyboardController?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                UiEvent.ClearBackstack -> navigator.navigateUp()

                is UiEvent.ShowToast -> {
                    try {
                        keyboardController?.hide()
                        if (it.text != null) {
                            Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
                        } else if (it.textRes != null) {
                            Toast.makeText(context, it.textRes, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                else -> Unit
            }
        }
    }

    resultRecipient.onNavResult { resul ->
        when (resul) {
            NavResult.Canceled -> Unit
            is NavResult.Value -> viewModel.onEmojiSelected(resul.value)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(CreateFolderEvent.CreateFolder) },
                modifier = Modifier
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = stringResource(id = R.string.add_list_content_desc)
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.create_collection),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.buy_premium_content_desc
                        )
                    )
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item("write_title") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SelectEmojiView(
                        selectedEmoji = state.selectedEmoji,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(50.dp)
                    ) {
                        navigator.navigate(EmojiPickerSheetDestination)
                    }
                    OutlinedTextField(
                        value = state.folderName,
                        onValueChange = viewModel::onFolderNameChanged,
                        label = { Text(text = stringResource(id = R.string.collection_name)) },
                        placeholder = { Text(text = stringResource(id = R.string.collection_name_hint)) },
                        trailingIcon = {
                            Text(
                                text = state.counterText,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        },
                        supportingText = {
                            Box(modifier = Modifier.animateContentSize()) {
                                if (state.hasNameError) Text(
                                    text = stringResource(id = R.string.empty_field_warning),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )
                }
            }

            item("pick_color") {
                Text(
                    text = stringResource(id = R.string.select_color),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.colors.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(MaterialTheme.shapes.large)
                                .background(item)
                                .clickable { viewModel.onColorSelected(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = state.selectedColorIndex == index,
                                enter = scaleIn(getDefaultAnimationSpec(500)),
                                exit = scaleOut(getDefaultAnimationSpec(500))
                            ) {
                                SelectedIndicator(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            if (state.showLangSelection) item("pick_language") {
                Text(
                    text = stringResource(id = R.string.pick_language),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.pick_language_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.languages.forEach { item ->
                        LanguageChip(
                            isSelected = item == state.selectedLanguage,
                            title = item.readable,
                            icon = item.flagUnicode,
                            onLanguageSelected = {
                                viewModel.onLanguageSelected(item)
                            },
                            modifier = Modifier.clip(MaterialTheme.shapes.small)
                        )
                    }
                }
            }
        }
    }
}