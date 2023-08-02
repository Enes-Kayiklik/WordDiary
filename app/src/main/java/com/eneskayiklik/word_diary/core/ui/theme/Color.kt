package com.eneskayiklik.word_diary.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.*

val Pink = Color(0xFF7D5260)
val Purple = Color(0xFFD0BCFF)
val PurpleVariant = Color(0xFF6650a4)
val Orange = Color(0xFFE87636)
val OrangeVariant = Color(0xFFFF5A26)
val Red = Color(0xFFFA2B5A)
val Blue = Color(0xFF1EA2E7)
val GreenYellow = Color(0xFFC2E71E)
val SuccessGreen = Color(0xFF00C797)

val DEFAULT_PRIMARY_COLOR = Color(0xFF00C853).toArgb()

@Composable
fun dynamicColorScheme(
    keyColor: Color,
    style: ColorStyle = ColorStyle.TonalSpot,
    isDark: Boolean = isSystemInDarkTheme(),
    isAmoledBlack: Boolean = false,
    colorfulBackground: Boolean = false,
    contrastLevel: Double = 0.0
): ColorScheme {
    val hct = Hct.fromInt(keyColor.toArgb())
    val colors = MaterialDynamicColors()
    val scheme = if (colorfulBackground) SchemeMonochrome(hct, isDark, contrastLevel)
    else when (style) {
        ColorStyle.TonalSpot -> SchemeTonalSpot(hct, isDark, contrastLevel)
        ColorStyle.Neutral -> SchemeNeutral(hct, isDark, contrastLevel)
        ColorStyle.Vibrant -> SchemeVibrant(hct, isDark, contrastLevel)
        ColorStyle.Expressive -> SchemeExpressive(hct, isDark, contrastLevel)
        ColorStyle.Rainbow -> SchemeRainbow(hct, isDark, contrastLevel)
        ColorStyle.FruitSalad -> SchemeFruitSalad(hct, isDark, contrastLevel)
        ColorStyle.Monochrome -> SchemeMonochrome(hct, isDark, contrastLevel)
        ColorStyle.Fidelity -> SchemeFidelity(hct, isDark, contrastLevel)
        ColorStyle.Content -> SchemeContent(hct, isDark, contrastLevel)
    }

    return if (isDark.not()) {
        lightColorScheme(
            background = Color(colors.background().getArgb(scheme)),
            error = Color(colors.error().getArgb(scheme)),
            errorContainer = Color(colors.errorContainer().getArgb(scheme)),
            inverseOnSurface = Color(colors.inverseOnSurface().getArgb(scheme)),
            inversePrimary = Color(colors.inversePrimary().getArgb(scheme)),
            inverseSurface = Color(colors.inverseSurface().getArgb(scheme)),
            onBackground = Color(colors.onBackground().getArgb(scheme)),
            onError = Color(colors.onError().getArgb(scheme)),
            onErrorContainer = Color(colors.onErrorContainer().getArgb(scheme)),
            onPrimary = Color(colors.onPrimary().getArgb(scheme)),
            onPrimaryContainer = Color(colors.onPrimaryContainer().getArgb(scheme)),
            onSecondary = Color(colors.onSecondary().getArgb(scheme)),
            onSecondaryContainer = Color(colors.onSecondaryContainer().getArgb(scheme)),
            onSurface = Color(colors.onSurface().getArgb(scheme)),
            onSurfaceVariant = Color(colors.onSurfaceVariant().getArgb(scheme)),
            onTertiary = Color(colors.onTertiary().getArgb(scheme)),
            onTertiaryContainer = Color(colors.onTertiaryContainer().getArgb(scheme)),
            outline = Color(colors.outline().getArgb(scheme)),
            outlineVariant = Color(colors.outlineVariant().getArgb(scheme)),
            primary = Color(colors.primary().getArgb(scheme)),
            primaryContainer = Color(colors.primaryContainer().getArgb(scheme)),
            scrim = Color(colors.scrim().getArgb(scheme)),
            secondary = Color(colors.secondary().getArgb(scheme)),
            secondaryContainer = Color(colors.secondaryContainer().getArgb(scheme)),
            surface = Color(colors.surface().getArgb(scheme)),
            surfaceTint = Color(colors.surfaceTint().getArgb(scheme)),
            surfaceVariant = Color(colors.surfaceVariant().getArgb(scheme)),
            tertiary = Color(colors.tertiary().getArgb(scheme)),
            tertiaryContainer = Color(colors.tertiaryContainer().getArgb(scheme))
        )
    } else {
        darkColorScheme(
            background = if (isAmoledBlack) Color.Black else Color(colors.background().getArgb(scheme)),
            error = Color(colors.error().getArgb(scheme)),
            errorContainer = Color(colors.errorContainer().getArgb(scheme)),
            inverseOnSurface = Color(colors.inverseOnSurface().getArgb(scheme)),
            inversePrimary = Color(colors.inversePrimary().getArgb(scheme)),
            inverseSurface = Color(colors.inverseSurface().getArgb(scheme)),
            onBackground = Color(colors.onBackground().getArgb(scheme)),
            onError = Color(colors.onError().getArgb(scheme)),
            onErrorContainer = Color(colors.onErrorContainer().getArgb(scheme)),
            onPrimary = Color(colors.onPrimary().getArgb(scheme)),
            onPrimaryContainer = Color(colors.onPrimaryContainer().getArgb(scheme)),
            onSecondary = Color(colors.onSecondary().getArgb(scheme)),
            onSecondaryContainer = Color(colors.onSecondaryContainer().getArgb(scheme)),
            onSurface = Color(colors.onSurface().getArgb(scheme)),
            onSurfaceVariant = Color(colors.onSurfaceVariant().getArgb(scheme)),
            onTertiary = Color(colors.onTertiary().getArgb(scheme)),
            onTertiaryContainer = Color(colors.onTertiaryContainer().getArgb(scheme)),
            outline = Color(colors.outline().getArgb(scheme)),
            outlineVariant = Color(colors.outlineVariant().getArgb(scheme)),
            primary = Color(colors.primary().getArgb(scheme)),
            primaryContainer = Color(colors.primaryContainer().getArgb(scheme)),
            scrim = Color(colors.scrim().getArgb(scheme)),
            secondary = Color(colors.secondary().getArgb(scheme)),
            secondaryContainer = Color(colors.secondaryContainer().getArgb(scheme)),
            surface = if (isAmoledBlack) Color.Black else Color(colors.surface().getArgb(scheme)),
            surfaceTint = Color(colors.surfaceTint().getArgb(scheme)),
            surfaceVariant = Color(colors.surfaceVariant().getArgb(scheme)),
            tertiary = Color(colors.tertiary().getArgb(scheme)),
            tertiaryContainer = Color(colors.tertiaryContainer().getArgb(scheme))
        )
    }
}