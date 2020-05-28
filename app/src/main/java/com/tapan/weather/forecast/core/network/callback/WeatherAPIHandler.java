package com.tapan.weather.forecast.core.network.callback;

import com.tapan.weather.forecast.core.network.request.WeatherRequest;
import com.tapan.weather.forecast.core.network.response.WeatherResponse;
import com.tapan.weather.forecast.core.network.service.WeatherService;
import io.reactivex.Single;

/**
 * API handler to fetch the weather content
 *
 * @author Tapan Rana (ttapan.rana@gmail.com)
 */
public class WeatherAPIHandler {

  private String baseUrl;

  public WeatherAPIHandler(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Retrieve the content of weather details
   *
   * @return {@link Single} result of the content
   */
  public Single<WeatherResponse> getWeatherDetails(WeatherRequest request, String appId) {
    WeatherService weatherService = RetrofitHandler.getRetrofitInstance(baseUrl)
        .create(WeatherService.class);

    return weatherService.getWeatherDetails(request.getLatitude(), request.getLongitude(), appId);
  }
}
