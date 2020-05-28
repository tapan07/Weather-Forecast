package com.tapan.weather.forecast.home.presenter;

import android.util.Log;
import com.tapan.weather.forecast.R;
import com.tapan.weather.forecast.core.base.presenter.BasePresenter;
import com.tapan.weather.forecast.core.network.callback.WeatherAPIHandler;
import com.tapan.weather.forecast.core.network.request.WeatherRequest;
import com.tapan.weather.forecast.core.network.response.WeatherResponse;
import com.tapan.weather.forecast.core.rx.BaseObserver;
import com.tapan.weather.forecast.core.util.CoreConstants;
import com.tapan.weather.forecast.core.util.CoreUtils;
import com.tapan.weather.forecast.home.callback.HomeListener;
import com.tapan.weather.forecast.home.persistence.GeoLocation;
import com.tapan.weather.forecast.home.view.HomeView;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter<V extends HomeView> extends BasePresenter<V>
    implements HomeListener<V> {

  private static final String TAG = "HomePresenter";

  public HomePresenter() {
  }

  @Override
  public void getWeatherContent(double latitude, double longitude) {
    getView().getLocationDB().getGeoLocationDao()
        .getLocation(latitude, longitude)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(weatherCacheObserver(latitude, longitude));
  }

  private SingleObserver<? super GeoLocation> weatherCacheObserver(final double latitude,
      final double longitude) {
    return new BaseObserver<GeoLocation>() {
      @Override
      public void onSuccess(GeoLocation response) {
        getView().hideLoading();
        lazySet(DisposableHelper.DISPOSED);
        getView().updateWeatherDetails(response);
      }

      @Override
      public void onError(Throwable ex) {
        //getView().hideLoading();
        Log.e(TAG, ex.getMessage());
        onWeatherAPICall(latitude, longitude);
      }
    };
  }

  private void onWeatherAPICall(double latitude, double longitude) {
    if (getView() != null) {
      if (getView().isNetworkConnected()) {
        processWeatherContent(latitude, longitude);
      } else {
        getView().showError(R.string.msg_network_error);
      }
    }
  }

  /**
   * Retrieve the weather data
   */
  private void processWeatherContent(double latitude, double longitude) {
    getView().showLoading();
    WeatherAPIHandler weatherAPIHandler = new WeatherAPIHandler(CoreConstants.BASE_URL);

    WeatherRequest request = new WeatherRequest.Builder()
        .setLatitude(latitude)
        .setLongitude(longitude)
        .build();
    weatherContentRequest(weatherAPIHandler, request);
  }

  /**
   * Subscribing the weather content
   *
   * @param weatherAPIHandler API handler
   */
  private void weatherContentRequest(WeatherAPIHandler weatherAPIHandler, WeatherRequest request) {
    weatherAPIHandler.getWeatherDetails(request, CoreConstants.APP_ID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(weatherContentObserver());
  }

  /**
   * Handle the success/error response of weather content
   *
   * @return Single observer of result
   */
  private SingleObserver<? super WeatherResponse> weatherContentObserver() {
    return new BaseObserver<WeatherResponse>() {
      @Override
      public void onSuccess(WeatherResponse response) {
        //getView().hideLoading();
        lazySet(DisposableHelper.DISPOSED);
        updatePersistence(response);
      }

      @Override
      public void onError(Throwable ex) {
        getView().hideLoading();
        Log.e(TAG, ex.getMessage());
      }
    };
  }

  private void updatePersistence(WeatherResponse response) {
    GeoLocation geoLocation = new GeoLocation();
    geoLocation.setLatitude(getView().getCurrentLatitude());
    geoLocation.setLongitude(getView().getCurrentLongitude());

    if (response.getMain() != null) {
      geoLocation.setTemperature(CoreUtils.getFormattedCurrentTemp(response.getMain().getTemp()));
      geoLocation.setOverAllTemperature(
          CoreUtils.getFormattedOverallTemp(response.getMain().getTempMin(),
              response.getMain().getTempMax()));
    }

    if (response.getWeather() != null && response.getWeather().size() > 0
        && response.getWind() != null) {
      geoLocation.setDescription(
          CoreUtils.getFormattedDescription(response.getWeather().get(0).getDescription(),
              response.getWind().getSpeed()));
    }

    insertLocationData(geoLocation);
  }

  private void insertLocationData(final GeoLocation geoLocation) {
    getView().getLocationDB().getGeoLocationDao()
        .insertLocation(geoLocation)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletableObserver() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onComplete() {
            getView().hideLoading();
            getView().updateWeatherDetails(geoLocation);
          }

          @Override
          public void onError(Throwable e) {
            getView().hideLoading();
            getView().updateWeatherDetails(geoLocation);
          }
        });
  }
}
