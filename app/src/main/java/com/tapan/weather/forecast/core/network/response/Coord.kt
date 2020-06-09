package com.tapan.weather.forecast.core.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Coord {
  @SerializedName("lon")
  @Expose
  var lon = 0.0

  @SerializedName("lat")
  @Expose
  var lat = 0.0
}