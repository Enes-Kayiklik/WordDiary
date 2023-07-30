package com.eneskayiklik.word_diary.core.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.destinations.CalendarScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.ListsScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.SettingsScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.StatisticsScreenDestination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit,
    currentRoute: String?,
    showBottomBar: Boolean = true,
    bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            route = ListsScreenDestination,
            title = R.string.collections,
            icon = Icons.Outlined.Style,
            contentDescription = R.string.collections
        ),
        BottomNavItem(
            route = StatisticsScreenDestination,
            title = R.string.statistics,
            resIcon = R.drawable.ic_line_chart,
            contentDescription = R.string.statistics
        ),
        BottomNavItem(
            route = CalendarScreenDestination,
            title = R.string.calendar,
            icon = Icons.Outlined.CalendarMonth,
            contentDescription = R.string.calendar
        ),
        BottomNavItem(
            route = SettingsScreenDestination,
            title = R.string.settings,
            icon = Icons.Outlined.Settings,
            contentDescription = R.string.settings
        ),
    ),
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            WDBottomBar(
                showBottomBar = showBottomBar,
                bottomNavItems = bottomNavItems,
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        },
        modifier = modifier
    ) {
        content()
    }
}