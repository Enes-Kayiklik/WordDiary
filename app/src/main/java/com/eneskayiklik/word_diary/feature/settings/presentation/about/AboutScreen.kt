package com.eneskayiklik.word_diary.feature.settings.presentation.about

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.BuildConfig
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.components.WaveShape
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.feature.settings.presentation.about.component.AboutAppContent
import com.eneskayiklik.word_diary.feature.settings.presentation.about.component.AboutCard
import com.eneskayiklik.word_diary.feature.settings.presentation.about.component.AboutDeveloperContent
import com.eneskayiklik.word_diary.feature.settings.presentation.about.component.AboutDropdownMenu
import com.eneskayiklik.word_diary.feature.settings.presentation.about.component.SupportDevelopmentActions
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.plus
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class,
    ExperimentalAnimationApi::class
)
@Destination(style = ScreensAnim::class)
@Composable
fun AboutScreen(
    navigator: DestinationsNavigator,
    viewModel: AboutViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var isExpanded by remember { mutableStateOf(false) }
    var atEnd by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = atEnd) {
        if (atEnd.not()) {
            delay(450)
            atEnd = atEnd.not()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.about),
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
            }, actions = {
                IconButton(onClick = { isExpanded = isExpanded.not() }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(id = R.string.more_button_desc)
                    )
                }
                AboutDropdownMenu(expanded = isExpanded, navigator = navigator) {
                    isExpanded = isExpanded.not()
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(
                horizontal = 16.dp,
                vertical = 24.dp
            ),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AboutCard(
                    cardTitle = stringResource(id = R.string.about_the_app),
                    sectionTitle = {
                        Column {
                            Text(
                                text = BuildConfig.VERSION_NAME,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .align(Alignment.End)
                            )
                            Text(
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    },
                    actionContent = {
                        AboutAppContent()
                    },
                    sectionIcon = {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(WaveShape())
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = WaveShape()
                                )
                                .clickable {
                                    atEnd = atEnd.not()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            val image = rememberAnimatedVectorPainter(
                                AnimatedImageVector.animatedVectorResource(
                                    id = R.drawable.ic_launcher_animated
                                ), atEnd = atEnd
                            )
                            Icon(
                                painter = image,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(.5F),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                AboutCard(
                    cardTitle = stringResource(id = R.string.development_and_design),
                    sectionTitle = {
                        Text(
                            text = stringResource(id = R.string.developer_name),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    },
                    sectionDescription = {
                        Text(
                            text = stringResource(id = R.string.developer_info_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 24.dp),
                            textAlign = TextAlign.Center
                        )
                    },
                    actionContent = {
                        AboutDeveloperContent()
                    },
                    sectionIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_developer),
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                AboutCard(
                    cardTitle = stringResource(id = R.string.support_development),
                    sectionTitle = {
                        Text(
                            text = stringResource(id = R.string.support_development_title),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    },
                    sectionDescription = {
                        Text(
                            text = stringResource(id = R.string.support_development_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 24.dp),
                            textAlign = TextAlign.Center
                        )
                    },
                    sectionIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Face,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    actionContent = {
                        SupportDevelopmentActions()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}