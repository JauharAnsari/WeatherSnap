package com.example.weathersnap.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.data.model.CurrentWeather
import com.example.weathersnap.data.model.GeocodingResult
import com.example.weathersnap.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(val city: GeocodingResult, val weather: CurrentWeather) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _suggestions = MutableStateFlow<List<GeocodingResult>>(emptyList())
    val suggestions: StateFlow<List<GeocodingResult>> = _suggestions

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private var searchJob: Job? = null
    private val suggestionCache = mutableMapOf<String, List<GeocodingResult>>()

    fun onQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length > 2) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(300)
                if (suggestionCache.containsKey(query)) {
                    _suggestions.value = suggestionCache[query]!!
                } else {
                    try {
                        val results = repository.searchCity(query)
                        suggestionCache[query] = results
                        _suggestions.value = results
                    } catch (e: Exception) {
                        _suggestions.value = emptyList()
                    }
                }
            }
        } else {
            _suggestions.value = emptyList()
        }
    }

    fun selectCity(city: GeocodingResult) {
        _searchQuery.value = city.displayName
        _suggestions.value = emptyList()
        fetchWeather(city)
    }

    private fun fetchWeather(city: GeocodingResult) {
        _weatherState.value = WeatherState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getWeather(city.latitude, city.longitude)
                if (response.current != null) {
                    _weatherState.value = WeatherState.Success(city, response.current)
                } else {
                    _weatherState.value = WeatherState.Error("No weather data found")
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "Failed to fetch weather")
            }
        }
    }
}
