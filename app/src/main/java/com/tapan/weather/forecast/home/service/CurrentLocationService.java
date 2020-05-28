package com.tapan.weather.forecast.home.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import androidx.annotation.Nullable;
import com.tapan.weather.forecast.home.model.GeoCodeModel;
import java.util.List;
import java.util.Locale;

public class CurrentLocationService extends IntentService {

  private static final String IDENTIFIER = "CurrentLocationService";
  private ResultReceiver addressResultReceiver;

  public CurrentLocationService() {
    super(IDENTIFIER);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    String msg;
    if (intent != null) {
      addressResultReceiver = intent.getParcelableExtra("add_receiver");
    }

    if (addressResultReceiver == null) {
      Log.e("GetAddressIntentService",
          "No receiver, not processing the request further");
      return;
    }

    GeoCodeModel geoCodeModel = new GeoCodeModel();
    Location location = null;
    if (intent != null) {
      location = intent.getParcelableExtra("add_location");
    }

    if (location == null) {
      msg = "No location, can't go further without location";
      sendResultsToReceiver(0, geoCodeModel);
      return;
    }
    //call GeoCoder getFromLocation to get address
    //returns list of addresses, take first one and send info to result receiver
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    List<Address> addresses = null;

    try {
      geoCodeModel.setLatitude(location.getLatitude());
      geoCodeModel.setLongitude(location.getLongitude());

      addresses = geocoder.getFromLocation(
          location.getLatitude(),
          location.getLongitude(),
          1);
    } catch (Exception ioException) {
      Log.e("", "Error in getting address for the location");
    }

    if (addresses == null || addresses.size() == 0) {
      msg = "No address found for the location";
      sendResultsToReceiver(1, geoCodeModel);
    } else {
      Address address = addresses.get(0);

      geoCodeModel.setFeatureName(address.getFeatureName());
      geoCodeModel.setLocality(address.getLocality());
      geoCodeModel.setState(address.getAdminArea());
      geoCodeModel.setCountry(address.getCountryName());
      geoCodeModel.setPinCode(address.getPostalCode());

      String addressDetails = address.getFeatureName()
          + "\n"
          + address.getThoroughfare()
          + "\n"
          + "Locality: "
          + address.getLocality()
          + "\n"
          + "County: "
          + address.getSubAdminArea()
          + "\n"
          + "State: "
          + address.getAdminArea()
          + "\n"
          + "Country: "
          + address.getCountryName()
          + "\n"
          + "Postal Code: "
          + address.getPostalCode()
          + "\n";
      sendResultsToReceiver(2, geoCodeModel);
    }
  }

  //to send results to receiver in the source activity
  private void sendResultsToReceiver(int resultCode,
      GeoCodeModel geoCodeModel) {
    Bundle bundle = new Bundle();
    //bundle.putString("address_result", message);
    bundle.putParcelable("geo_code_details", geoCodeModel);
    addressResultReceiver.send(resultCode, bundle);
  }
}
