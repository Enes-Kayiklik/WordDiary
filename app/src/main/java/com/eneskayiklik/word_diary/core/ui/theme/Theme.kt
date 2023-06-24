package com.eneskayiklik.word_diary.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.eneskayiklik.word_diary.core.data_store.data.AppTheme
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

@Composable
fun WordDiaryTheme(
    color: Color,
    isAmoledBlack: Boolean = false,
    colorfulBackground: Boolean = false,
    appTheme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    colorStyle: ColorStyle = ColorStyle.TONAL_SPOT,
    fontFamily: FontFamily = FontFamily.SansSerif,
    content: @Composable () -> Unit
) {
    var surfaceColor by remember { mutableStateOf(Color.Transparent) }

    val view = LocalView.current
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

    val palettes = color.toTonalPalettes(
        when (colorStyle) {
            ColorStyle.SPRITZ -> PaletteStyle.Spritz
            ColorStyle.VIBRANT -> PaletteStyle.Vibrant
            ColorStyle.EXPRESSIVE -> PaletteStyle.Expressive
            ColorStyle.RAINBOW -> PaletteStyle.Rainbow
            ColorStyle.FRUIT_SALAD -> PaletteStyle.FruitSalad
            else -> PaletteStyle.TonalSpot
        }
    )

    CompositionLocalProvider(LocalTonalPalettes provides palettes) {
        val colorScheme = dynamicColorScheme(
            isLight = isLight,
            isAmoledBlack = isAmoledBlack,
            colorfulBackground = colorfulBackground
        )

        MaterialTheme(
            colorScheme = colorScheme,
            typography = getTypography(fontFamily),
            content = content
        )

        surfaceColor = colorScheme.surfaceColorAtElevation(3.dp)
    }
}