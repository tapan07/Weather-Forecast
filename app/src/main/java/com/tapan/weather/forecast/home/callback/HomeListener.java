package com.tapan.weather.forecast.home.callback;

import com.tapan.weather.forecast.core.base.callback.BaseListener;
import com.tapan.weather.forecast.home.view.HomeView;

public interface HomeListener<V extends HomeView> extends BaseListener<V> {

  void getWeatherContent(double latitude, double longitude);
}
