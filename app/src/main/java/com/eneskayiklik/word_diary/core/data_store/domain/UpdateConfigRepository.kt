package com.eneskayiklik.word_diary.core.data_store.domain

import com.eneskayiklik.word_diary.core.data.model.InitResponse
import com.eneskayiklik.word_diary.core.data_store.data.model.UpdateConfig
import kotlinx.coroutines.flow.Flow

interface UpdateConfigRepository {

    suspend fun updateConfigData(data: InitResponse)

    suspend fun getConfigData(): Flow<UpdateConfig>
}