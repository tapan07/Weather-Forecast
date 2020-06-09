package com.tapan.weather.forecast.core.network.service

import com.tapan.weather.forecast.core.network.response.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Handle the API services
 *
 * @author Tapan Rana (ttapan.rana@gmail.com)
 */
interface WeatherService {
  @Headers("Content-Type:application/json")
  @GET("weather")
  fun getWeatherDetails(
    @Query("lat") latitude: Double,
    @Query("lon") longitude: Double,
    @Query("appid") appId: String
  ): Single<WeatherResponse>
}