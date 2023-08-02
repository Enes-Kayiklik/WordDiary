package com.eneskayiklik.word_diary.feature.settings.presentation

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.settings.presentation.component.AppLanguageDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.component.MotherLanguageDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.component.ColorPickerDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.component.ColorStylePickerDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.component.FontFamilyPickerDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.component.ThemePickerDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.pages.AboutPage
import com.eneskayiklik.word_diary.feature.settings.presentation.pages.CloudSyncPage
import com.eneskayiklik.word_diary.feature.settings.presentation.pages.GeneralPage
import com.eneskayiklik.word_diary.feature.settings.presentation.pages.ThemingPage
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Destination(style = ScreensAnim::class)
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    var selectedPage by remember { mutableIntStateOf(0) }

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

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { pagerState.currentPage }.collectLatest {
            selectedPage = it
        }
    }

    if (state.dialogType != SettingsDialog.None) {
        when (state.dialogType) {
            SettingsDialog.SelectAppLanguage -> AppLanguageDialog(
                selectedLang = state.userPrefs.appLanguage,
                onDismiss = {
                    viewModel.onEvent(SettingsEvent.ShowDialog(SettingsDialog.None))
                }, onSelected = { lang ->
                    viewModel.onEvent(SettingsEvent.SetAppLanguage(lang))
                }
            )

            SettingsDialog.SelectMotherLanguage -> MotherLanguageDialog(
                selectedLang = state.userPrefs.userLanguage,
                onDismiss = {
                    viewModel.onEvent(SettingsEvent.ShowDialog(SettingsDialog.None))
                }, onSelected = { lang ->
                    viewModel.onEvent(SettingsEvent.SetMotherLanguage(lang))
                }
            )

            SettingsDialog.SelectTheme -> ThemePickerDialog(
                activeTheme = state.userPrefs.themePrefs.appTheme,
                onSelected = { viewModel.onEvent(SettingsEvent.PickTheme(it)) },
                onDismiss = { viewModel.onEvent(SettingsEvent.ShowDialog(SettingsDialog.None)) }
            )

            SettingsDialog.SelectPrimaryColor -> ColorPickerDialog(
                onColorSelected = { viewModel.onEvent(SettingsEvent.PickColor(it)) },
                onDismiss = { viewModel.onEvent(SettingsEvent.ShowDialog(SettingsDialog.None)) },
                defaultColors = state.themeColors
            )

            SettingsDialog.SelectPaletteStyle -> ColorStylePickerDialog(
                activeStyle = state.userPrefs.themePrefs.colorStyle,
                onSelected = { viewModel.onEvent(SettingsEvent.PickColorStyle(it)) },
                onDismiss = { viewModel.onEvent(SettingsEvent.ShowDialog(SettingsDialog.None)) }
            )

            SettingsDialog.SelectFont -> FontFamilyPickerDialog(
                activeStyle = state.userPrefs.themePrefs.fontFamily,
                onSelected = { viewModel.onEvent(SettingsEvent.PickFontFamily(it)) },
                onDismiss = { viewModel.onEvent(SettingsEvent.ShowDialog(SettingsDialog.None)) }
            )

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = state.pages[selectedPage].title),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = TITLE_LETTER_SPACING,
                        fontSize = 20.sp
                    )
                }, navigationIcon = {
                    IconButton(onClick = navigator::navigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedPage]),
                        shape = RoundedCornerShape(
                            topStart = 3.dp,
                            topEnd = 3.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp,
                        )
                    )
                },
                divider = { },
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ) {
                state.pages.forEachIndexed { index, page ->
                    Tab(selected = index == selectedPage, onClick = {
                        if (index != selectedPage) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    }, icon = {
                        val icon = if (index == selectedPage) page.selectedIcon
                        else page.unselectedIcon
                        Icon(imageVector = icon, contentDescription = null)
                    })
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1F),
                key = { i -> i }) { page ->
                when (state.pages[page]) {
                    SettingsPage.General -> GeneralPage(
                        modifier = Modifier.fillMaxSize(),
                        userPrefs = { state.userPrefs },
                        onEvent = viewModel::onEvent
                    )

                    SettingsPage.Theming -> ThemingPage(
                        modifier = Modifier.fillMaxSize(),
                        userPrefs = { state.userPrefs },
                        onEvent = viewModel::onEvent
                    )

                    SettingsPage.SyncData -> CloudSyncPage(modifier = Modifier.fillMaxSize())
                    SettingsPage.Notification -> Box(modifier = Modifier.fillMaxSize())
                    SettingsPage.About -> AboutPage(modifier = Modifier.fillMaxSize())
                }
            }
        }

        /*LazyColumn(
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
                    headlineText = { Text(text = stringResource(id = R.string.general)) },
                    supportingText = { Text(text = stringResource(id = R.string.general_desc)) },
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
                    headlineText = { Text(text = stringResource(id = R.string.theme_and_colors)) },
                    supportingText = { Text(text = stringResource(id = R.string.theme_and_colors_desc)) },
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
                    headlineText = { Text(text = stringResource(id = R.string.backup_and_restore)) },
                    supportingText = { Text(text = stringResource(id = R.string.backup_and_restore_desc)) },
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
                    headlineText = { Text(text = stringResource(id = R.string.app_language)) },
                    supportingText = { Text(text = stringResource(id = R.string.app_language_desc)) },
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
                    headlineText = { Text(text = stringResource(id = R.string.user_language)) },
                    supportingText = { Text(text = stringResource(id = R.string.user_language_desc)) },
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
                    headlineText = { Text(text = stringResource(id = R.string.about)) },
                    supportingText = {
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
        }*/
    }
}