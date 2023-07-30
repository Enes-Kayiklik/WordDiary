package com.eneskayiklik.word_diary.feature.calendar.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.core.database.model.StudySessionWithFolder
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SingleStudySessionView(
    studySession: StudySessionWithFolder,
    modifier: Modifier = Modifier,
) {
    val folder = studySession.folder
    val session = studySession.session

    val sessionTimeAtDay = remember {
        SimpleDateFormat("HH mm", Locale.getDefault()).format(session.date).split(" ")
    }

    ListItem(
        headlineContent = {
            Text(text = (folder.emoji ?: "") + " " + folder.title)
        }, overlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = session.sessionType.icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(text = stringResource(id = session.sessionType.title))
            }
        }, leadingContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = sessionTimeAtDay.first(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = sessionTimeAtDay.last(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }, modifier = modifier
    )
}