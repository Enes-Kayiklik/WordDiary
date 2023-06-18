package com.eneskayiklik.word_diary.feature.paywall.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.paywall.domain.model.WordDiaryProduct

@Composable
fun ProductView(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    product: WordDiaryProduct
) {
    val colorAnim by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceColorAtElevation(
            12.dp
        )
    )
    val textColorAnim by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
    Column(modifier = modifier.border(1.dp, colorAnim, MaterialTheme.shapes.medium)) {
        Column(
            modifier = Modifier
                .weight(2F)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = product.period, style = MaterialTheme.typography.titleMedium)
            Text(
                text = product.readablePrice,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .3F)
                )
            )
        }
        Column(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()
                .background(colorAnim, MaterialTheme.shapes.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product.readablePerWeek,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = textColorAnim
                )
            )
            Text(
                text = stringResource(id = R.string.week),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = textColorAnim
                )
            )
        }
    }
}