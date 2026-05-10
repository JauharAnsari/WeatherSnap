package com.example.weathersnap.data.repository

import com.example.weathersnap.data.api.GeocodingApiService
import com.example.weathersnap.data.api.WeatherApiService
import com.example.weathersnap.data.model.GeocodingResult
import com.example.weathersnap.data.model.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class WeatherRepository @Inject constructor(
    private val geocodingApiService: GeocodingApiService,
    private val weatherApiService: WeatherApiService
) {
    suspend fun searchCity(query: String): List<GeocodingResult> {
        return withContext(Dispatchers.IO) {
            val response = geocodingApiService.searchCity(name = query)
            response.results ?: emptyList()
        }
    }

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return withContext(Dispatchers.IO) {
            weatherApiService.getWeather(latitude = lat, longitude = lon)
        }
    }
}
