package com.tapan.weather.forecast.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CoreUtils {

  /**
   * Convert kelvin data to celcius data
   *
   * @param kelvinData kelvin type
   * @return celcius value
   */
  public static int convertKelvinToCelcius(float kelvinData) {
    return (int) (kelvinData - 273.15);
  }

  public static String getTodayDate(String dateFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    return sdf.format(new Date());
  }

  public static String getFormattedOverallTemp(float minTemp, float maxTemp) {
    return String.format("%s℃ %s℃", "Min: " + convertKelvinToCelcius(minTemp),
        "Max: " + convertKelvinToCelcius(maxTemp));
  }

  public static String getFormattedCurrentTemp(float temp) {
    return String.format("%s℃", String.valueOf(convertKelvinToCelcius(temp)));
  }

  public static String getFormattedDescription(String description, float speed) {
    return String.format("%s %s", description, String.format("%s km/h", speed));
  }
}
