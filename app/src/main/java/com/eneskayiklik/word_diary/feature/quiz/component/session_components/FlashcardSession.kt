package com.eneskayiklik.word_diary.feature.quiz.component.session_components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eneskayiklik.swiper.SwipedOutDirection
import com.eneskayiklik.swiper.Swiper
import com.eneskayiklik.swiper.rememberSwiperController
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.util.animation_converter.DpConverter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ColumnScope.FlashcardSession(
    items: List<WordEntity>,
    onWordSwiped: (word: WordEntity, direction: SwipedOutDirection, timeSpent: Long) -> Unit,
    onItemVisible: (WordEntity?) -> Unit,
    modifier: Modifier = Modifier,
    cardModifier: Modifier = Modifier
) {
    var initialTime by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

    val swiperController = rememberSwiperController()
    var swipeDirection by remember { mutableStateOf<SwipedOutDirection?>(null) }
    val activeDirection by remember { derivedStateOf { swipeDirection } }

    Swiper(
        items = items,
        swiperController = swiperController,
        onItemRemoved = { item, direction ->
            if (swipeDirection != null) {
                onWordSwiped(item, direction, System.currentTimeMillis() - initialTime)
            }
            initialTime = System.currentTimeMillis()
            swipeDirection = null
        },
        onDirectionChanged = {
            swipeDirection = it
        },
        onItemVisible = onItemVisible,
        modifier = modifier,
        onEmpty = { }
    ) { item ->
        var isWordVisible by remember { mutableStateOf(false) }
        val arrangementAnimation by animateDpAsState(
            targetValue = if (isWordVisible) 8.dp else 0.dp,
            animationSpec = tween(300)
        )
        Box(
            modifier = cardModifier
                .align(Alignment.Center)
                .clickable {
                    isWordVisible = isWordVisible.not()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(arrangementAnimation)
            ) {
                Text(text = item.meaning, style = MaterialTheme.typography.headlineMedium)
                AnimatedVisibility(
                    visible = isWordVisible,
                    enter = expandHorizontally(animationSpec = tween(300)) + expandVertically(
                        animationSpec = tween(300)
                    ),
                    exit = shrinkHorizontally(animationSpec = tween(300)) + shrinkVertically(
                        animationSpec = tween(300)
                    )
                ) {
                    Divider(modifier = Modifier.fillMaxWidth())
                }
                AnimatedVisibility(
                    visible = isWordVisible,
                    enter = expandVertically(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300))
                ) {
                    Text(text = item.word, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }

    AnimatedContent(
        targetState = activeDirection,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .align(Alignment.CenterHorizontally)
    ) { activeDir ->
        val transition = rememberInfiniteTransition()

        val position by transition.animateValue(
            initialValue = (-4).dp,
            targetValue = 4.dp,
            typeConverter = DpConverter(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (activeDir) {
                SwipedOutDirection.LEFT -> {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .offset(x = position)
                    )
                    Text(
                        text = "I am still learning this word",
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }

                SwipedOutDirection.RIGHT -> {
                    Text(
                        text = "I remember this word",
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .offset(x = position)
                    )
                }

                else -> Text(
                    text = "",
                    modifier = Modifier,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}