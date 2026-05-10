package com.example.weathersnap.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_reports")
data class WeatherReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val imagePath: String,
    val originalSizeKb: Long,
    val compressedSizeKb: Long,
    val notes: String,
    val timestamp: Long
)
