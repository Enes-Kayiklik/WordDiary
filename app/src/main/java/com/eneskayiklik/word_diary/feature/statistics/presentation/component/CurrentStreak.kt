package com.eneskayiklik.word_diary.feature.statistics.presentation.component

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R

@Composable
fun CurrentStreakView(
    modifier: Modifier = Modifier,
    newWords: String,
    studyTime: String,
    @StringRes studyTimeFormatter: Int,
    studySessions: String,
    streakCount: Int,
    @StringRes streakFormatter: Int,
    newWordProgress: Float
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1F), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            StatisticsHeaderItem(
                modifier = Modifier.weight(1F),
                title = stringResource(id = R.string.statistics_new_words),
                subTitle = newWords,
                leadingContent = {
                    val progress by animateFloatAsState(targetValue = newWordProgress)
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 4.dp,
                        strokeCap = StrokeCap.Round,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(.3F)
                    )
                }
            )
            StatisticsHeaderItem(
                modifier = Modifier.weight(1F),
                title = stringResource(id = R.string.statistics_study_time),
                subTitle = stringResource(id = studyTimeFormatter, studyTime),
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
        }
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
        )
        Column(modifier = Modifier.weight(1F), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            StatisticsHeaderItem(
                modifier = Modifier.weight(1F),
                title = stringResource(id = R.string.statistics_current_streak),
                subTitle = stringResource(id = streakFormatter, streakCount),
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
            StatisticsHeaderItem(
                modifier = Modifier.weight(1F),
                title = stringResource(id = R.string.statistics_study_sessions),
                subTitle = studySessions,
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Architecture,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun StatisticsHeaderItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    leadingContent: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent()
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(text = subTitle, style = MaterialTheme.typography.labelLarge)
        }
    }
}