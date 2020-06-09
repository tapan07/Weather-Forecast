package com.tapan.weather.forecast.home.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GeoLocationDao {
  @Query("SELECT * FROM GeoLocation " + " where latitude =:latitude and longitude =:longitude")
  fun getLocation(
    latitude: Double,
    longitude: Double
  ): Single<GeoLocation>

  @Insert
  fun insertLocation(geoLocation: GeoLocation): Completable
}