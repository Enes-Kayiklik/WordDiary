package com.eneskayiklik.word_diary.core.domain.repository

import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.data.model.InitResponse

interface InitRepository {

    suspend fun getInitConfig(): Result<InitResponse>
}