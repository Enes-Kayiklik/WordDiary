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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.destinations.AboutScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.AppLanguageScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.BackupScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.GeneralScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.PaywallScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.ThemeScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.UserLanguageScreenDestination
import com.eneskayiklik.word_diary.feature.settings.presentation.premium.BuyPremiumButton
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Destination(style = ScreensAnim::class)
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                UiEvent.ClearBackstack -> navigator.navigateUp()

                is UiEvent.ShowToast -> {
                    if (it.text != null) {
                        Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
                    } else if (it.textRes != null) {
                        Toast.makeText(context, it.textRes, Toast.LENGTH_LONG).show()
                    }
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.settings),
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
            if (WordDiaryApp.hasPremium.not()) item(key = "buy_premium") {
                BuyPremiumButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
                    onBuyNow = {
                        navigator.navigate(PaywallScreenDestination)
                    },
                    onRestore = viewModel::restorePurchase
                )
            }
            item(key = "general") {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.general)) },
                    supportingContent = { Text(text = stringResource(id = R.string.general_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.ToggleOn,
                            contentDescription = stringResource(id = R.string.general_desc)
                        )
                    },
                    modifier = Modifier.clickable { navigator.navigate(GeneralScreenDestination) }
                )
            }
            item(key = "theme") {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.theme_and_colors)) },
                    supportingContent = { Text(text = stringResource(id = R.string.theme_and_colors_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = stringResource(id = R.string.theme_and_colors_desc)
                        )
                    },
                    modifier = Modifier.clickable { navigator.navigate(ThemeScreenDestination) }
                )
            }

            /*item(key = "personalize") {
                ListItem(
                    headlineText = { Text(text = stringResource(id = R.string.personalize)) },
                    supportingText = { Text(text = stringResource(id = R.string.personalize_desc)) },
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
            }*/

            item(key = "backup") {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.backup_and_restore)) },
                    supportingContent = { Text(text = stringResource(id = R.string.backup_and_restore_desc)) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.AddToDrive,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        navigator.navigate(BackupScreenDestination)
                    }
                )
            }

            item(key = "app_language") {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.app_language)) },
                    supportingContent = { Text(text = stringResource(id = R.string.app_language_desc)) },
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
                    headlineContent = { Text(text = stringResource(id = R.string.user_language)) },
                    supportingContent = { Text(text = stringResource(id = R.string.user_language_desc)) },
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
                    headlineContent = { Text(text = stringResource(id = R.string.about)) },
                    supportingContent = {
                        Text(
                            text = stringResource(
                                R.string.about_desc,
                                stringResource(id = R.string.app_name)
                            )
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.about_the_app)
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