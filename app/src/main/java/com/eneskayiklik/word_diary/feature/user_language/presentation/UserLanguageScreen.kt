package com.eneskayiklik.word_diary.feature.user_language.presentation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Destination(style = ScreensAnim::class)
@Composable
fun UserLanguageScreen(
    navigator: DestinationsNavigator,
    viewModel: UserLanguageViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val userPrefs = viewModel.userPrefs.collectAsState(initial = UserPreference()).value
    val context = LocalContext.current

    BackHandler(userPrefs.userLanguage == UserLanguage.NOT_SPECIFIED, onBack = {
        viewModel.onEven(UiEvent.ShowToast(textRes = R.string.select_language_warning))
    })

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is UiEvent.ShowToast -> if (it.textRes != null) {
                    Toast.makeText(context, it.textRes, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.languages),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    if (userPrefs.userLanguage != UserLanguage.NOT_SPECIFIED) navigator.popBackStack()
                    else viewModel.onEven(UiEvent.ShowToast(textRes = R.string.select_language_warning))
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button_desc)
                    )
                }
            }, scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (userPrefs.userLanguage != UserLanguage.NOT_SPECIFIED) navigator.popBackStack()
                    else viewModel.onEven(UiEvent.ShowToast(textRes = R.string.select_language_warning))
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null
                )
            }
        },
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(
                bottom = 64.dp
            ),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.languages_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 64.dp, bottom = 8.dp)
                )
            }
            UserLanguage.values().forEach { lang ->
                if (lang.readable.isNotEmpty()) {
                    item(key = lang) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable { viewModel.selectUserLanguage(lang) }
                            .padding(horizontal = 16.dp), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lang.readable,
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            RadioButton(
                                selected = lang == userPrefs.userLanguage,
                                onClick = { viewModel.selectUserLanguage(lang) },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }
        }
    }
}