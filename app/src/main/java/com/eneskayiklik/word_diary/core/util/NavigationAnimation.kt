package com.eneskayiklik.word_diary.core.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
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

private const val SLIDE_ANIM_DIVIDE_OFFSET = 8

fun <T> getDefaultAnimationSpec(duration: Int = 300): TweenSpec<T> {
    return tween(
        easing = EaseInOutCirc,
        durationMillis = duration
    )
}

fun <T> getDefaultEnterAnim(duration: Int = 400): TweenSpec<T> {
    return tween(
        easing = CubicBezierEasing(.05f, .7f, .1f, 1f),
        durationMillis = duration
    )
}

fun <T> getDefaultExitAnim(duration: Int = 200): TweenSpec<T> {
    return tween(
        easing = CubicBezierEasing(.3f, 0f, .8f, .15f),
        durationMillis = duration
    )
}

@OptIn(ExperimentalAnimationApi::class)
private val bottomBarItems: List<Destination>
    get() = listOf(
        StatisticsScreenDestination,
        ListsScreenDestination,
        CalendarScreenDestination
    )

@ExperimentalAnimationApi
object ScreensAnim : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return when (targetState.appDestination()) {
            in bottomBarItems -> defaultFadeInAnim()
            else -> slideInAnim()
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return when (targetState.appDestination()) {
            in bottomBarItems -> defaultFadeOutAnim()
            else -> slideOutAnim()
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return when (initialState.appDestination()) {
            in bottomBarItems -> defaultFadeInAnim()
            else -> popSlideInAnim()
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return when (initialState.appDestination()) {
            in bottomBarItems -> defaultFadeOutAnim()
            else -> popSlideOutAnim()
        }
    }
}

fun slideInAnim(): EnterTransition = defaultFadeInAnim() + slideInHorizontally(
    animationSpec = getDefaultEnterAnim()
) { it.getPosition() }

fun slideOutAnim(): ExitTransition = defaultFadeOutAnim() + slideOutHorizontally(
    animationSpec = getDefaultExitAnim()
) { -it.getPosition() }

fun popSlideInAnim(): EnterTransition = defaultFadeInAnim() + slideInHorizontally(
    animationSpec = getDefaultEnterAnim()
) { -it.getPosition() }

fun popSlideOutAnim(): ExitTransition = defaultFadeOutAnim() + slideOutHorizontally(
    animationSpec = getDefaultExitAnim()
) { it.getPosition() }

fun defaultFadeInAnim(): EnterTransition = fadeIn(
    animationSpec = getDefaultEnterAnim(150)
)

fun defaultFadeOutAnim(): ExitTransition = fadeOut(
    animationSpec = getDefaultExitAnim(150)
)

private fun Int.getPosition() = div(SLIDE_ANIM_DIVIDE_OFFSET)