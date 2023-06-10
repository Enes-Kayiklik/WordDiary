package com.eneskayiklik.word_diary.core.data_store.data

import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPreferenceSerializer @Inject constructor(

) : Serializer<UserPreference> {
    override val defaultValue: UserPreference
        get() = UserPreference()

    override suspend fun readFrom(input: InputStream): UserPreference {
        return try {
            Json.decodeFromString(
                deserializer = UserPreference.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: UserPreference, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = UserPreference.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}