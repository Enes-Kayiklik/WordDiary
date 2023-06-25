package com.eneskayiklik.word_diary.core.data_store.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eneskayiklik.word_diary.core.data_store.data.model.CoinInfo
import com.eneskayiklik.word_diary.core.data_store.domain.CoinRepository
import com.eneskayiklik.word_diary.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): CoinRepository {

    companion object {
        private val LAST_COIN_ADDED = stringPreferencesKey("LAST_COIN_ADDED")
        private val DAILY_FREE_COIN = intPreferencesKey("DAILY_FREE_COIN")
        private val ADS_COIN = intPreferencesKey("ADS_COIN")
    }

    override val coinInfo: Flow<CoinInfo>
        get() = dataStore.data
            .map { preferences ->
                if (preferences[LAST_COIN_ADDED] != DateUtils.getToday()) {
                    dataStore.edit { editor ->
                        editor[LAST_COIN_ADDED] = DateUtils.getToday()
                        editor[DAILY_FREE_COIN] = 5
                    }
                }

                CoinInfo(
                    dailyFreeCoin = preferences[DAILY_FREE_COIN] ?: 0,
                    fromAdsCoin = preferences[ADS_COIN] ?: 0
                )
            }

    override suspend fun spendCoin() {
        dataStore.edit { editor ->
            val hasFreeCoin = (editor[DAILY_FREE_COIN] ?: 0) > 0
            if (hasFreeCoin) {
                editor[DAILY_FREE_COIN] = maxOf((editor[DAILY_FREE_COIN] ?: 0) - 1, 0)
            } else {
                editor[ADS_COIN] = maxOf((editor[ADS_COIN] ?: 0) - 1, 0)
            }
        }
    }

    override suspend fun increaseAdsCoin() {
        dataStore.edit { editor ->
            editor[ADS_COIN] = (editor[ADS_COIN] ?: 0) + 5
        }
    }
}