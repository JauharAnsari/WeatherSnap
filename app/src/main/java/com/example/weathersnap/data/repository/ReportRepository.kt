package com.example.weathersnap.data.repository

import com.example.weathersnap.data.db.WeatherReport
import com.example.weathersnap.data.db.WeatherReportDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
class ReportRepository @Inject constructor(
    private val dao: WeatherReportDao
) {
    fun getAllReports(): Flow<List<WeatherReport>> = dao.getAllReports()

    suspend fun saveReport(report: WeatherReport) {
        withContext(Dispatchers.IO) {
            dao.insertReport(report)
        }
    }
}
