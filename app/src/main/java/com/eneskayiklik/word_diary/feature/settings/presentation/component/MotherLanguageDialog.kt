package com.eneskayiklik.word_diary.feature.settings.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListOption
import com.maxkeppeler.sheets.list.models.ListSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotherLanguageDialog(
    selectedLang: UserLanguage,
    onSelected: (UserLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    val options = remember {
        UserLanguage.values().mapNotNull {
            if (it.readable.isEmpty() || it.isoCode.isEmpty()) null
            else ListOption(
                titleText = it.readable,
                selected = it == selectedLang
            )
        }
    }

    ListDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest = { onDismiss() }),
        header = Header.Default(
            title = stringResource(id = R.string.user_language)
        ),
        selection = ListSelection.Single(
            showRadioButtons = true,
            options = options
        ) { _, option ->
            onSelected(UserLanguage.values().first { it.readable == option.titleText })
        }
    )
}