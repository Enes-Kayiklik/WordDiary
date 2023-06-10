package com.eneskayiklik.word_diary.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.eneskayiklik.word_diary.core.util.ClearRippleTheme
import com.eneskayiklik.word_diary.core.util.popSlideInAnim
import com.eneskayiklik.word_diary.core.util.slideOutAnim

@Composable
fun WDBottomBar(
    showBottomBar: Boolean,
    bottomNavItems: List<BottomNavItem>,
    currentRoute: String?,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    AnimatedVisibility(
        visible = showBottomBar,
        enter = popSlideInAnim(),
        exit = slideOutAnim()
    ) {
        CompositionLocalProvider(LocalRippleTheme provides ClearRippleTheme) {
            NavigationBar(modifier = modifier) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            if (item.icon != null) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(
                                        id = item.contentDescription
                                    )
                                )
                            } else if (item.resIcon != null) {
                                Icon(
                                    painter = painterResource(id = item.resIcon),
                                    contentDescription = stringResource(
                                        id = item.contentDescription
                                    )
                                )
                            }
                        },
                        label = { Text(text = stringResource(id = item.title)) },
                        selected = currentRoute == item.route.route,
                        onClick = { if (currentRoute != item.route.route) onNavigate(item.route.route) }
                    )
                }
            }
        }
    }
}