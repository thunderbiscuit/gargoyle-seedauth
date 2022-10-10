/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.utils

import fr.acinq.bitcoin.Bech32
import io.ktor.http.*

fun parseBech32EncodedUrl(input: String): Url {
    val (hrp, data) = Bech32.decode(input)
    val payload = Bech32.five2eight(data, 0).decodeToString()
    val url = URLBuilder(payload).build()
    if (!url.protocol.isSecure()) throw Error("Protocol is not https!")
    return url
}
