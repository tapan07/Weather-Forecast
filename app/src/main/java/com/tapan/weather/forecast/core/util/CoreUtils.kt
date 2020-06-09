package com.tapan.weather.forecast.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CoreUtils {
  /**
   * Convert kelvin data to celcius data
   *
   * @param kelvinData kelvin type
   * @return celcius value
   */
  fun convertKelvinToCelcius(kelvinData: Float): Int {
    return (kelvinData - 273.15).toInt()
  }

  fun getTodayDate(dateFormat: String?): String {
    val sdf =
      SimpleDateFormat(dateFormat, Locale.ENGLISH)
    return sdf.format(Date())
  }

  fun getFormattedOverallTemp(
    minTemp: Float,
    maxTemp: Float
  ): String {
    return String.format(
        "%s℃ %s℃", "Min: " + convertKelvinToCelcius(minTemp),
        "Max: " + convertKelvinToCelcius(maxTemp)
    )
  }

  fun getFormattedCurrentTemp(temp: Float): String {
    return String.format(
        "%s℃", convertKelvinToCelcius(temp)
        .toString()
    )
  }

  fun getFormattedDescription(
    description: String?,
    speed: Float
  ): String {
    return String.format("%s %s", description, String.format("%s km/h", speed))
  }
}