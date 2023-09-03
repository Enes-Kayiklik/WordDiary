package com.eneskayiklik.word_diary.feature.settings.presentation.pages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.BuildConfig
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.components.WaveShape
import com.eneskayiklik.word_diary.feature.destinations.LicensesScreenDestination
import com.eneskayiklik.word_diary.feature.settings.presentation.component.ContactButton
import com.eneskayiklik.word_diary.util.PRIVACY
import com.eneskayiklik.word_diary.util.TELEGRAM_CHANNEL
import com.eneskayiklik.word_diary.util.TELEGRAM_CHANNEL_REPORT_BUGS_TOPIC
import com.eneskayiklik.word_diary.util.TERMS
import com.eneskayiklik.word_diary.util.WORD_DIARY_CROWDIN
import com.eneskayiklik.word_diary.util.WORD_DIARY_GITHUB
import com.eneskayiklik.word_diary.util.WORD_DIARY_PLAY_STORE
import com.eneskayiklik.word_diary.util.extensions.openLink
import com.eneskayiklik.word_diary.util.extensions.shareAppLink
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalLayoutApi::class, ExperimentalAnimationApi::class)
@Composable
fun AboutPage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                supportingContent = {
                    Text(text = BuildConfig.VERSION_NAME)
                },
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(WaveShape())
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = WaveShape()
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_foreground),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.5F),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(vertical = 4.dp)
            )
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                maxItemsInEachRow = 4,
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                ContactButton(icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_github),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, title = stringResource(id = R.string.github), onClick = {
                    context.openLink(WORD_DIARY_GITHUB)
                })

                ContactButton(icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_telegram),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, title = stringResource(id = R.string.telegram), onClick = {
                    context.openLink(TELEGRAM_CHANNEL)
                })

                ContactButton(icon = {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, title = stringResource(id = R.string.share), onClick = {
                    context.shareAppLink()
                })

                ContactButton(icon = {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, title = stringResource(id = R.string.rate), onClick = {
                    context.openLink(WORD_DIARY_PLAY_STORE)
                })

                ContactButton(icon = {
                    Icon(
                        imageVector = Icons.Outlined.BugReport,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, title = stringResource(id = R.string.report_bugs), onClick = {
                    context.openLink(TELEGRAM_CHANNEL_REPORT_BUGS_TOPIC)
                })

                ContactButton(icon = {
                    Icon(
                        imageVector = Icons.Outlined.Translate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, title = stringResource(id = R.string.translate), onClick = {
                    context.openLink(WORD_DIARY_CROWDIN)
                })
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.destination_licenses))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.LocalLibrary,
                            contentDescription = null,
                        )
                    }, trailingContent = {
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                        )
                    }, colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .5F),
                        headlineColor = MaterialTheme.colorScheme.onSurface,
                        supportingColor = MaterialTheme.colorScheme.onSurface
                    ), modifier = Modifier
                        .clip(
                            MaterialTheme.shapes.medium.copy(
                                bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
                                bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
                            )
                        )
                        .clickable { navigator.navigate(LicensesScreenDestination) }
                )

                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.privacy_policy))
                    }, leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Policy,
                            contentDescription = null,
                        )
                    }, trailingContent = {
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                        )
                    }, colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .5F),
                        headlineColor = MaterialTheme.colorScheme.onSurface,
                        supportingColor = MaterialTheme.colorScheme.onSurface
                    ), modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .clickable {
                            context.openLink(PRIVACY)
                        }
                )

                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.terms_conditions))
                    }, leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                        )
                    }, trailingContent = {
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                        )
                    }, colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .5F),
                        headlineColor = MaterialTheme.colorScheme.onSurface,
                        supportingColor = MaterialTheme.colorScheme.onSurface
                    ), modifier = Modifier
                        .clip(
                            MaterialTheme.shapes.medium.copy(
                                topStart = MaterialTheme.shapes.extraSmall.topStart,
                                topEnd = MaterialTheme.shapes.extraSmall.topEnd
                            )
                        )
                        .clickable { context.openLink(TERMS) }
                )
            }
        }

        item {
            Text(
                text = "Designed and developed by Enes Kayıklık in Turkey with ❤️",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp, vertical = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}