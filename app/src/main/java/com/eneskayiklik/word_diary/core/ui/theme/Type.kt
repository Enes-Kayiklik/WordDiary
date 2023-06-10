package com.eneskayiklik.word_diary.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.eneskayiklik.word_diary.R

private val defaultType = Typography()

val Roboto = FontFamily(
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val JosefinSans = FontFamily(
    Font(R.font.josefin_sans_medium, FontWeight.Medium),
    Font(R.font.josefin_sans_light, FontWeight.Light),
    Font(R.font.josefin_sans_regular, FontWeight.Normal),
    Font(R.font.josefin_sans_bold, FontWeight.Bold)
)

val Quicksand = FontFamily(
    Font(R.font.quicksand_medium, FontWeight.Medium),
    Font(R.font.quicksand_light, FontWeight.Light),
    Font(R.font.quicksand_regular, FontWeight.Normal),
    Font(R.font.quicksand_bold, FontWeight.Bold)
)

val Caveat = FontFamily(
    Font(R.font.caveat_medium, FontWeight.Medium),
    Font(R.font.caveat_regular, FontWeight.Normal),
    Font(R.font.caveat_bold, FontWeight.Bold)
)

fun getTypography(fontFamily: FontFamily) = defaultType.copy(
    displayLarge = defaultType.displayLarge.copy(
        fontFamily = fontFamily
    ),
    displayMedium = defaultType.displayMedium.copy(
        fontFamily = fontFamily
    ),
    displaySmall = defaultType.displaySmall.copy(
        fontFamily = fontFamily
    ),
    headlineLarge = defaultType.headlineLarge.copy(
        fontFamily = fontFamily
    ),
    headlineSmall = defaultType.headlineSmall.copy(
        fontFamily = fontFamily
    ),
    headlineMedium = defaultType.headlineMedium.copy(
        fontFamily = fontFamily
    ),
    titleLarge = defaultType.titleLarge.copy(
        fontFamily = fontFamily
    ),
    titleMedium = defaultType.titleMedium.copy(
        fontFamily = fontFamily
    ),
    titleSmall = defaultType.titleSmall.copy(
        fontFamily = fontFamily
    ),
    bodyLarge = defaultType.bodyLarge.copy(
        fontFamily = fontFamily
    ),
    bodyMedium = defaultType.bodyMedium.copy(
        fontFamily = fontFamily
    ),
    bodySmall = defaultType.bodySmall.copy(
        fontFamily = fontFamily
    ),
    labelLarge = defaultType.labelLarge.copy(
        fontFamily = fontFamily
    ),
    labelMedium = defaultType.labelMedium.copy(
        fontFamily = fontFamily
    ),
    labelSmall = defaultType.labelSmall.copy(
        fontFamily = fontFamily
    )
)