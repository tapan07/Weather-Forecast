package com.tapan.weather.forecast.core.util

import java.text.DecimalFormat

object CoreConstants {
  const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
  const val APP_ID = "5ad7218f2e11df834b0eaf3a33a39d2a"
  const val DATE_FORMAT = "dd MMM yyyy HH:mm"
  const val TABLE_NAME = "GeoLocation"
  const val DB_NAME = "GeoLocationDB"
  var decimalFormat = DecimalFormat("#.##")
}