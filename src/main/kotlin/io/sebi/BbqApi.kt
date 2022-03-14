package io.sebi

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.sebi.weatherapi.WeatherApiResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.io.File

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        })
    }
}

val appId = File("appid.txt").readText().trim()

@Serializable
data class BbqResponse(val city: String, val shouldBbq: Boolean)

suspend fun canBarbecueInCity(city: String): BbqResponse {
    val weatherApiResponse = client.get<WeatherApiResponse>("https://api.openweathermap.org/data/2.5/weather") {
        parameter("q", city)
        parameter("units", "metric")
        parameter("appid", appId)
    }

    return BbqResponse(city, weatherApiResponse.main.feelsLike > 5.0)
}