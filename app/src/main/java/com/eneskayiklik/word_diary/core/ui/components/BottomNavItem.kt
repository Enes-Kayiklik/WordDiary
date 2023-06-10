package com.eneskayiklik.word_diary.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.eneskayiklik.word_diary.feature.destinations.DirectionDestination

data class BottomNavItem(
    val route: DirectionDestination,
    @StringRes val title: Int,
    @StringRes val contentDescription: Int,
    val icon: ImageVector? = null,
    @DrawableRes val resIcon: Int? = null
)
