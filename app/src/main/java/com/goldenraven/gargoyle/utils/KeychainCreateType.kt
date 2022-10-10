/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.utils

sealed class KeychainCreateType {
    object FROMSCRATCH : KeychainCreateType()
    class RECOVER(val recoveryPhrase: String) : KeychainCreateType()
}
