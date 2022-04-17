package org.laolittle.plugin

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

internal val client by lazy {
    HttpClient(OkHttp) {
        engine {
            config {
                readTimeout(30, TimeUnit.SECONDS)
            }
        }
    }
}

internal val Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    allowStructuredMapKeys = true
}