package com.eneskayiklik.word_diary.core.data.repository

import com.eneskayiklik.word_diary.core.domain.repository.TranslationRepository
import com.eneskayiklik.word_diary.util.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.feature.create_word.domain.model.SampleSentence
import com.eneskayiklik.word_diary.feature.create_word.domain.model.SynonymState
import com.eneskayiklik.word_diary.feature.create_word.domain.model.SynonymsAndSamples
import org.jsoup.Jsoup

class TranslationRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : TranslationRepository {

    override fun getTranslation(
        text: String,
        sourceLang: String,
        targetLang: String
    ): Flow<Result<String>> = flow {
        try {
            emit(Result.Loading)
            val translatedText: List<String> = client.get(HttpRoutes.GOOGLE_TRANSLATE) {
                parameter("q", text)
                parameter("sl", sourceLang)
                parameter("tl", targetLang)
            }.body()
            val result = translatedText.firstOrNull()?.lowercase()
                ?: throw NullPointerException("Translated text can not be null")
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }

    override fun getSynonymsAndSamples(
        word: String,
        sourceLang: String,
        targetLang: String
    ): Flow<Result<SynonymsAndSamples>> = flow {
        try {
            val synonyms = mutableListOf<SynonymState>()
            val samples = mutableListOf<SampleSentence>()
            val departmentUrl = "https://glosbe.com/$sourceLang/$targetLang/${word.lowercase()}"
            val doc = Jsoup.connect(departmentUrl).get()

            doc.select("section.px-1 > div.pl-1 > div > ul.pr-1 > li").forEach { element ->
                val synonym = element.select("h3.align-top").text()
                val sampleSentence = element.select("div.translation__example > p")
                val learnedLang = sampleSentence.getOrNull(1)?.text()
                val nativeLang = sampleSentence.getOrNull(0)?.text()
                if (synonym.isNotEmpty()) synonyms.add(
                    SynonymState(
                        synonym = synonym,
                        sampleNativeLang = nativeLang ?: "",
                        sampleLearnedLang = learnedLang ?: ""
                    )
                )
            }
            doc.getElementsByClass("odd:bg-slate-100 px-1").forEach { element ->
                val sampleSentence = element.getElementsByClass("w-1/2")
                val learnedLang = sampleSentence.getOrNull(1)?.text()
                val nativeLang = sampleSentence.getOrNull(0)?.text()
                samples.add(
                    SampleSentence(
                        sampleNativeLang = nativeLang ?: "",
                        sampleLearnedLang = learnedLang ?: ""

                    )
                )
            }

            emit(
                Result.Success(
                    SynonymsAndSamples(
                        synonyms = synonyms.distinctBy { s -> s.synonym.lowercase() }.take(3),
                        samples = samples.take(5)
                    )
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }
}