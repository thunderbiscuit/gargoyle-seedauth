/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.network

import android.util.Log
import com.goldenraven.gargoyle.data.Keychain.generateLinkingKey
import fr.acinq.bitcoin.ByteVector32
import fr.acinq.bitcoin.Crypto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class KtorClient {

    suspend fun login(url: Url) {
        val client = HttpClient(CIO)
        Log.i("KtorClient", "We've built the Ktor client")

        val linkingKey = generateLinkingKey(url)
        val k1: String = url.parameters["k1"] ?: throw Error("No k1 in URL!")
        val signedChallenge = Crypto.compact2der(
            Crypto.sign(
                data = ByteVector32.fromValidHex(k1),
                privateKey = linkingKey.privateKey
            )
        ).toHex()
        Log.i("KtorClient", "Signature is $signedChallenge")

        val builder: URLBuilder = URLBuilder(url)
        builder.parameters.append(name = "sig", value = signedChallenge)
        builder.parameters.append(name = "key", value = linkingKey.publicKey.toString())
        val returnUrl = builder.build()
        println("The URL we use to login is $returnUrl")

        val response: HttpResponse = client.get("$returnUrl")
        Log.i("KtorClient", "Ktor client attempted login. Server response was ${response.status}")
        val responseBody: String = response.body()
        Log.i("KtorClient", "Response is $response")
        Log.i("KtorClient", "Response body: $responseBody")
        client.close()
    }
}
