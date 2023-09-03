package com.eneskayiklik.word_diary.feature.settings.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    val options = remember {
        AppLanguage.values().map {
            val title = if (it.stringRes != null) context.getString(it.stringRes) else it.readable

            ListOption(
                titleText = title,
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
            onSelected(AppLanguage.values().firstOrNull { it.readable == option.titleText }
                ?: AppLanguage.FOLLOW_SYSTEM)
        }
    )
}