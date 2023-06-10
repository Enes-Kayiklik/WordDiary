package com.eneskayiklik.word_diary.core.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.eneskayiklik.word_diary.feature.appDestination
import com.eneskayiklik.word_diary.feature.destinations.ListsScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.SettingsScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.StatisticsScreenDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

const val NAVIGATION_ANIMATION_STIFFNESS = 800F
private const val FADE_ANIM_STIFFNESS = Spring.StiffnessMedium
private const val SLIDE_ANIM_DIVIDE_OFFSET = 6

@ExperimentalAnimationApi
object ScreensAnim : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (targetState.appDestination()) {
            StatisticsScreenDestination, ListsScreenDestination, SettingsScreenDestination -> null
            else -> slideInAnim()
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            StatisticsScreenDestination, ListsScreenDestination, SettingsScreenDestination -> null
            else -> slideOutAnim()
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (initialState.appDestination()) {
            StatisticsScreenDestination, ListsScreenDestination, SettingsScreenDestination -> null
            else -> popSlideInAnim()
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (initialState.appDestination()) {
            StatisticsScreenDestination, ListsScreenDestination, SettingsScreenDestination -> null
            else -> popSlideOutAnim()
        }
    }
}

fun slideInAnim(): EnterTransition = fadeIn(
    animationSpec = spring(stiffness = FADE_ANIM_STIFFNESS)
) + slideInHorizontally(animationSpec = spring(stiffness = NAVIGATION_ANIMATION_STIFFNESS)) { it.getPosition() }

fun slideOutAnim(): ExitTransition = fadeOut(
    animationSpec = spring(stiffness = FADE_ANIM_STIFFNESS)
) + slideOutHorizontally(animationSpec = spring(stiffness = NAVIGATION_ANIMATION_STIFFNESS)) { -it.getPosition() }

fun popSlideInAnim(): EnterTransition = fadeIn(
    animationSpec = spring(stiffness = FADE_ANIM_STIFFNESS)
) + slideInHorizontally(animationSpec = spring(stiffness = NAVIGATION_ANIMATION_STIFFNESS)) { -it.getPosition() }

fun popSlideOutAnim(): ExitTransition = fadeOut(
    animationSpec = spring(stiffness = FADE_ANIM_STIFFNESS)
) + slideOutHorizontally(animationSpec = spring(stiffness = NAVIGATION_ANIMATION_STIFFNESS)) { it.getPosition() }

private fun Int.getPosition() = div(SLIDE_ANIM_DIVIDE_OFFSET)