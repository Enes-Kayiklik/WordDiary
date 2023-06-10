package com.eneskayiklik.word_diary.core.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eneskayiklik.word_diary.R
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
            title = R.string.destination_lists,
            icon = Icons.Outlined.Style,
            contentDescription = R.string.destination_lists_content_desc
        ),
        BottomNavItem(
            route = StatisticsScreenDestination,
            title = R.string.destination_statistics,
            resIcon = R.drawable.ic_line_chart,
            contentDescription = R.string.destination_statistics_content_desc
        ),
        BottomNavItem(
            route = SettingsScreenDestination,
            title = R.string.destination_settings,
            icon = Icons.Outlined.Settings,
            contentDescription = R.string.destination_settings_content_desc
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