package com.tapan.weather.forecast.home.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface GeoLocationDao {

  @Query("SELECT * FROM GeoLocation " + " where latitude =:latitude and longitude =:longitude")
  Single<GeoLocation> getLocation(double latitude, double longitude);

  @Insert
  Completable insertLocation(GeoLocation geoLocation);
}
