package com.tapan.weather.forecast.core.network.callback

import com.tapan.weather.forecast.core.network.request.WeatherRequest
import com.tapan.weather.forecast.core.network.response.WeatherResponse
import com.tapan.weather.forecast.core.network.service.WeatherService
import io.reactivex.Single

/**
 * API handler to fetch the weather content
 *
 * @author Tapan Rana (ttapan.rana@gmail.com)
 */
class WeatherAPIHandler(private val baseUrl: String) {

  /**
   * Retrieve the content of weather details
   *
   * @return [Single] result of the content
   */
  fun getWeatherDetails(
    request: WeatherRequest,
    appId: String
  ): Single<WeatherResponse> {
    val weatherService = RetrofitHandler.getRetrofitInstance(baseUrl)
        .create(WeatherService::class.java)
    return weatherService.getWeatherDetails(request.latitude, request.longitude, appId)
  }
}