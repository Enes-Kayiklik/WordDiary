package com.eneskayiklik.swiper

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange

enum class SwipedOutDirection {
    LEFT, RIGHT
}

@Composable
inline fun <reified T> Swiper(
    items: List<T>,
    onItemRemoved: (T, SwipedOutDirection) -> Unit,
    crossinline onDirectionChanged: (SwipedOutDirection?) -> Unit = { },
    crossinline onItemVisible: (T?) -> Unit = { },
    onEmpty: () -> Unit = {},
    swiperController: SwiperController = rememberSwiperController(),
    stackCount: Int = 2,
    modifier: Modifier = Modifier,
    crossinline content: @Composable BoxScope.(T) -> Unit
) {
    val list = items.take(stackCount).reversed()

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        LaunchedEffect(key1 = list) {
            onItemVisible(list.lastOrNull())
        }

        list.forEachIndexed { index, item ->
            key(item) {
                val cardController = rememberCardController()
                if (index == list.lastIndex) {
                    swiperController.currentCardController = cardController
                }
                if (cardController.isCardOut().not()) {
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        cardController.onDragEnd()
                                    },
                                    onDragCancel = {
                                        cardController.onDragCancel()
                                    },
                                    onDrag = { change, dragAmount ->
                                        if (change.positionChange() != Offset.Zero) change.consume()
                                        cardController.onDrag(dragAmount)
                                        onDirectionChanged(cardController.swipingDirection)
                                    }
                                )
                            }
                            .graphicsLayer(
                                translationX = cardController.cardX,
                                translationY = cardController.cardY,
                                rotationZ = cardController.rotation,
                            )
                    ) {
                        content(item)
                    }
                } else {
                    cardController.swipedOutDirection?.let { outDirection ->
                        onItemRemoved(item, outDirection)
                        if (items.isEmpty()) {
                            onEmpty()
                        }
                    }
                }
            }
        }
    }
}