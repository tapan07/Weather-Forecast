package com.tapan.weather.forecast.home.activity

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tapan.weather.forecast.R.id
import com.tapan.weather.forecast.R.layout
import com.tapan.weather.forecast.core.base.activity.BaseActivity
import com.tapan.weather.forecast.core.util.CoreConstants
import com.tapan.weather.forecast.core.util.CoreUtils.getTodayDate
import com.tapan.weather.forecast.home.model.GeoCodeModel
import com.tapan.weather.forecast.home.persistence.GeoLocation
import com.tapan.weather.forecast.home.persistence.GeoLocationDatabase
import com.tapan.weather.forecast.home.presenter.HomePresenter
import com.tapan.weather.forecast.home.service.CurrentLocationService
import com.tapan.weather.forecast.home.view.HomeView

class HomeActivity : BaseActivity(), HomeView {

  private lateinit var txtDisplayData: TextView
  private lateinit var txtCurrentTime: AppCompatTextView
  private lateinit var txtTemperature: AppCompatTextView
  private lateinit var txtOverallTemperature: AppCompatTextView
  private lateinit var txtDescription: AppCompatTextView
  private lateinit var mHomePresenter: HomePresenter<HomeView>
  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var addressResultReceiver: LocationAddressResultReceiver
  private lateinit var currentLocation: Location
  private lateinit var locationCallback: LocationCallback
  override var currentLatitude = 0.0
    private set
  override var currentLongitude = 0.0
    private set

  companion object {
    private const val LOCATION_PERMISSION_REQUEST_CODE = 2
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setUp()
    initiateData()
    getCurrentLocation()
  }

  private fun initiateData() {
    mHomePresenter = HomePresenter()
    mHomePresenter.onAttach(this)
  }

  override fun layoutResource(): Int {
    return layout.activity_home
  }

  override fun setUp() {
    txtDisplayData = findViewById(id.display_data)
    txtCurrentTime = findViewById(id.current_time)
    txtTemperature = findViewById(id.current_temp)
    txtOverallTemperature = findViewById(id.overall_temp)
    txtDescription = findViewById(id.description)
  }

  private fun getCurrentLocation() {
    addressResultReceiver =
      LocationAddressResultReceiver(
          Handler()
      )
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult) {
        currentLocation = locationResult.locations[0]
        address
      }
    }
  }

  private fun startLocationUpdates() {
    if (ContextCompat.checkSelfPermission(
            this,
            permission.ACCESS_FINE_LOCATION
        )
        != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(
          this, arrayOf(permission.ACCESS_FINE_LOCATION),
          LOCATION_PERMISSION_REQUEST_CODE
      )
    } else {
      val locationRequest = LocationRequest()
      locationRequest.interval = 120 * 60 * 1000.toLong()
      locationRequest.fastestInterval = 60000
      locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      fusedLocationClient.requestLocationUpdates(
          locationRequest,
          locationCallback,
          null
      )
    }
  }

  private val address: Unit
    get() {
      if (!Geocoder.isPresent()) {
        Toast.makeText(this, "Can't find current address, ", Toast.LENGTH_SHORT)
            .show()
        return
      }
      val intent = Intent(this, CurrentLocationService::class.java)
      intent.putExtra("add_receiver", addressResultReceiver)
      intent.putExtra("add_location", currentLocation)
      startService(intent)
    }

  override fun updateWeatherDetails(response: GeoLocation) {
    txtCurrentTime.text = getTodayDate(CoreConstants.DATE_FORMAT)
    txtTemperature.text = response.temperature
    txtOverallTemperature.text = response.overAllTemperature
    txtDescription.text = response.description
  }

  override val locationDB: GeoLocationDatabase?
    get() = GeoLocationDatabase.getInstance(context = this)

  private fun showResults(geoCodeDetails: GeoCodeModel?) {
    txtDisplayData.text = (geoCodeDetails?.locality + "\n" +
        geoCodeDetails?.state + "\n" +
        geoCodeDetails?.country).trimIndent()
    currentLatitude = CoreConstants.decimalFormat.format(
        geoCodeDetails?.latitude
    )
        .toDouble()
    currentLongitude = CoreConstants.decimalFormat.format(
        geoCodeDetails?.longitude
    )
        .toDouble()
    mHomePresenter.getWeatherContent(currentLatitude, currentLongitude)
  }

  override fun onResume() {
    super.onResume()
    startLocationUpdates()
  }

  override fun onPause() {
    super.onPause()
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }

  inner class LocationAddressResultReceiver internal constructor(handler: Handler?) :
      ResultReceiver(handler) {
    override fun onReceiveResult(
      resultCode: Int,
      resultData: Bundle
    ) {
      if (resultCode == 0) {
        address
      }
      if (resultCode == 1) {
        Toast.makeText(
            this@HomeActivity,
            "Address not found, ",
            Toast.LENGTH_SHORT
        )
            .show()
      }
      val geoCodeDetails: GeoCodeModel? = resultData.getParcelable("geo_code_details")
      showResults(geoCodeDetails)
    }
  }
}