/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.data

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object SecretsRepository {

    // lateinit variables should be avoided as much as possible
    private lateinit var dataStore: DataStore<RecoveryPhrase>

    fun initSecretsRepository(dataStore: DataStore<RecoveryPhrase>) {
        this.dataStore = dataStore
    }

    fun encryptRecoveryPhrase(recoveryPhrase: String) {
        Log.i("SecretsRepository", "We're saving the recovery phrase")
        runBlocking {
            dataStore.updateData {
                RecoveryPhrase(recoveryPhrase)
            }
        }
    }

    fun decryptRecoveryPhrase(): String {
        Log.i("SecretsRepository", "We're decrypting the recovery phrase")
        val recoveryPhrase: RecoveryPhrase
        runBlocking {
            recoveryPhrase = dataStore.data.first()
        }
        return recoveryPhrase.recoveryPhrase
    }
}
