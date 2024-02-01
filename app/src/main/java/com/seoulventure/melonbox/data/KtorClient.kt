package com.seoulventure.melonbox.data

import com.seoulventure.melonbox.logD
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    //http 클라이언트
    val httpClient: HttpClient = HttpClient(CIO) {
        expectSuccess = true
        // json 설정
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }

        // 로깅 설정
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    logD("api log: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10 * 60 * 1000
            connectTimeoutMillis = 10 * 60 * 1000
            socketTimeoutMillis = 10 * 60 * 1000
        }

        // 기본적인 api 호출시 넣는 것들 즉, 기본 세팅
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}