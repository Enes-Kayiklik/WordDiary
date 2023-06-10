package com.eneskayiklik.word_diary.feature.create_folder.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.ui.theme.Blue
import com.eneskayiklik.word_diary.core.ui.theme.GreenYellow
import com.eneskayiklik.word_diary.core.ui.theme.Orange
import com.eneskayiklik.word_diary.core.ui.theme.OrangeVariant
import com.eneskayiklik.word_diary.core.ui.theme.Pink
import com.eneskayiklik.word_diary.core.ui.theme.Purple
import com.eneskayiklik.word_diary.core.ui.theme.PurpleVariant
import com.eneskayiklik.word_diary.core.ui.theme.Red

const val MAX_FOLDER_NAME_LEN = 20

data class CreateFolderState(
    val folderName: TextFieldValue = TextFieldValue(),
    val hasNameError: Boolean = false,
    val languages: List<UserLanguage> = emptyList(),
    val selectedLanguage: UserLanguage = UserLanguage.NOT_SPECIFIED,
    val colors: List<Color> = getDefaultColors(),
    val selectedEmoji: String? = null,
    val selectedColorIndex: Int = 0,
) {
    private val currentLen = folderName.text.length

    val counterText = "$currentLen/$MAX_FOLDER_NAME_LEN"
}

fun getDefaultColors() = listOf(
    Pink,
    Purple,
    PurpleVariant,
    Orange,
    OrangeVariant,
    Red,
    Blue,
    GreenYellow
)