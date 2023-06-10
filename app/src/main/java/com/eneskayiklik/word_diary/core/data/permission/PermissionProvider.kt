package com.eneskayiklik.word_diary.core.data.permission

interface PermissionProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): Int
}

enum class NotificationState {
    Rationale,
    Settings,
    None
}