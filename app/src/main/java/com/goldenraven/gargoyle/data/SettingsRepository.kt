/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object SettingsRepository {
    // lateinit variables should be avoided as much as possible
    private lateinit var dataStore: DataStore<Preferences>

    fun initSettingsRepository(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
    }

    private val KEYCHAIN_INITIATED = booleanPreferencesKey("keychain_initiated")

    fun keychainInitiated() {
        runBlocking {
            dataStore.edit { settings ->
                settings[KEYCHAIN_INITIATED] = true
            }
        }
    }

    fun isKeychainInitiated(): Boolean {
        var keychainIsInitiated: Boolean
        runBlocking {
            val keychainIsInitiatedFlow: Flow<Boolean> = dataStore.data.map { preferences ->
                preferences[KEYCHAIN_INITIATED] ?: false
            }
            keychainIsInitiated = keychainIsInitiatedFlow.first()
        }
        return keychainIsInitiated
    }
}
