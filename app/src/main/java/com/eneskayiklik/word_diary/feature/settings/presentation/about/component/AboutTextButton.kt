package com.eneskayiklik.word_diary.feature.settings.presentation.about.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AboutTextButton(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    imageVector: ImageVector? = null,
    contentDescription: String? = null,
    title: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick, modifier = modifier) {
        if (icon != null) Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp)
        ) else if (imageVector != null) Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = title,
            modifier = Modifier.padding(start = if (icon != null || imageVector != null) 8.dp else 0.dp)
        )
    }
}