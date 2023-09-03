package com.eneskayiklik.word_diary.feature.create_folder.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SelectedIndicator(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    icon: ImageVector = Icons.Outlined.Done
) {
    Box(modifier = modifier.background(backgroundColor), contentAlignment = Alignment.Center) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.fillMaxSize(.7F))
    }
}