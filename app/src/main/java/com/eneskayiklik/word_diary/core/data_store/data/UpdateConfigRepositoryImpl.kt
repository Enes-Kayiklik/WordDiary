package com.eneskayiklik.word_diary.core.data_store.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eneskayiklik.word_diary.core.data.model.InitResponse
import com.eneskayiklik.word_diary.core.data_store.data.model.UpdateConfig
import com.eneskayiklik.word_diary.core.data_store.domain.UpdateConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateConfigRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UpdateConfigRepository {

    companion object {
        private val FORCE_UPDATE_VERSION = intPreferencesKey("FORCE_UPDATE_VERSION")
        private val FEATURES = stringPreferencesKey("FEATURES")
        private val LATEST_VERSION_NAME = stringPreferencesKey("LATEST_VERSION_NAME")
        private val OPTIONAL_UPDATE_VERSION = intPreferencesKey("OPTIONAL_UPDATE_VERSION")
        private val LAST_UPDATED = longPreferencesKey("LAST_UPDATED")
    }

    override suspend fun updateConfigData(data: InitResponse) {
        dataStore.edit { preferences ->
            preferences[FORCE_UPDATE_VERSION] = data.forceUpdateVersion
            preferences[FEATURES] = data.features
            preferences[OPTIONAL_UPDATE_VERSION] = data.optionalUpdateVersion
            preferences[LATEST_VERSION_NAME] = data.latestVersionName
            preferences[LAST_UPDATED] = System.currentTimeMillis()
        }
    }

    override suspend fun getConfigData(): Flow<UpdateConfig> =
        dataStore.data
            .map { preferences ->
                UpdateConfig(
                    forceUpdateVersion = preferences[FORCE_UPDATE_VERSION] ?: 0,
                    features = preferences[FEATURES] ?: "",
                    optionalUpdateVersion = preferences[OPTIONAL_UPDATE_VERSION] ?: 0,
                    latestVersionName = preferences[LATEST_VERSION_NAME] ?: "",
                    lastUpdated = preferences[LAST_UPDATED] ?: 0L
                )
            }
}