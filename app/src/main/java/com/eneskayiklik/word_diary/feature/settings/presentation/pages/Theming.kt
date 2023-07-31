package com.eneskayiklik.word_diary.feature.settings.presentation.pages

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Highlight
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsDialog
import com.eneskayiklik.word_diary.feature.settings.presentation.SettingsEvent

@Composable
fun ThemingPage(
    modifier: Modifier = Modifier,
    userPrefs: () -> UserPreference,
    onEvent: (SettingsEvent) -> Unit
) {
    val themePrefs by remember(key1 = userPrefs) { mutableStateOf(userPrefs().themePrefs) }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier
    ) {
        /*item("theme_title") {
            Text(
                text = stringResource(id = R.string.theme),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(start = 56.dp)
            )
        }*/
        item("app_theme") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.app_theme))
                },
                supportingContent = {
                    Text(text = stringResource(id = themePrefs.appTheme.title))
                }, leadingContent = {
                    Icon(imageVector = Icons.Outlined.DarkMode, contentDescription = null)
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.ShowDialog(SettingsDialog.SelectTheme))
                }
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
                    Icon(imageVector = Icons.Outlined.Brush, contentDescription = null)
                }, trailingContent = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.ShowDialog(SettingsDialog.SelectPrimaryColor))
                }
            )
        }
        item("pure_black") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.amoled_black))
                },
                supportingContent = {
                    Text(text = stringResource(id = R.string.amoled_black_desc))
                }, leadingContent = {
                    Icon(imageVector = Icons.Outlined.Highlight, contentDescription = null)
                }, trailingContent = {
                    Switch(
                        checked = themePrefs.isAmoledBlack,
                        onCheckedChange = {
                            onEvent(SettingsEvent.UpdateAmoledBlack)
                        }
                    )
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.UpdateAmoledBlack)
                }
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) item("material_you") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.wallpaper_color))
                },
                supportingContent = {
                    Text(text = stringResource(id = R.string.wallpaper_color_desc))
                }, leadingContent = {
                    Icon(imageVector = Icons.Outlined.Wallpaper, contentDescription = null)
                }, trailingContent = {
                    Switch(
                        checked = themePrefs.extractWallpaperColor,
                        onCheckedChange = {
                            onEvent(SettingsEvent.OnWallpaperColor)
                        }
                    )
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.OnWallpaperColor)
                }
            )
        }
        item("monochrome") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.monochrome))
                },
                supportingContent = {
                    Text(text = stringResource(id = R.string.monochrome_desc))
                }, leadingContent = {
                    Icon(imageVector = Icons.Outlined.Contrast, contentDescription = null)
                }, trailingContent = {
                    Switch(
                        checked = themePrefs.colorfulBackground,
                        onCheckedChange = {
                            onEvent(SettingsEvent.UpdateMonochrome)
                        }
                    )
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.UpdateMonochrome)
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
                    Icon(imageVector = Icons.Outlined.FormatSize, contentDescription = null)
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.ShowDialog(SettingsDialog.SelectFont))
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
                    Icon(imageVector = Icons.Outlined.Palette, contentDescription = null)
                }, modifier = Modifier.clickable {
                    onEvent(SettingsEvent.ShowDialog(SettingsDialog.SelectPaletteStyle))
                }
            )
        }

        /*item("colors_title") {
            Text(
                text = stringResource(id = R.string.colors),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(start = 56.dp)
            )
        }*/

        /*item("random_color") {
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
                            //viewModel.onEvent(ThemeEvent.OnRandomColor)
                        }
                    )
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.OnRandomColor)
                }
            )
        }*/
    }
}