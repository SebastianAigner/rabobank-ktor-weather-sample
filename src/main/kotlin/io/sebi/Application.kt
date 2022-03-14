package io.sebi

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/api/bbq") {
                // /api/bbq?city=AMS
                val city = call.request.queryParameters["city"] ?: "Amsterdam"
                call.respond(canBarbecueInCity(city))
            }
        }
    }.start(true)
}

