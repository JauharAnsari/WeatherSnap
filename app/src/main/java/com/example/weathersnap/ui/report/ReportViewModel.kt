package com.example.weathersnap.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.data.db.WeatherReport
import com.example.weathersnap.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _saveState = MutableStateFlow<Boolean>(false)
    val saveState: StateFlow<Boolean> = _saveState

    fun saveReport(
        city: String,
        temp: Double,
        condition: String,
        humidity: Int,
        wind: Double,
        pressure: Double,
        imagePath: String,
        originalSizeKb: Long,
        compressedSizeKb: Long,
        notes: String
    ) {
        viewModelScope.launch {
            val report = WeatherReport(
                cityName = city,
                temperature = temp,
                condition = condition,
                humidity = humidity,
                windSpeed = wind,
                pressure = pressure.toInt(),
                imagePath = imagePath,
                originalSizeKb = originalSizeKb,
                compressedSizeKb = compressedSizeKb,
                notes = notes,
                timestamp = System.currentTimeMillis()
            )
            repository.saveReport(report)
            _saveState.value = true
        }
    }
    
    fun resetState() {
        _saveState.value = false
    }
}
