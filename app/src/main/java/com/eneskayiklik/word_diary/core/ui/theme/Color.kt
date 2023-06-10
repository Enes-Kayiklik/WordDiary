package com.eneskayiklik.word_diary.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kyant.monet.a1
import com.kyant.monet.a2
import com.kyant.monet.a3
import com.kyant.monet.n1
import com.kyant.monet.n2

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
    isLight: Boolean = isSystemInDarkTheme().not(),
    isAmoledBlack: Boolean = false,
    colorfulBackground: Boolean = false
): ColorScheme {
    return if (isLight) {
        lightColorScheme(
            background = if (colorfulBackground) 98.a2 else 99.n1,
            inverseOnSurface = 95.n1,
            inversePrimary = 80.a1,
            inverseSurface = 20.n1,
            onBackground = 10.n1,
            onPrimary = 100.a1,
            onPrimaryContainer = 10.a1,
            onSecondary = 100.a2,
            onSecondaryContainer = 10.a2,
            onSurface = 10.n1,
            onSurfaceVariant = 30.n2,
            onTertiary = 100.a3,
            onTertiaryContainer = 10.a3,
            outline = 50.n2,
            outlineVariant = 80.n2,
            primary = 40.a1,
            primaryContainer = 90.a1,
            scrim = 0.n1,
            secondary = 40.a2,
            secondaryContainer = 90.a2,
            surface = (if (colorfulBackground) 98.a2 else 99.n1).copy(.97F),
            surfaceVariant = 90.n2,
            tertiary = 40.a3,
            tertiaryContainer = 90.a3
        )
    } else {
        darkColorScheme(
            background = if (isAmoledBlack) 0.n1 else if (colorfulBackground) 10.a1 else 10.n1,
            inverseOnSurface = 20.n1,
            inversePrimary = 40.a1,
            inverseSurface = 90.n1,
            onBackground = 90.n1,
            onPrimary = 20.a1,
            onPrimaryContainer = 90.a1,
            onSecondary = 20.a2,
            onSecondaryContainer = 90.a2,
            onSurface = 90.n1,
            onSurfaceVariant = 80.n2,
            onTertiary = 20.a3,
            onTertiaryContainer = 90.a3,
            outline = 60.n2,
            outlineVariant = 30.n2,
            primary = 80.a1,
            primaryContainer = 30.a1,
            scrim = 0.n1,
            secondary = 80.a2,
            secondaryContainer = 30.a2,
            surface = if (isAmoledBlack) 0.n1
            else if (colorfulBackground) 10.a1.copy(.97F)
            else 10.n1.copy(.97F),
            surfaceVariant = 30.n2,
            tertiary = 80.a3,
            tertiaryContainer = 30.a3
        )
    }
}