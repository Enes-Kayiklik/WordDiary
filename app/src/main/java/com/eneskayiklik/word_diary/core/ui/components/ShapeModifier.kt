package com.eneskayiklik.word_diary.core.ui.components

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

fun Modifier.clipToMaterialShape(
    index: Int,
    lastIndex: Int,
    smallShape: CornerBasedShape,
    largeShape: CornerBasedShape
): Modifier {
    return clip(
        RoundedCornerShape(
            topStart = if (index == 0) largeShape.topStart else smallShape.topStart,
            topEnd = if (index == 0) largeShape.topEnd else smallShape.topEnd,
            bottomStart = if (index != 0 && index == lastIndex) largeShape.bottomStart else smallShape.bottomStart,
            bottomEnd = if (index != 0 && index == lastIndex) largeShape.bottomEnd else smallShape.bottomEnd
        )
    )
}