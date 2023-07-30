package com.eneskayiklik.word_diary.feature.settings.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.AppLanguage
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListOption
import com.maxkeppeler.sheets.list.models.ListSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLanguageDialog(
    selectedLang: AppLanguage,
    onSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    val options = remember {
        AppLanguage.values().mapNotNull {
            if (it.readable.isEmpty()) null
            else ListOption(
                titleText = it.readable,
                selected = it == selectedLang
            )
        }
    }

    ListDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest = { onDismiss() }),
        header = Header.Default(
            title = stringResource(id = R.string.app_language)
        ),
        selection = ListSelection.Single(
            showRadioButtons = true,
            options = options
        ) { _, option ->
            onSelected(AppLanguage.values().first { it.readable == option.titleText })
        }
    )
}