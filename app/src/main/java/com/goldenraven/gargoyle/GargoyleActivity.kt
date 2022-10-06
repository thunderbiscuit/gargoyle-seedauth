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
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.goldenraven.gargoyle.data.*
import com.goldenraven.gargoyle.ui.HomeScreen
import com.goldenraven.gargoyle.ui.onboarding.OnboardScreen
import com.goldenraven.gargoyle.ui.theme.GargoyleTheme

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
val Context.secretsDataStore: DataStore<RecoveryPhrase> by dataStore(
    fileName = "secrets.bin",
    serializer = RecoveryPhraseSerializer(KeyStoreManager())
)

class GargoyleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SettingsRepository.initSettingsRepository(preferencesDataStore)
        SecretsRepository.initSecretsRepository(secretsDataStore)

        // ask the repository if a keychain already exists
        // if so, load it and launch into main activity, otherwise go to intro
        if (SettingsRepository.isKeychainInitiated()) {
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
