package com.eneskayiklik.word_diary.util.animation_converter

import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class DpConverter : TwoWayConverter<Dp, AnimationVector1D> {
    override val convertFromVector: (AnimationVector1D) -> Dp
        get() = {
            it.value.dp
        }

    override val convertToVector: (Dp) -> AnimationVector1D
        get() = {
            AnimationVector1D(it.value)
        }

}