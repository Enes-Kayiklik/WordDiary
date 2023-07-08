package com.eneskayiklik.word_diary.feature.word_list.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.word_list.presentation.ListStatistic

@Composable
fun ListsStatisticView(
    statistic: ListStatistic,
    modifier: Modifier = Modifier,
    onStudyClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = statistic.progress,
                modifier = Modifier.size(124.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(.3F),
                strokeWidth = 6.dp,
                strokeCap = StrokeCap.Round
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "${statistic.percentage}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        text = "%",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Text(text = stringResource(id = R.string.mastered), style = MaterialTheme.typography.bodySmall)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ListStatisticCard(
                icon = Icons.Default.ViewAgenda,
                title = stringResource(id = R.string.total_words),
                value = statistic.totalWords
            )
            Divider()
            ListStatisticCard(
                icon = Icons.Default.School,
                title = stringResource(id = R.string.mastered_words),
                value = statistic.masteredWords
            )
            Button(onClick = onStudyClick, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    Icons.Default.HistoryEdu,
                    contentDescription = null
                )
                Text(
                    text = stringResource(id = R.string.practise),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ListStatisticCard(
    icon: ImageVector,
    title: String,
    value: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = title, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = "$value", style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}