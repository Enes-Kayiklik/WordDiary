package com.eneskayiklik.word_diary.feature.statistics.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R

@Composable
fun GeneralStatisticsView(
    modifier: Modifier = Modifier,
    learningWord: Int,
    completeLearned: Int,
    studyTime: String,
    @StringRes studyTimeFormatter: Int,
    studySessions: Int,
    startOfLearning: String,
    streakCount: Int,
    @StringRes streakFormatter: Int
) {
    val learningWordText =
        "$learningWord " + if (learningWord <= 1) stringResource(id = R.string.word_singular)
        else stringResource(id = R.string.word_plural)

    val completeLearnedText =
        "$completeLearned " + if (completeLearned <= 1) stringResource(id = R.string.word_singular)
        else stringResource(id = R.string.word_plural)

    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GeneralStatisticsItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.learning),
            valueText = learningWordText,
            leadingIcon = Icons.Outlined.Book
        )
        Divider(modifier = Modifier.fillMaxWidth())
        GeneralStatisticsItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.learned),
            valueText = completeLearnedText,
            leadingIcon = Icons.Outlined.StarBorder
        )
        Divider(modifier = Modifier.fillMaxWidth())
        GeneralStatisticsItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.best_streak),
            valueText = stringResource(id = streakFormatter, streakCount),
            leadingIcon = Icons.Outlined.LocalFireDepartment
        )
        Divider(modifier = Modifier.fillMaxWidth())
        GeneralStatisticsItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.study_time),
            valueText = stringResource(id = studyTimeFormatter, studyTime),
            leadingIcon = Icons.Outlined.Schedule
        )
        Divider(modifier = Modifier.fillMaxWidth())
        GeneralStatisticsItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.study_sessions),
            valueText = "$studySessions",
            leadingIcon = Icons.Outlined.Architecture
        )
        Divider(modifier = Modifier.fillMaxWidth())
        GeneralStatisticsItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.start_of_learning),
            valueText = startOfLearning,
            leadingIcon = Icons.Outlined.CalendarMonth
        )
    }
}

@Composable
fun GeneralStatisticsItem(
    modifier: Modifier = Modifier,
    title: String,
    valueText: String,
    leadingIcon: ImageVector
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = valueText, style = MaterialTheme.typography.labelLarge.copy(
                MaterialTheme.colorScheme.onSurfaceVariant.copy(.5F)
            )
        )
    }
}