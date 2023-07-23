package com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel

import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel.component.ColorPickerDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel.component.ColorStylePickerDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel.component.FontFamilyPickerDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.look_and_feel.component.ThemePickerDialog
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
@Destination(style = ScreensAnim::class)
fun ThemeScreen(
    navigator: DestinationsNavigator,
    viewModel: ThemeViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val userPrefs = viewModel.userPrefs.collectAsState(initial = UserPreference()).value
    val themePrefs = userPrefs.themePrefs

    val state = viewModel.state.collectAsState().value

    when (state.activeDialog) {
        ThemeDialogType.THEME_PICKER -> ThemePickerDialog(
            activeTheme = themePrefs.appTheme,
            onSelected = { viewModel.onEvent(ThemeEvent.PickTheme(it)) },
            onDismiss = { viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.NONE)) }
        )

        ThemeDialogType.COLOR_PICKER -> ColorPickerDialog(
            onColorSelected = { viewModel.onEvent(ThemeEvent.PickColor(it)) },
            onDismiss = { viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.NONE)) },
            defaultColors = state.themeColors
        )

        ThemeDialogType.COLOR_STYLE_PICKER -> ColorStylePickerDialog(
            activeStyle = themePrefs.colorStyle,
            onSelected = { viewModel.onEvent(ThemeEvent.PickColorStyle(it)) },
            onDismiss = { viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.NONE)) }
        )
        ThemeDialogType.FONT_FAMILY_PICKER -> FontFamilyPickerDialog(
            activeStyle = themePrefs.fontFamily,
            onSelected = { viewModel.onEvent(ThemeEvent.PickFontFamily(it)) },
            onDismiss = { viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.NONE)) }
        )

        else -> Unit
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.theme_and_colors),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = navigator::popBackStack) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button_desc)
                    )
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(
                vertical = 24.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item("theme_title") {
                Text(
                    text = stringResource(id = R.string.theme),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 56.dp)
                )
            }
            item("app_theme") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.app_theme))
                    },
                    supportingContent = {
                        Text(text = stringResource(id = themePrefs.appTheme.title))
                    }, leadingContent = {
                        Icon(imageVector = Icons.Filled.Brush, contentDescription = null)
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.THEME_PICKER))
                    }
                )
            }
            item("amoled_black") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.amoled_black))
                    },
                    supportingContent = {
                        Text(text = stringResource(id = R.string.amoled_black_desc))
                    }, leadingContent = {
                        Box(modifier = Modifier.size(24.dp))
                    }, trailingContent = {
                        Switch(
                            checked = themePrefs.isAmoledBlack,
                            onCheckedChange = {
                                viewModel.onEvent(ThemeEvent.OnAmoledBlack)
                            }
                        )
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.OnAmoledBlack)
                    }
                )
            }
            item("colorful_background") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.colorful_background))
                    },
                    supportingContent = {
                        Text(text = stringResource(id = R.string.colorful_background_desc))
                    }, leadingContent = {
                        Box(modifier = Modifier.size(24.dp))
                    }, trailingContent = {
                        Switch(
                            checked = themePrefs.colorfulBackground,
                            onCheckedChange = {
                                viewModel.onEvent(ThemeEvent.OnColorfulBackground)
                            }
                        )
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.OnColorfulBackground)
                    }
                )
            }

            item("font_family") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.font_family))
                    },
                    supportingContent = {
                        Text(text = themePrefs.fontFamily.title)
                    }, leadingContent = {
                        Box(modifier = Modifier.size(24.dp))
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.FONT_FAMILY_PICKER))
                    }
                )
            }

            item("colors_title") {
                Text(
                    text = stringResource(id = R.string.colors),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 56.dp)
                )
            }
            item("primary_color") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.primary_color))
                    },
                    supportingContent = {
                        Text(text = stringResource(id = R.string.primary_color_desc))
                    }, leadingContent = {
                        Icon(imageVector = Icons.Filled.Palette, contentDescription = null)
                    }, trailingContent = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(themePrefs.primaryColor))
                        )
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.COLOR_PICKER))
                    }
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) item("wallpaper_color") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.wallpaper_color))
                    },
                    supportingContent = {
                        Text(text = stringResource(id = R.string.wallpaper_color_desc))
                    }, leadingContent = {
                        Box(modifier = Modifier.size(24.dp))
                    }, trailingContent = {
                        Switch(
                            checked = themePrefs.extractWallpaperColor,
                            onCheckedChange = {
                                viewModel.onEvent(ThemeEvent.OnWallpaperColor)
                            }
                        )
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.OnWallpaperColor)
                    }
                )
            }

            item("random_color") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.random_color))
                    },
                    supportingContent = {
                        Text(text = stringResource(id = R.string.random_color_desc))
                    }, leadingContent = {
                        Box(modifier = Modifier.size(24.dp))
                    }, trailingContent = {
                        Switch(
                            checked = themePrefs.randomColor,
                            onCheckedChange = {
                                viewModel.onEvent(ThemeEvent.OnRandomColor)
                            }
                        )
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.OnRandomColor)
                    }
                )
            }

            item("palette_style") {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.palette_style))
                    },
                    supportingContent = {
                        Text(text = themePrefs.colorStyle.title)
                    }, leadingContent = {
                        Box(modifier = Modifier.size(24.dp))
                    }, modifier = Modifier.clickable {
                        viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.COLOR_STYLE_PICKER))
                    }
                )
            }
        }
    }
}