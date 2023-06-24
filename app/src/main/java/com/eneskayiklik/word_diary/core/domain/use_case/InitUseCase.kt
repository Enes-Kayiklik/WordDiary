package com.eneskayiklik.word_diary.core.domain.use_case

import android.content.Context
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.data.model.InitResponse
import com.eneskayiklik.word_diary.core.data_store.domain.UpdateConfigRepository
import com.eneskayiklik.word_diary.core.domain.repository.InitRepository
import com.eneskayiklik.word_diary.util.extensions.hasInternetConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InitUseCase @Inject constructor(
    private val repository: InitRepository,
    private val configRepository: UpdateConfigRepository
) {

    suspend operator fun invoke(context: Context) = flow {
        emit(Result.Loading)
        try {
            val localConfig = configRepository.getConfigData().first()
            if (context.hasInternetConnection() && localConfig.lastUpdated <= System.currentTimeMillis() - (1000 * 60 * 60 * 2)) {
                val initConfig = repository.getInitConfig()
                emit(initConfig)

                // Update local value
                if (initConfig is Result.Success) configRepository.updateConfigData(initConfig.result)
            } else {
                // Wait for the layout initialization :(
                // TODO("Better approach needed!!")
                delay(200L)
                emit(
                    Result.Success(
                        InitResponse(
                            forceUpdateVersion = localConfig.forceUpdateVersion,
                            optionalUpdateVersion = localConfig.optionalUpdateVersion,
                            features = localConfig.features,
                            latestVersionName = localConfig.latestVersionName
                        )
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }
}