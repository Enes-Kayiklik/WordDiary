package com.eneskayiklik.word_diary.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.data_store.data.UserPreferenceSerializer
import com.eneskayiklik.word_diary.util.USER_PREFERENCE_FILE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.userDataStore by preferencesDataStore(
        name = "user_preferences"
    )

    // Preference Data Store
    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.userDataStore
    }

    // Proto Data Store
    @Provides
    @Singleton
    fun provideUserPreferenceDataStore(
        @ApplicationContext context: Context,
        serializer: UserPreferenceSerializer
    ): DataStore<UserPreference> = DataStoreFactory.create(
        serializer = serializer
    ) {
        context.dataStoreFile(USER_PREFERENCE_FILE)
    }
}