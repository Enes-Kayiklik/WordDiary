package com.eneskayiklik.word_diary.feature.settings.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R

@Composable
fun DriveConfigView(
    modifier: Modifier = Modifier,
    isSignedIn: Boolean,
    isDriveBackingUp: () -> Boolean,
    onAction: () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_drive),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )

            Column(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.backup_to_drive),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(id = R.string.backup_to_drive_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (isSignedIn.not()) stringResource(id = R.string.sign_in) else stringResource(
                    id = R.string.create_backup
                )
            )
            AnimatedVisibility(visible = isDriveBackingUp()) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(start = 4.dp).size(12.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}