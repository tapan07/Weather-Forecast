package com.tapan.weather.forecast.home.service

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.tapan.weather.forecast.home.model.GeoCodeModel
import java.util.Locale

class CurrentLocationService : IntentService(
    IDENTIFIER
) {

  private var addressResultReceiver: ResultReceiver? = null

  companion object {
    private const val IDENTIFIER = "CurrentLocationService"
  }

  override
  fun onHandleIntent(intent: Intent?) {
    val msg: String
    if (intent != null) {
      addressResultReceiver = intent.getParcelableExtra("add_receiver")
    }
    if (addressResultReceiver == null) {
      Log.e(
          "GetAddressIntentService",
          "No receiver, not processing the request further"
      )
      return
    }
    //val geoCodeModel = GeoCodeModel()
    var location: Location? = null
    if (intent != null) {
      location = intent.getParcelableExtra("add_location")
    }
    if (location == null) {
      msg = "No location, can't go further without location"
      sendResultsToReceiver(0, null)
      return
    }
    //call GeoCoder getFromLocation to get address
    //returns list of addresses, take first one and send info to result receiver
    val geocoder = Geocoder(this, Locale.getDefault())
    var addresses: List<Address>? = null
    try {
      addresses = geocoder.getFromLocation(
          location.latitude,
          location.longitude,
          1
      )
    } catch (ioException: Exception) {
      Log.e("", "Error in getting address for the location")
    }
    if (addresses == null || addresses.isEmpty()) {
      msg = "No address found for the location"
      sendResultsToReceiver(
          1,
          GeoCodeModel(
              "", "", "", "", "",
              location.latitude, location.longitude
          )
      )
    } else {
      val address = addresses[0]

      sendResultsToReceiver(
          2,
          GeoCodeModel(
              address.featureName, address.locality,
              address.adminArea, address.countryName, address.postalCode,
              location.latitude, location.longitude
          )
      )
    }
  }

  //to send results to receiver in the source activity
  private fun sendResultsToReceiver(
    resultCode: Int,
    geoCodeModel: GeoCodeModel?
  ) {
    val bundle = Bundle()
    //bundle.putString("address_result", message);
    bundle.putParcelable("geo_code_details", geoCodeModel)
    addressResultReceiver!!.send(resultCode, bundle)
  }
}