package com.eneskayiklik.word_diary.feature.word_list.domain

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Ballot
import androidx.compose.material.icons.outlined.Spellcheck
import androidx.compose.material.icons.outlined.Style
import androidx.compose.ui.graphics.vector.ImageVector
import com.eneskayiklik.word_diary.R
import kotlinx.serialization.Serializable

@Serializable
enum class StudyType(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    val icon: ImageVector,
    val requirePremium: Boolean,
    val minimumWordCount: Int,
) {
    FlashCard(
        title = R.string.quiz_type_flash_card_title,
        subtitle = R.string.quiz_type_flash_card_desc,
        icon = Icons.Outlined.Style,
        requirePremium = false,
        minimumWordCount = 3
    ),
    Write(
        title = R.string.quiz_type_write_title,
        subtitle = R.string.quiz_type_write_desc,
        icon = Icons.Outlined.Spellcheck,
        requirePremium = false,
        minimumWordCount = 3
    ),
    MultipleChoice(
        title = R.string.quiz_type_multiple_choice_title,
        subtitle = R.string.quiz_type_multiple_choice_desc,
        icon = Icons.Outlined.Ballot,
        requirePremium = false,
        minimumWordCount = 4
    )
}