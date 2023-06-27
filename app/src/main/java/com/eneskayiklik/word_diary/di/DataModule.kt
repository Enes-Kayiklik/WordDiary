package com.eneskayiklik.word_diary.di

import com.eneskayiklik.word_diary.core.data.repository.InitRepositoryImpl
import com.eneskayiklik.word_diary.core.data.repository.TranslationRepositoryImpl
import com.eneskayiklik.word_diary.core.data_store.data.CoinRepositoryImpl
import com.eneskayiklik.word_diary.core.data_store.data.UpdateConfigRepositoryImpl
import com.eneskayiklik.word_diary.core.data_store.data.UserPreferenceRepositoryImpl
import com.eneskayiklik.word_diary.core.data_store.domain.CoinRepository
import com.eneskayiklik.word_diary.core.data_store.domain.UpdateConfigRepository
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.domain.repository.InitRepository
import com.eneskayiklik.word_diary.core.domain.repository.TranslationRepository
import com.eneskayiklik.word_diary.feature.backup.data.repository.BackupRepositoryImpl
import com.eneskayiklik.word_diary.feature.backup.domain.repository.BackupRepository
import com.eneskayiklik.word_diary.feature.folder_list.data.repository.FolderRepositoryImpl
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.eneskayiklik.word_diary.feature.paywall.data.PaywallRepositoryImpl
import com.eneskayiklik.word_diary.feature.paywall.domain.PaywallRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsFolderRepository(
        folderRepository: FolderRepositoryImpl
    ): FolderRepository

    @Binds
    fun bindsUserPreferenceRepository(
        userPreferenceRepository: UserPreferenceRepositoryImpl
    ): UserPreferenceRepository

    @Binds
    fun bindsUpdateConfigRepository(
        updateConfigRepository: UpdateConfigRepositoryImpl
    ): UpdateConfigRepository

    @Binds
    fun bindsPaywallRepository(
        paywallRepository: PaywallRepositoryImpl
    ): PaywallRepository

    @Binds
    fun bindsInitRepository(
        initRepository: InitRepositoryImpl
    ): InitRepository

    @Binds
    fun bindsTranslationRepository(
        translationRepository: TranslationRepositoryImpl
    ): TranslationRepository

    @Binds
    fun bindCoinRepository(
        coinRepository: CoinRepositoryImpl
    ): CoinRepository

    @Binds
    fun bindBackupRepository(
        backupRepository: BackupRepositoryImpl
    ): BackupRepository
}