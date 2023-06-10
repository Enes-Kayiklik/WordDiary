package com.eneskayiklik.word_diary.feature.quiz.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.animation.animateDoubleAsState
import com.eneskayiklik.word_diary.feature.quiz.StudyResult

@Composable
fun StudyResultView(
    modifier: Modifier = Modifier,
    resultState: StudyResult
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 64.dp, start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = resultState.headlineImage),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
            }
            Text(
                text = stringResource(id = resultState.kudosTextRes),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ), modifier = Modifier.padding(vertical = 16.dp)
            )
            Divider(modifier = Modifier.fillMaxWidth())
        }
        item {
            Text(
                text = stringResource(id = R.string.study_result_your_performance),
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(54.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StudyResultPercentageViewer(
                    icon = R.drawable.ic_graduation_cap,
                    title = stringResource(id = R.string.study_result_proficiency),
                    percentage = resultState.proficiency,
                    progress = resultState.proficiencyProgress,
                    modifier = Modifier.weight(1F)
                )
                StudyResultPercentageViewer(
                    icon = R.drawable.ic_accuracy,
                    title = stringResource(id = R.string.study_result_accuracy),
                    percentage = resultState.accuracy,
                    progress = resultState.accuracyProgress,
                    modifier = Modifier.weight(1F)
                )
                Box(modifier = Modifier.weight(1F))
            }
            Divider(modifier = Modifier.fillMaxWidth())
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.study_result_study_duration),
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = resultState.timeSpent,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        item {
            Divider(modifier = Modifier.fillMaxWidth())
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.study_result_total_words_studied),
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "${resultState.totalQuestionCount}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun StudyResultPercentageViewer(
    @DrawableRes icon: Int,
    title: String,
    progress: Float,
    percentage: Double,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isVisible) progress else 0F,
        animationSpec = tween(500)
    )
    val animatedPercentage by animateDoubleAsState(
        targetValue = if (isVisible) percentage else .0,
        animationSpec = tween(500)
    )

    val formattedPercentage by remember {
        derivedStateOf {
            if (percentage % 1.0 != .0) "%.1f".format(animatedPercentage).plus("%")
            else "%.0f".format(animatedPercentage).plus("%")
        }
    }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 8.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondaryContainer,
                strokeCap = StrokeCap.Round
            )
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(.5F)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formattedPercentage, style = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = title, style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}