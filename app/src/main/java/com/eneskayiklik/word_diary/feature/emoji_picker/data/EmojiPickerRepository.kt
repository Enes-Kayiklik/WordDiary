package com.eneskayiklik.word_diary.feature.emoji_picker.data

import android.content.Context
import android.graphics.Paint
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.emoji_picker.domain.model.EmojiItem
import com.eneskayiklik.word_diary.feature.emoji_picker.domain.model.EmojiTitle
import com.eneskayiklik.word_diary.feature.emoji_picker.domain.model.EmojiTree
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmojiPickerRepository @Inject constructor() {

    private var emojis: List<EmojiTree>? = null

    private val orderById = listOf(
        EmojiTitle.SMILEYS,
        EmojiTitle.ANIMALS,
        EmojiTitle.FOOD,
        EmojiTitle.ACTIVITY,
        EmojiTitle.TRAVEL,
        EmojiTitle.OBJECTS,
        EmojiTitle.SYMBOLS,
        EmojiTitle.FLAGS
    )

    fun getEmojis(context: Context): List<EmojiTree> {
        return emojis.takeIf { it != null } ?: run {
            val json = context.resources.openRawResource(R.raw.emojis).bufferedReader()
                .use { it.readText() }
            val type: Map<String, List<LinkedTreeMap<String, String>>> = HashMap()
            val result = (Gson().fromJson(json, type::class.java) ?: mapOf()).toEmojiCollection()
            return result.also { emojis = result }
        }
    }

    private fun Map<String, List<LinkedTreeMap<String, String>>>.toEmojiCollection(): List<EmojiTree> =
        try {
            val result: MutableList<EmojiTree> = mutableListOf()
            entries.sortedByDescending { entry -> orderById.find { it.key == entry.key } }
                .forEach { (key, value) ->
                    val emojiTree = value.toEmojiTree(key)
                    if (emojiTree != null) result.add(emojiTree)
                }
            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }

    private fun List<LinkedTreeMap<String, String>>.toEmojiTree(oldTitle: String): EmojiTree? = try {
        val title = when (oldTitle) {
            EmojiTitle.SYMBOLS.key -> EmojiTitle.SYMBOLS
            EmojiTitle.FLAGS.key -> EmojiTitle.FLAGS
            EmojiTitle.OBJECTS.key -> EmojiTitle.OBJECTS
            EmojiTitle.ACTIVITY.key -> EmojiTitle.ACTIVITY
            EmojiTitle.TRAVEL.key -> EmojiTitle.TRAVEL
            EmojiTitle.FOOD.key -> EmojiTitle.FOOD
            EmojiTitle.ANIMALS.key -> EmojiTitle.ANIMALS
            EmojiTitle.SMILEYS.key -> EmojiTitle.SMILEYS
            else -> null
        }
        if (title != null) EmojiTree(
            title = title,
            emojis = mapNotNull { it.toEmojiItem() }
        ) else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun LinkedTreeMap<String, String>.toEmojiItem() = try {
        val emoji = get("emoji")
        val isRendered = Paint().hasGlyph(emoji)
        if (isRendered) EmojiItem(
            emoji = emoji ?: throw NullPointerException(),
            name = get("name") ?: throw NullPointerException()
        ) else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}