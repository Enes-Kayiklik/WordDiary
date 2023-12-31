package com.eneskayiklik.word_diary.feature.create_folder.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.core.util.getDefaultAnimationSpec
import com.eneskayiklik.word_diary.util.extensions.pxToDp

@Composable
fun LanguageChip(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    title: String,
    icon: String,
    onLanguageSelected: () -> Unit
) {
    val context = LocalContext.current
    var parentSize by remember { mutableStateOf(IntSize(0, 0)) }

    var circularOffset by remember {
        mutableStateOf(
            Offset(
                parentSize.width / 2F,
                parentSize.height / 2F
            )
        )
    }
    val startAnim by animateFloatAsState(
        targetValue = if (isSelected) parentSize.width.pxToDp(context) * 2 else 0F,
        animationSpec = getDefaultAnimationSpec(500), label = "language_chip"
    )
    val borderAnim by animateColorAsState(
        targetValue = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
        animationSpec = getDefaultAnimationSpec(500), label = "language_chip_border"
    )

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    circularOffset = offset
                    onLanguageSelected()
                }
            }
            .onGloballyPositioned { parentSize = it.size }
            .then(
                if (parentSize.width > 0) Modifier.size(
                    parentSize.width.pxToDp(context).dp,
                    parentSize.height.pxToDp(context).dp
                ) else Modifier
            )
            .border(1.dp, borderAnim, MaterialTheme.shapes.small)
    ) {
        Box(
            modifier = Modifier
                .size(1.dp)
                .offset(
                    x = circularOffset.x
                        .pxToDp(context)
                        .dp,
                    y = circularOffset.y
                        .pxToDp(context)
                        .dp
                )
                .scale(startAnim)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )

        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}