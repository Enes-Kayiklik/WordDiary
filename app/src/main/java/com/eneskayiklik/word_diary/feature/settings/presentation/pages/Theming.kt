package com.eneskayiklik.word_diary.feature.settings.presentation.pages

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
@Composable
fun ThemingPage(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 24.dp
        ),
        modifier = modifier
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
                    Text(text = ""/*stringResource(id = themePrefs.appTheme.title)*/)
                }, leadingContent = {
                    Icon(imageVector = Icons.Filled.Brush, contentDescription = null)
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.THEME_PICKER))
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
                        checked = false/*themePrefs.isAmoledBlack*/,
                        onCheckedChange = {
                            //viewModel.onEvent(ThemeEvent.OnAmoledBlack)
                        }
                    )
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.OnAmoledBlack)
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
                        checked = false/*themePrefs.colorfulBackground*/,
                        onCheckedChange = {
                            //viewModel.onEvent(ThemeEvent.OnColorfulBackground)
                        }
                    )
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.OnColorfulBackground)
                }
            )
        }

        item("font_family") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.font_family))
                },
                supportingContent = {
                    Text(text = ""/*themePrefs.fontFamily.title*/)
                }, leadingContent = {
                    Box(modifier = Modifier.size(24.dp))
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.FONT_FAMILY_PICKER))
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
                            .background(MaterialTheme.colorScheme.primary/*Color(themePrefs.primaryColor)*/)
                    )
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.COLOR_PICKER))
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
                        checked = false/*themePrefs.extractWallpaperColor*/,
                        onCheckedChange = {
                            //viewModel.onEvent(ThemeEvent.OnWallpaperColor)
                        }
                    )
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.OnWallpaperColor)
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
                        checked = false/*themePrefs.randomColor*/,
                        onCheckedChange = {
                            //viewModel.onEvent(ThemeEvent.OnRandomColor)
                        }
                    )
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.OnRandomColor)
                }
            )
        }

        item("palette_style") {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.palette_style))
                },
                supportingContent = {
                    Text(text = ""/*themePrefs.colorStyle.title*/)
                }, leadingContent = {
                    Box(modifier = Modifier.size(24.dp))
                }, modifier = Modifier.clickable {
                    //viewModel.onEvent(ThemeEvent.ShowDialog(ThemeDialogType.COLOR_STYLE_PICKER))
                }
            )
        }
    }
}