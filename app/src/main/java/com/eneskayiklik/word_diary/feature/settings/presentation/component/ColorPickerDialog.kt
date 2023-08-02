package com.eneskayiklik.word_diary.feature.settings.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.MultipleColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(
    onColorSelected: (color: Int) -> Unit,
    defaultColors: List<Int> = emptyList(),
    onDismiss: () -> Unit
) {
    ColorDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest = { onDismiss() }),
        selection = ColorSelection(
            onSelectNone = onDismiss,
            onSelectColor = onColorSelected,
        ),
        config = ColorConfig(
            templateColors = MultipleColors.ColorsInt(defaultColors),
            allowCustomColorAlphaValues = false
        )
    )
}