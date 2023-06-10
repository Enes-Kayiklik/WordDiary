package com.eneskayiklik.word_diary.di

import com.eneskayiklik.word_diary.core.data_store.data.UserPreferenceRepositoryImpl
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.feature.folder_list.data.repository.FolderRepositoryImpl
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
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
}