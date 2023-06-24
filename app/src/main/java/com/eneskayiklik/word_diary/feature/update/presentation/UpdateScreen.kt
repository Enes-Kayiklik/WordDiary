package com.eneskayiklik.word_diary.feature.update.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data.model.InitResponse
import com.eneskayiklik.word_diary.core.ui.components.WaveShape
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.WORD_DIARY_PLAY_STORE
import com.eneskayiklik.word_diary.util.extensions.finishActivity
import com.eneskayiklik.word_diary.util.extensions.openLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Destination(
    style = ScreensAnim::class,
    navArgsDelegate = UpdateScreenArgs::class
)
fun UpdateScreen(
    navigator: DestinationsNavigator,
    viewModel: UpdateViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    fun onBackPressed() {
        if (state.isForceUpdate) context.finishActivity() else navigator.navigateUp()
    }

    BackHandler(onBack = ::onBackPressed)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.destination_update_available),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, navigationIcon = {
                IconButton(onClick = ::onBackPressed) {
                    val icon = if (state.isForceUpdate) Icons.Default.Close
                    else Icons.Default.ArrowBack
                    Icon(imageVector = icon, contentDescription = null)
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Outlined.Verified,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = 96.dp.toPx()
                        translationY = 96.dp.toPx()
                    }
                    .alpha(.1F)
                    .align(Alignment.TopEnd)
                    .size(248.dp)
            )

            Icon(
                imageVector = Icons.Outlined.WorkspacePremium,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = -48.dp.toPx()
                        translationY = 124.dp.toPx()
                    }
                    .alpha(.1F)
                    .align(Alignment.CenterStart)
                    .size(172.dp)
            )

            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = 92.dp.toPx()
                        translationY = (-24).dp.toPx()
                    }
                    .alpha(.1F)
                    .align(Alignment.BottomEnd)
                    .size(224.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.destination_update_available_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(WaveShape())
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = WaveShape()
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = state.latestVersionName,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Text(
                text = state.features,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1F))
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(onClick = ::onBackPressed, modifier = Modifier.weight(1F)) {
                    Text(text = stringResource(id = R.string.close))
                }

                Button(
                    onClick = { context.openLink(WORD_DIARY_PLAY_STORE) },
                    modifier = Modifier.weight(1F)
                ) {
                    Text(text = stringResource(id = R.string.update))
                }
            }
        }
    }
}

data class UpdateScreenArgs(
    val config: InitResponse
)