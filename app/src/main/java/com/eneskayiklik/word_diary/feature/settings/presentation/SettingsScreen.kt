package com.eneskayiklik.word_diary.feature.settings.presentation

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddToDrive
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.feature.destinations.AboutScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.AppLanguageScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.GeneralScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.ThemeScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.UserLanguageScreenDestination
import com.eneskayiklik.word_diary.feature.settings.presentation.premium.BuyPremiumButton
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Destination(style = ScreensAnim::class)
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.destination_settings_title),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it,
            modifier = Modifier.fillMaxSize()
        ) {
            item(key = "buy_premium") {
                BuyPremiumButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
                    onBuyNow = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.coming_soon),
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onRestore = {

                    }
                )
            }
            item(key = "general") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.settings_general_title)) },
                    supportingText = { Text(text = stringResource(id = R.string.settings_general_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.ToggleOn,
                            contentDescription = stringResource(id = R.string.settings_general_desc)
                        )
                    },
                    modifier = Modifier.clickable { navigator.navigate(GeneralScreenDestination) }
                )
            }
            item(key = "theme") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.settings_theming_title)) },
                    supportingText = { Text(text = stringResource(id = R.string.settings_theming_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = stringResource(id = R.string.settings_theming_desc)
                        )
                    },
                    modifier = Modifier.clickable { navigator.navigate(ThemeScreenDestination) }
                )
            }

            item(key = "personalize") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.settings_personalize_title)) },
                    supportingText = { Text(text = stringResource(id = R.string.settings_personalize_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        Toast.makeText(
                            context,
                            context.getString(R.string.coming_soon),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }

            item(key = "backup") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.settings_backup_title)) },
                    supportingText = { Text(text = stringResource(id = R.string.settings_backup_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.AddToDrive,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        Toast.makeText(
                            context,
                            context.getString(R.string.coming_soon),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }

            item(key = "app_language") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.settings_app_language_title)) },
                    supportingText = { Text(text = stringResource(id = R.string.settings_app_language_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Language,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable { navigator.navigate(AppLanguageScreenDestination) }
                )
            }

            item(key = "user_language") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.settings_user_language_title)) },
                    supportingText = { Text(text = stringResource(id = R.string.settings_user_language_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Translate,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable { navigator.navigate(UserLanguageScreenDestination) }
                )
            }

            item(key = "about") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.settings_about_title)) },
                    supportingText = {
                        Text(
                            text = stringResource(
                                R.string.settings_about_desc,
                                stringResource(id = R.string.app_name)
                            )
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.settings_about_desc)
                        )
                    },
                    modifier = Modifier.clickable { navigator.navigate(AboutScreenDestination) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}