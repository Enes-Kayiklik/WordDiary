package com.eneskayiklik.word_diary.core.data

sealed interface Result<out T> {
    data class Error(val exception: Exception) : Result<Nothing>

    object Loading : Result<Nothing>

    data class Success<T>(val result: T) : Result<T>
}