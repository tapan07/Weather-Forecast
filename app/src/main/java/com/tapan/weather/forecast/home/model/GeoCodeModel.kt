package com.tapan.weather.forecast.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoCodeModel(
  var featureName: String,
  var locality: String,
  var state: String,
  var country: String,
  var pinCode: String,
  var latitude: Double,
  var longitude: Double
) : Parcelable
