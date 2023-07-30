package com.eneskayiklik.word_diary.core.util

import androidx.compose.animation.AnimatedContentTransitionScope
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
import com.eneskayiklik.word_diary.feature.destinations.CalendarScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.Destination
import com.eneskayiklik.word_diary.feature.destinations.ListsScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.StatisticsScreenDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

const val NAVIGATION_ANIMATION_STIFFNESS = 800F
private const val FADE_ANIM_STIFFNESS = Spring.StiffnessMedium
private const val SLIDE_ANIM_DIVIDE_OFFSET = 6

@OptIn(ExperimentalAnimationApi::class)
private val bottomBarItems: List<Destination>
    get() = listOf(
        StatisticsScreenDestination,
        ListsScreenDestination,
        CalendarScreenDestination
    )

@ExperimentalAnimationApi
object ScreensAnim : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (targetState.appDestination()) {
            in bottomBarItems -> null
            else -> slideInAnim()
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            in bottomBarItems-> null
            else -> slideOutAnim()
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (initialState.appDestination()) {
            in bottomBarItems -> null
            else -> popSlideInAnim()
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (initialState.appDestination()) {
            in bottomBarItems -> null
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