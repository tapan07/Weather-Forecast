package com.tapan.weather.forecast.home.service;

import android.content.Context;
import com.tapan.weather.forecast.home.persistence.GeoLocation;

public class LocationManager {

  private Context context;

  public LocationManager(Context context) {
    this.context = context;
  }

  public void getLocation(GeoLocation geoLocation) {
    //int permission =
    //    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    //
    //if (permission == PackageManager.PERMISSION_GRANTED) {
    //  LocationServices.getFusedLocationProviderClient(context).getLastLocation().
    //      addOnSuccessListener() -> {
    //
    //  }
    //}
  }
}
