package com.eneskayiklik.word_diary.feature.folder_list.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount

@Composable
fun SingleFolderItem(
    folderWithCount: FolderWithWordCount,
    modifier: Modifier = Modifier
) {
    val folder = folderWithCount.folder

    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier, contentAlignment = Alignment.Center
            ) {
                val painter = painterResource(R.drawable.ic_folder)
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = Color(folder.color),
                    modifier = Modifier
                        .fillMaxWidth(.5F)
                        .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                )
                if (folder.emoji != null) Text(text = folder.emoji, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = folder.title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${folderWithCount.wordCount} words",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column(
            modifier = Modifier.padding(8.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }

            CircularProgressIndicator(
                modifier = Modifier
                    .size(16.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                trackColor = MaterialTheme.colorScheme.primary.copy(.4F),
                progress = .5F
            )
        }
    }
}