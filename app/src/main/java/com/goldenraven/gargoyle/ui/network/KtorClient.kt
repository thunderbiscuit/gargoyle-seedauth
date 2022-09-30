package com.goldenraven.gargoyle.ui.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class KtorClient {

    suspend fun login() {
        val client = HttpClient(CIO)
        Log.i("KtorClient", "We've built the Ktor client")
        val response: HttpResponse = client.get("https://ktor.io/")
        Log.i("KtorClient", "Ktor client attempted login. Server response was ${response.status}")
        client.close()
    }
}
