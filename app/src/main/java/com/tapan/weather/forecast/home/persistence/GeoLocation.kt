package com.tapan.weather.forecast.home.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "GeoLocation")
class GeoLocation : Serializable {

  @PrimaryKey(autoGenerate = true)
  var id = 0

  @ColumnInfo(name = "latitude")
  var latitude = 0.0

  @ColumnInfo(name = "longitude")
  var longitude = 0.0

  @ColumnInfo(name = "featurename")
  var featureName: String? = null

  @ColumnInfo(name = "locality")
  var locality: String? = null

  @ColumnInfo(name = "state")
  var state: String? = null

  @ColumnInfo(name = "country")
  var country: String? = null

  @ColumnInfo(name = "pincode")
  var pinCode: String? = null

  @ColumnInfo(name = "temperature")
  var temperature: String? = null

  @ColumnInfo(name = "overalltemperature")
  var overAllTemperature: String? = null

  @ColumnInfo(name = "description")
  var description: String? = null
}
