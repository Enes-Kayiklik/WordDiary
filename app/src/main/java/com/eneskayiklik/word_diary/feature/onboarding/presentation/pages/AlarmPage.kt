package com.eneskayiklik.word_diary.feature.onboarding.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.NotificationSettings
import com.eneskayiklik.word_diary.core.util.components.ClockPicker
import com.eneskayiklik.word_diary.feature.onboarding.presentation.component.AlarmView
import java.time.LocalTime

@Composable
fun AlarmPage(
    modifier: Modifier = Modifier,
    notificationSettings: NotificationSettings,
    setAlarm: (LocalTime) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    var isClockPickerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_alarm_clock),
            contentDescription = null,
            modifier = Modifier.size(148.dp)
        )

        Text(
            text = stringResource(id = R.string.onboarding_alarm_title),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.onboarding_alarm_desc),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        AlarmView(
            isActive = notificationSettings.isNotificationEnabled,
            selectedTime = stringResource(
                id = R.string.onboarding_alarm_notify_me_at,
                notificationSettings.notificationTime
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            onCheckedChange = onCheckedChange,
            onClick = { isClockPickerVisible = isClockPickerVisible.not() }
        )
    }

    if (isClockPickerVisible) ClockPicker(
        selectTime = setAlarm,
        closePicker = { isClockPickerVisible = false }
    )
}