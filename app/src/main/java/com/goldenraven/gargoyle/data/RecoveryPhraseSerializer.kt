/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.data

import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class RecoveryPhrase(val recoveryPhrase: String)

class RecoveryPhraseSerializer(
    private val keyStoreManager: KeyStoreManager
): Serializer<RecoveryPhrase> {

    override val defaultValue: RecoveryPhrase
        get() = RecoveryPhrase("No recovery phrase")

    override suspend fun readFrom(input: InputStream): RecoveryPhrase {
        val decryptedBytes = keyStoreManager.decrypt(input)
        return Json.decodeFromString(
            deserializer = RecoveryPhrase.serializer(),
            string = decryptedBytes.decodeToString()
        )
    }

    override suspend fun writeTo(t: RecoveryPhrase, output: OutputStream) {
        keyStoreManager.encrypt(
            bytes = Json.encodeToString(
                serializer = RecoveryPhrase.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}
