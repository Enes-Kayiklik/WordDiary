package com.eneskayiklik.word_diary.core.data.repository

import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.data.model.InitResponse
import com.eneskayiklik.word_diary.core.domain.repository.InitRepository
import com.eneskayiklik.word_diary.util.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import javax.inject.Inject

class InitRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : InitRepository {

    override suspend fun getInitConfig(): Result<InitResponse> {
        return try {
            // Since we are getting result from Github it returns content type as "plain/text"
            // So we have to decode manually
            val bodyString = client.get(HttpRoutes.INIT_CONFIG).bodyAsText()
            Result.Success(Json.decodeFromString(bodyString))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}