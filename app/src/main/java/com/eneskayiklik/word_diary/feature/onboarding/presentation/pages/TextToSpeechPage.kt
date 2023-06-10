package com.eneskayiklik.word_diary.feature.onboarding.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.util.GOOGLE_TTS
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.openLink
import com.eneskayiklik.word_diary.util.extensions.openTTSSettings
import com.eneskayiklik.word_diary.util.extensions.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToSpeechPage(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.onboarding_text_to_speech_title),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.onboarding_text_to_speech_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            item {
                Text(
                    text = stringResource(id = R.string.onboarding_text_to_speech_first_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.onboarding_text_to_speech_first_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { context.openLink(GOOGLE_TTS) }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_play),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(id = R.string.onboarding_text_to_speech_open_play_store))
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.onboarding_text_to_speech_second_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.onboarding_text_to_speech_second_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { context.openTTSSettings() }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(id = R.string.onboarding_text_to_speech_open_settings))
                }
            }
        }
    }
}