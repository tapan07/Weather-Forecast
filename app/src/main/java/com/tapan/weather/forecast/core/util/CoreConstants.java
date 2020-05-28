package com.tapan.weather.forecast.core.util;

import java.text.DecimalFormat;

public class CoreConstants {
  public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
  public static final String APP_ID = "5ad7218f2e11df834b0eaf3a33a39d2a";
  public static final String DATE_FORMAT = "dd MMM yyyy HH:mm";
  public static final String TABLE_NAME = "GeoLocation";
  public static final String DB_NAME = "GeoLocationDB";
  public static DecimalFormat decimalFormat = new DecimalFormat("#.##");
}
