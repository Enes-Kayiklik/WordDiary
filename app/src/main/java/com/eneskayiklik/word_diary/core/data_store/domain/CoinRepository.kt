package com.eneskayiklik.word_diary.core.data_store.domain

import com.eneskayiklik.word_diary.core.data_store.data.model.CoinInfo
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    val coinInfo: Flow<CoinInfo>

    suspend fun spendCoin()

    suspend fun increaseAdsCoin()
}