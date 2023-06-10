package com.eneskayiklik.word_diary.core.data.permission

import com.eneskayiklik.word_diary.R

class NotificationPermissionProvider : PermissionProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): Int =
        if (isPermanentlyDeclined) R.string.notification_permission_declined else R.string.notification_permission_rationale
}