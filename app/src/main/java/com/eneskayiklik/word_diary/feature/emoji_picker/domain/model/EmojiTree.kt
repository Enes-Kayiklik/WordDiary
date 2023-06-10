package com.eneskayiklik.word_diary.feature.emoji_picker.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.EmojiObjects
import androidx.compose.material.icons.outlined.EmojiSymbols
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.ui.graphics.vector.ImageVector
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.util.MAX_EMOJI_COUNT_LINE

data class EmojiTree(
    val title: EmojiTitle,
    val emojis: List<EmojiItem>
) {
    val lineCount =
        emojis.size / MAX_EMOJI_COUNT_LINE + if (emojis.size % MAX_EMOJI_COUNT_LINE > 0) 1 else 0
}

data class EmojiItem(
    val emoji: String,
    val name: String
)

enum class EmojiTitle(val key: String, @StringRes val value: Int, val icon: ImageVector) {
    SYMBOLS("Symbols", R.string.emoji_symbols, Icons.Outlined.EmojiSymbols),
    FLAGS("Flags", R.string.emoji_flags, Icons.Outlined.Flag),
    OBJECTS("Objects", R.string.emoji_objects, Icons.Outlined.EmojiObjects),
    ACTIVITY("Activity", R.string.emoji_activity, Icons.Outlined.Celebration),
    TRAVEL("Travel & Places", R.string.emoji_travel, Icons.Outlined.Luggage),
    FOOD("Food & Drink", R.string.emoji_food, Icons.Outlined.Fastfood),
    ANIMALS("Animals & Nature", R.string.emoji_animals, Icons.Outlined.Pets),
    SMILEYS("Smileys & People", R.string.emoji_smileys, Icons.Outlined.Mood)
}