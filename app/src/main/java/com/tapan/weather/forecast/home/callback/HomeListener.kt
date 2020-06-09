package com.tapan.weather.forecast.home.callback

import com.tapan.weather.forecast.core.base.callback.BaseListener
import com.tapan.weather.forecast.home.view.HomeView

interface HomeListener<V : HomeView> : BaseListener<V> {

  fun getWeatherContent(
    latitude: Double,
    longitude: Double
  )
}