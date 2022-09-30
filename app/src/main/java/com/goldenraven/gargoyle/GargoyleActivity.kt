/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.goldenraven.gargoyle.ui.HomeScreen
import com.goldenraven.gargoyle.ui.onboarding.OnboardScreen
import com.goldenraven.gargoyle.ui.theme.GargoyleTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val KEYCHAIN_INITIATED = booleanPreferencesKey("keychain_initiated")
suspend fun initiateKeychain(dataStore: DataStore<Preferences>) {
    dataStore.edit { settings ->
        settings[KEYCHAIN_INITIATED] = true
    }
}

class GargoyleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            initiateKeychain(dataStore)
        }
        var keychainInitiated: Boolean
        runBlocking {
            val keychainInitiated2: Flow<Boolean> = dataStore.data.map { preferences ->
                preferences[KEYCHAIN_INITIATED] ?: false
            }
            keychainInitiated = keychainInitiated2.first()
        }

        // ask the repository if a keychain already exists
        // if so, load it and launch into main activity, otherwise go to intro
        if (keychainInitiated) {
            // Keychain.loadKeychain()

            setContent {
                GargoyleTheme {
                    HomeScreen()
                }
            }
        } else {
            setContent {
                GargoyleTheme {
                    OnboardScreen()
                }
            }
        }
    }
}
