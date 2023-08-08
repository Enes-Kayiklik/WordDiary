package com.eneskayiklik.word_diary.feature.settings.presentation

import android.graphics.Color
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.feature.folder_list.presentation.UserData

data class SettingsState(
    val pages: List<SettingsPage> = listOf(
        SettingsPage.General,
        SettingsPage.Theming,
        SettingsPage.SyncData,
        SettingsPage.Notification,
        SettingsPage.About
    ),
    val userPrefs: UserPreference = UserPreference(),
    val dialogType: SettingsDialog = SettingsDialog.None,
    val themeColors: List<Int> = generateThemeColors(),
    val userData: UserData = UserData(),
    val isDriveBackingUp: Boolean = false
)

enum class SettingsDialog {
    None,
    SelectAppLanguage,
    SelectMotherLanguage,
    SelectTheme,
    SelectPrimaryColor,
    SelectFont,
    SelectPaletteStyle,
    NotificationFrequency,
    RemindingTime,
    RestoreBackup,
    RestartApp
}

enum class SettingsPage(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val title: Int
) {
    General(
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        title = R.string.general
    ), // User language, App language, Daily goal
    Theming(
        selectedIcon = Icons.Filled.Palette,
        unselectedIcon = Icons.Outlined.Palette,
        title = R.string.theme_and_colors
    ), // Theme, Font, Primary color, Wallpaper color, Monochrome, Palette style
    SyncData(
        selectedIcon = Icons.Filled.CloudSync,
        unselectedIcon = Icons.Outlined.CloudSync,
        title = R.string.backup_and_restore
    ), // Cloud sync, Local sync, Auto sync
    Notification(
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        title = R.string.setup_notification
    ), // Frequency, time, Sync reminder
    About(
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        title = R.string.about
    ) // Privacy policy, Terms of condition, Open source licences
}

fun generateThemeColors() = listOf(
    Color.parseColor("#FFD5E0"),
    Color.parseColor("#D585AF"),
    Color.parseColor("#C599D8"),
    Color.parseColor("#D587FA"),
    Color.parseColor("#AA87FF"),
    Color.parseColor("#6587FF"),
    Color.parseColor("#6286EA"),
    Color.parseColor("#00B5FF"),
    Color.parseColor("#0096EA"),
    Color.parseColor("#1DE9BB"),
    Color.parseColor("#00BFA5"),
    Color.parseColor("#00E678"),
    Color.parseColor("#00C85A"),
    Color.parseColor("#C6FF00"),
    Color.parseColor("#AEEA00")
)