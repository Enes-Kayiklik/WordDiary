package com.eneskayiklik.word_diary.core.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
fun animateDoubleAsState(
    targetValue: Double,
    animationSpec: AnimationSpec<Double> = spring(),
    label: String = "DoubleAnimation",
    finishedListener: ((Double) -> Unit)? = null
): State<Double> {
    return animateValueAsState(
        targetValue,
        Double.VectorConverter,
        animationSpec,
        label = label,
        finishedListener = finishedListener
    )
}

val Double.Companion.VectorConverter: TwoWayConverter<Double, AnimationVector1D>
    get() = DoubleToVector

private val DoubleToVector: TwoWayConverter<Double, AnimationVector1D> =
    TwoWayConverter({ AnimationVector1D(it.toFloat()) }, { it.value.toDouble() })