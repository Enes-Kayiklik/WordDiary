package com.eneskayiklik.word_diary.feature.word_list.presentation.component.word_queue

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.feature.word_list.presentation.WordListEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordQueueView(
    startIndex: Int,
    words: List<WordEntity>,
    modifier: Modifier = Modifier,
    onAction: (word: WordEntity, action: SwipeAction) -> Unit,
    onWordListEvent: (WordListEvent) -> Unit,
    userLangIso: String?,
    folderLangIso: String?,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    DisposableEffect(key1 = Unit) {

        onDispose {
            onWordListEvent(WordListEvent.OnSpeakEvent())
        }
    }

    HorizontalPager(
        modifier = modifier,
        key = { words[it].wordId },
        state = rememberPagerState(startIndex, pageCount = { words.size })
    ) { index ->
        val currentWord = words[index]
        val interactionSource = remember { MutableInteractionSource() }
        LazyColumn(
            contentPadding = PaddingValues(vertical = 24.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = currentWord.meaning,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onWordListEvent(
                                    WordListEvent.OnSpeakEvent(
                                        firstSentence = currentWord.meaning,
                                        firstSource = folderLangIso
                                    )
                                )
                            })
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    currentWord.synonyms.forEachIndexed { index, synonymSentence ->
                        Text(
                            text = synonymSentence.synonym,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.outline
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    onWordListEvent(
                                        WordListEvent.OnSpeakEvent(
                                            firstSentence = synonymSentence.synonym,
                                            firstSource = folderLangIso
                                        )
                                    )
                                })
                        )
                        if (index != currentWord.synonyms.lastIndex) Text(
                            text = ", ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }
            }
            item {
                Text(
                    text = currentWord.word,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onWordListEvent(
                                    WordListEvent.OnSpeakEvent(
                                        firstSentence = currentWord.word,
                                        firstSource = userLangIso
                                    )
                                )
                            })
                )
            }

            currentWord.samples.forEach { sampleSentence ->
                item {
                    var isExpanded by remember { mutableStateOf(false) }
                    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180F else 0F)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                isExpanded = isExpanded.not()
                            }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotation)
                        )
                        Column(
                            modifier = Modifier.weight(1F)
                        ) {
                            Text(
                                text = sampleSentence.sampleLearnedLang,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically { -it },
                                exit = shrinkVertically(shrinkTowards = Alignment.Top) { -it }
                            ) {
                                Text(
                                    text = sampleSentence.sampleNativeLang,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Outlined.VolumeUp,
                            contentDescription = null,
                            modifier = Modifier.clickable(
                                onClick = {
                                    onWordListEvent(
                                        WordListEvent.OnSpeakEvent(
                                            firstSentence = sampleSentence.sampleLearnedLang,
                                            firstSource = folderLangIso,
                                            secondSentence = sampleSentence.sampleNativeLang.takeIf { isExpanded },
                                            secondSource = userLangIso
                                        )
                                    )
                                },
                                indication = rememberRipple(radius = 12.dp),
                                interactionSource = remember {
                                    MutableInteractionSource()
                                })
                        )
                    }
                }
            }
        }
    }
}