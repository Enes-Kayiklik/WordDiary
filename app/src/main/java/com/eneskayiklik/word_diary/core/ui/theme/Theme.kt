package com.eneskayiklik.word_diary.core.ui.theme

import android.app.Activity
import android.app.WallpaperManager
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.eneskayiklik.word_diary.core.data_store.data.AppTheme
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun WordDiaryTheme(
    color: Color,
    isAmoledBlack: Boolean = false,
    colorfulBackground: Boolean = false,
    materialYou: Boolean = false,
    appTheme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    colorStyle: ColorStyle = ColorStyle.TonalSpot,
    fontFamily: FontFamily = FontFamily.SansSerif,
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    val keyColor = when {
        materialYou && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> colorResource(id = android.R.color.system_accent1_500)
        materialYou && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
            val extractedColor = WallpaperManager.getInstance(view.context)
                .getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
                ?.primaryColor
                ?.toArgb()

            if (extractedColor != null) Color(extractedColor) else color
        }

        else -> color
    }

    var surfaceColor by remember { mutableStateOf(Color.Transparent) }

    val isLight = when (appTheme) {
        AppTheme.DARK -> false
        AppTheme.LIGHT -> true
        else -> isSystemInDarkTheme().not()
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = surfaceColor.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(
                window,
                view
            ).apply {
                isAppearanceLightStatusBars = isLight
                isAppearanceLightNavigationBars = isLight
            }
        }
    }

    val colorScheme = dynamicColorScheme(
        isDark = isLight.not(),
        isAmoledBlack = isAmoledBlack,
        colorfulBackground = colorfulBackground,
        keyColor = keyColor,
        style = colorStyle
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(fontFamily),
        content = content
    )

    surfaceColor = colorScheme.surfaceColorAtElevation(3.dp)
}