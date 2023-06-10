package com.eneskayiklik.word_diary.core.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordToSpeech @Inject constructor(
    @ApplicationContext private val context: Context
): TextToSpeech.OnInitListener {
    private val tts: TextToSpeech = TextToSpeech(context, this)

    fun speak(str: String, source: String) {
        tts.apply {
            language = Locale.forLanguageTag(source)
            speak(str, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun speakAddQueue(str: String, source: String) {
        tts.apply {
            language = Locale.forLanguageTag(source)
            speak(str, TextToSpeech.QUEUE_ADD, null, null)
        }
    }

    fun clearQueue() = tts.stop()

    fun shutdown() = tts.shutdown()

    override fun onInit(status: Int) = Unit
}