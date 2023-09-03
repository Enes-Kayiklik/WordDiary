package com.eneskayiklik.word_diary.feature.paywall.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.components.WaveShape

@Composable
fun UpgradePremiumButton(
    selectedPriceText: String,
    titleText: String,
    @StringRes buttonText: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(text = titleText)
        },
        supportingContent = {
            Text(text = "$selectedPriceText / ${stringResource(id = R.string.week)}")
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(WaveShape())
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = WaveShape()
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_foreground),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.5F),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }, trailingContent = {
            Button(onClick = onClick) {
                Text(text = stringResource(id = buttonText))
            }
        },
        modifier = modifier
    )
}