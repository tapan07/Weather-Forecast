package com.tapan.weather.forecast.home.view;

import com.tapan.weather.forecast.core.base.view.BaseView;
import com.tapan.weather.forecast.home.persistence.GeoLocation;
import com.tapan.weather.forecast.home.persistence.GeoLocationDatabase;

public interface HomeView extends BaseView {

  void updateWeatherDetails(GeoLocation response);

  GeoLocationDatabase getLocationDB();

  double getCurrentLatitude();

  double getCurrentLongitude();
}
