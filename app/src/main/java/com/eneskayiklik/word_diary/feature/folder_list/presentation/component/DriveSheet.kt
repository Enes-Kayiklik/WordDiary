package com.eneskayiklik.word_diary.feature.folder_list.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.eneskayiklik.word_diary.feature.folder_list.presentation.UserData

@Composable
fun DriveSheet(
    modifier: Modifier = Modifier,
    userData: UserData
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SubcomposeAsyncImage(
                model = userData.photoUrl,
                error = {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = userData.displayName ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = userData.email ?: "",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Logout, contentDescription = null)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)) {
                Icon(imageVector = Icons.Outlined.Sync, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sync")
            }
            Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)) {
                Icon(imageVector = Icons.Outlined.Backup, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Backup")
            }
        }
    }
}