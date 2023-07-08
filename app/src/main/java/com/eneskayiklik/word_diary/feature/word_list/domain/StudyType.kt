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
        title = R.string.flash_card,
        subtitle = R.string.flash_card_desc,
        icon = Icons.Outlined.Style,
        requirePremium = false,
        minimumWordCount = 3
    ),
    Write(
        title = R.string.writing_practise,
        subtitle = R.string.writing_practise_desc,
        icon = Icons.Outlined.Spellcheck,
        requirePremium = false,
        minimumWordCount = 3
    ),
    MultipleChoice(
        title = R.string.multiple_choice,
        subtitle = R.string.multiple_choice_desc,
        icon = Icons.Outlined.Ballot,
        requirePremium = false,
        minimumWordCount = 4
    )
}