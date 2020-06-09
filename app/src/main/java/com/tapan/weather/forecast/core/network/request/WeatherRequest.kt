package com.tapan.weather.forecast.core.network.request

class WeatherRequest internal constructor(builder: Builder) {

  val latitude: Double
  val longitude: Double

  init {
    latitude = builder.latitude
    longitude = builder.longitude
  }

  class Builder {
    var latitude = 0.0
    var longitude = 0.0

    fun setLatitude(latitude: Double): Builder {
      this.latitude = latitude
      return this
    }

    fun setLongitude(longitude: Double): Builder {
      this.longitude = longitude
      return this
    }

    fun build(): WeatherRequest {
      return WeatherRequest(this)
    }
  }
}