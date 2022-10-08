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
import fr.acinq.bitcoin.crypto.Digest
import fr.acinq.bitcoin.crypto.Pack
import fr.acinq.bitcoin.crypto.hmac
import io.ktor.http.*
import java.security.SecureRandom

object Keychain {
    fun generateKeychain() {
        val secureRandom: SecureRandom = SecureRandom()
        val entropy = ByteArray(16)
        secureRandom.nextBytes(entropy)
        val mnemonic = MnemonicCode.toMnemonics(entropy)
        Log.i("Keychain", "New keychain generated, mnemonic: $mnemonic")
        SettingsRepository.keychainInitiated()
        SecretsRepository.encryptRecoveryPhrase(mnemonic.joinToString(" "))
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

    fun generateLinkingKey(url: Url): ExtendedPrivateKey {
        val seed = toSeed(SecretsRepository.decryptRecoveryPhrase(), "")
        val bip32RootKey: ExtendedPrivateKey = DeterministicWallet.generate(seed)

        val hashingKey = DeterministicWallet.derivePrivateKey(bip32RootKey, "m/138'/0").privateKey.value.toByteArray()
        val fullHash = Digest.sha256().hmac(
            key = hashingKey,
            data = url.host.encodeToByteArray(),
            blockSize = 64
        )

        val pathPart1 = fullHash.sliceArray(IntRange(0, 3)).let { Pack.int32BE(it, 0) }.toUInt()
        val pathPart2 = fullHash.sliceArray(IntRange(4, 7)).let { Pack.int32BE(it, 0) }.toUInt()
        val pathPart3 = fullHash.sliceArray(IntRange(8, 11)).let { Pack.int32BE(it, 0) }.toUInt()
        val pathPart4 = fullHash.sliceArray(IntRange(12, 15)).let { Pack.int32BE(it, 0) }.toUInt()

        val linkingPrivateKey = DeterministicWallet.derivePrivateKey(
            parent = bip32RootKey,
            keyPath = "m/138'/$pathPart1/$pathPart2/$pathPart3/$pathPart4"
        )

        println("The public key for host ${url.host} is ${linkingPrivateKey.publicKey}")
        return linkingPrivateKey
    }
}
