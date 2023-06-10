package com.eneskayiklik.word_diary.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eneskayiklik.word_diary.R

@Composable
fun AlarmView(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    selectedTime: String
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1F)) {
            Text(text = stringResource(id = R.string.onboarding_alarm_notify_me), style = MaterialTheme.typography.bodyLarge)
            Text(text = selectedTime, style = MaterialTheme.typography.bodyMedium)
        }
        Switch(
            checked = isActive,
            onCheckedChange = onCheckedChange
        )
    }
}