package com.goldenraven.gargoyle

import com.goldenraven.gargoyle.data.Keychain
import fr.acinq.bitcoin.DeterministicWallet
import org.junit.Test
import kotlin.test.assertEquals

class KeychainTest {
    // test vector from https://github.com/trezor/python-mnemonic/blob/master/vectors.json
    // {
    //     "entropy": "80808080808080808080808080808080",
    //     "network": "mainnet",
    //     "mnemonic": "letter advice cage absurd amount doctor acoustic avoid letter advice cage above",
    //     "passphrase": "TREZOR",
    //     "seed": "d71de856f81a8acc65e6fc851a38d4d7ec216fd0796d0a6827a3ad6ed5511a30fa280f12eb2e47ed2ac03b5c462a0358d18d69fe4f985ec81778c1b370b652a8",
    //     "bip32_xprv: "xprv9s21ZrQH143K2shfP28KM3nr5Ap1SXjz8gc2rAqqMEynmjt6o1qboCDpxckqXavCwdnYds6yBHZGKHv7ef2eTXy461PXUjBFQg6PrwY4Gzq"
    // }

    @Test
    fun `Recover a keychain test vector`() {
        val mnemonic = "letter advice cage absurd amount doctor acoustic avoid letter advice cage above"
        val keychain = Keychain.recoverKeychainWithPassphrase(mnemonic, "TREZOR")
        val rootBIP32ExtendedPrivKey: String = DeterministicWallet.encode(input = keychain, testnet = false)

        assertEquals(
            expected = "xprv9s21ZrQH143K2shfP28KM3nr5Ap1SXjz8gc2rAqqMEynmjt6o1qboCDpxckqXavCwdnYds6yBHZGKHv7ef2eTXy461PXUjBFQg6PrwY4Gzq",
            actual = rootBIP32ExtendedPrivKey
        )
    }

    // @Test
    // fun `Recover a keychain`() {
    //     val mnemonic = "letter advice cage absurd amount doctor acoustic avoid letter advice cage above"
    //     val keychain = Keychain.recoverKeychain(mnemonic, "TREZOR")
    //     val rootBIP32ExtendedPrivKey: String = DeterministicWallet.encode(input = keychain, testnet = false)
    //
    //     assertEquals(
    //         expected = "xprv9s21ZrQH143K2shfP28KM3nr5Ap1SXjz8gc2rAqqMEynmjt6o1qboCDpxckqXavCwdnYds6yBHZGKHv7ef2eTXy461PXUjBFQg6PrwY4Gzq",
    //         actual = rootBIP32ExtendedPrivKey
    //     )
    // }
}
