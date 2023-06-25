package com.eneskayiklik.word_diary.core.domain.repository

import kotlinx.coroutines.flow.Flow
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.feature.create_word.domain.model.SynonymsAndSamples

interface TranslationRepository {

    fun getTranslation(
        text: String,
        sourceLang: String,
        targetLang: String
    ): Flow<Result<String>>

    fun getSynonymsAndSamples(
        word: String,
        sourceLang: String,
        targetLang: String
    ): Flow<Result<SynonymsAndSamples>>
}