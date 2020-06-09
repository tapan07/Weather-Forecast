package com.tapan.weather.forecast.home.view

import com.tapan.weather.forecast.core.base.view.BaseView
import com.tapan.weather.forecast.home.persistence.GeoLocation
import com.tapan.weather.forecast.home.persistence.GeoLocationDatabase

interface HomeView : BaseView {
  fun updateWeatherDetails(response: GeoLocation)
  val locationDB: GeoLocationDatabase?
  val currentLatitude: Double
  val currentLongitude: Double
}