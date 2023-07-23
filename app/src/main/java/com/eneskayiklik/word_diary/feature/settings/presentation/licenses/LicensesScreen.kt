package com.eneskayiklik.word_diary.feature.settings.presentation.licenses

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Destination(style = ScreensAnim::class)
@Composable
fun LicensesScreen(
    navigator: DestinationsNavigator,
    viewModel: LicensesViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.destination_licenses_title),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = navigator::popBackStack) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button_desc)
                    )
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it,
            modifier = Modifier.fillMaxSize()
        ) {
            state.libs.forEach { lib ->
                item {
                    ListItem(headlineContent = {
                        Text(
                            text = lib.title,
                            fontWeight = FontWeight.SemiBold
                        )
                    }, supportingContent = {
                        if (lib.author != null) Text(text = lib.author)
                        if (lib.license != null) Text(text = lib.license)
                    }, trailingContent = { if (lib.version != null) Text(text = lib.version) },
                        modifier = Modifier.clickable { }
                    )
                }
            }
        }
    }
}