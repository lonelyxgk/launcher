package top.esajk.launcher

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

object AppState {
    val httpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }
    }
}