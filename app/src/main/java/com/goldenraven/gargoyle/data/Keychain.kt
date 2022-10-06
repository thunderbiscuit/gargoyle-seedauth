/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.data

import android.util.Log
import fr.acinq.bitcoin.DeterministicWallet
import fr.acinq.bitcoin.DeterministicWallet.ExtendedPrivateKey
import fr.acinq.bitcoin.MnemonicCode
import fr.acinq.bitcoin.MnemonicCode.toSeed
import java.security.SecureRandom

object Keychain {
    fun generateKeychain() {
        val secureRandom: SecureRandom = SecureRandom()
        val entropy = ByteArray(16)
        secureRandom.nextBytes(entropy)
        val mnemonic = MnemonicCode.toMnemonics(entropy)
        Log.i("Keychain", "New keychain generated, mnemonic: $mnemonic")
        SettingsRepository.keychainInitiated()
        SecretsRepository.encryptRecoveryPhrase(mnemonic.toString())
    }

    fun recoverKeychain(mnemonic: String): ExtendedPrivateKey {
        val seed = toSeed(mnemonic, "")
        return DeterministicWallet.generate(seed)
    }

    // the passphrase is required to use the test vectors in https://github.com/rust-bitcoin/rust-bip39/blob/master/src/lib.rs
    fun recoverKeychainWithPassphrase(mnemonic: String, passphrase: String): ExtendedPrivateKey {
        val seed = toSeed(mnemonic, passphrase)
        return DeterministicWallet.generate(seed)
    }
}
