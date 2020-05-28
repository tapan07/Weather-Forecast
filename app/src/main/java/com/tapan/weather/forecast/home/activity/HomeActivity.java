package com.tapan.weather.forecast.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tapan.weather.forecast.R;
import com.tapan.weather.forecast.core.base.activity.BaseActivity;
import com.tapan.weather.forecast.core.util.CoreConstants;
import com.tapan.weather.forecast.core.util.CoreUtils;
import com.tapan.weather.forecast.home.model.GeoCodeModel;
import com.tapan.weather.forecast.home.persistence.GeoLocation;
import com.tapan.weather.forecast.home.persistence.GeoLocationDatabase;
import com.tapan.weather.forecast.home.presenter.HomePresenter;
import com.tapan.weather.forecast.home.service.CurrentLocationService;
import com.tapan.weather.forecast.home.view.HomeView;

public class HomeActivity extends BaseActivity implements HomeView {

  private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
  private TextView txtDisplayData;
  private AppCompatTextView txtCurrentTime;
  private AppCompatTextView txtTemperature;
  private AppCompatTextView txtOverallTemperature;
  private AppCompatTextView txtDescription;
  private HomePresenter<HomeView> mHomePresenter;
  private GeoLocationDatabase locationDatabase;
  private FusedLocationProviderClient fusedLocationClient;
  private LocationAddressResultReceiver addressResultReceiver;
  private Location currentLocation;
  private LocationCallback locationCallback;
  private double currentLatitude, currentLongitude;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setUp();
    initiateData();
    getCurrentLocation();
  }

  private void initiateData() {
    mHomePresenter = new HomePresenter<>();
    mHomePresenter.onAttach(this);
    locationDatabase = GeoLocationDatabase.getInstance(this);
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.activity_home;
  }

  @Override
  protected void setUp() {
    txtDisplayData = findViewById(R.id.display_data);

    txtCurrentTime = findViewById(R.id.current_time);
    txtTemperature = findViewById(R.id.current_temp);
    txtOverallTemperature = findViewById(R.id.overall_temp);
    txtDescription = findViewById(R.id.description);
  }

  private void getCurrentLocation() {
    addressResultReceiver = new LocationAddressResultReceiver(new Handler());
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    locationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        currentLocation = locationResult.getLocations().get(0);
        getAddress();
      }
    };
  }

  private void startLocationUpdates() {
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
          LOCATION_PERMISSION_REQUEST_CODE);
    } else {
      LocationRequest locationRequest = new LocationRequest();
      locationRequest.setInterval(2 * 60 * 1000);
      locationRequest.setFastestInterval(60000);
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

      fusedLocationClient.requestLocationUpdates(locationRequest,
          locationCallback,
          null);
    }
  }

  private void getAddress() {

    if (!Geocoder.isPresent()) {
      Toast.makeText(this,
          "Can't find current address, ",
          Toast.LENGTH_SHORT).show();
      return;
    }

    Intent intent = new Intent(this, CurrentLocationService.class);
    intent.putExtra("add_receiver", addressResultReceiver);
    intent.putExtra("add_location", currentLocation);
    startService(intent);
  }

  @Override
  public void updateWeatherDetails(GeoLocation response) {
    txtCurrentTime.setText(CoreUtils.getTodayDate(CoreConstants.DATE_FORMAT));
    txtTemperature.setText(response.getTemperature());
    txtOverallTemperature.setText(response.getOverAllTemperature());
    txtDescription.setText(response.getDescription());
  }

  @Override
  public GeoLocationDatabase getLocationDB() {
    return locationDatabase;
  }

  @Override
  public double getCurrentLatitude() {
    return currentLatitude;
  }

  @Override
  public double getCurrentLongitude() {
    return currentLongitude;
  }

  private void showResults(GeoCodeModel geoCodeDetails) {
    txtDisplayData.setText(geoCodeDetails.getLocality() + "\n" +
        geoCodeDetails.getState() + "\n" +
        geoCodeDetails.getCountry());
    currentLatitude =
        Double.parseDouble(CoreConstants.decimalFormat.format(geoCodeDetails.getLatitude()));
    currentLongitude =
        Double.parseDouble(CoreConstants.decimalFormat.format(geoCodeDetails.getLongitude()));
    mHomePresenter.getWeatherContent(currentLatitude, currentLongitude);
  }

  @Override
  protected void onResume() {
    super.onResume();
    startLocationUpdates();
  }

  @Override
  protected void onPause() {
    super.onPause();
    fusedLocationClient.removeLocationUpdates(locationCallback);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  public class LocationAddressResultReceiver extends ResultReceiver {

    LocationAddressResultReceiver(Handler handler) {
      super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

      if (resultCode == 0) {
        //Last Location can be null for various reasons
        //for example the api is called first time
        //so retry till location is set
        //since intent service runs on background thread, it doesn't block main thread
        Log.d("Address", "Location null retrying");
        getAddress();
      }

      if (resultCode == 1) {
        Toast.makeText(HomeActivity.this,
            "Address not found, ",
            Toast.LENGTH_SHORT).show();
      }

      GeoCodeModel geoCodeDetails = resultData.getParcelable("geo_code_details");

      showResults(geoCodeDetails);
    }
  }
}
