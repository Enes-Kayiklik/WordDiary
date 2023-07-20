package com.eneskayiklik.word_diary.feature.onboarding.presentation.pages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectPage(
    modifier: Modifier = Modifier,
    userLanguage: UserLanguage,
    selectUserLanguage: (UserLanguage) -> Unit,
    onEvent: (UiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    BackHandler(userLanguage == UserLanguage.NOT_SPECIFIED, onBack = {
        onEvent(UiEvent.ShowToast(textRes = R.string.select_language_warning))
    })

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.user_language),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.languages_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 64.dp,
                        bottom = 8.dp,
                        top = 8.dp
                    )
                )
            }
            UserLanguage.values().forEach { lang ->
                if (lang.readable.isNotEmpty()) {
                    item(key = lang) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable { selectUserLanguage(lang) }
                            .padding(horizontal = 16.dp), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lang.readable,
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            RadioButton(
                                selected = lang == userLanguage,
                                onClick = { selectUserLanguage(lang) },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }
        }
    }
}