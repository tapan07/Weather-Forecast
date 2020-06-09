package com.tapan.weather.forecast.home.presenter

import android.util.Log
import com.tapan.weather.forecast.R.string
import com.tapan.weather.forecast.core.base.presenter.BasePresenter
import com.tapan.weather.forecast.core.network.callback.WeatherAPIHandler
import com.tapan.weather.forecast.core.network.request.WeatherRequest
import com.tapan.weather.forecast.core.network.request.WeatherRequest.Builder
import com.tapan.weather.forecast.core.network.response.WeatherResponse
import com.tapan.weather.forecast.core.rx.BaseObserver
import com.tapan.weather.forecast.core.util.CoreConstants
import com.tapan.weather.forecast.core.util.CoreUtils.getFormattedCurrentTemp
import com.tapan.weather.forecast.core.util.CoreUtils.getFormattedDescription
import com.tapan.weather.forecast.core.util.CoreUtils.getFormattedOverallTemp
import com.tapan.weather.forecast.home.callback.HomeListener
import com.tapan.weather.forecast.home.persistence.GeoLocation
import com.tapan.weather.forecast.home.view.HomeView
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper.DISPOSED
import io.reactivex.schedulers.Schedulers

class HomePresenter<V : HomeView> : BasePresenter<V>(), HomeListener<V> {

  companion object {
    private const val TAG = "HomePresenter"
  }

  override
  fun getWeatherContent(
    latitude: Double,
    longitude: Double
  ) {
    view!!.locationDB?.geoLocationDao
        ?.getLocation(latitude, longitude)
        ?.subscribeOn(Schedulers.io())
        ?.observeOn(AndroidSchedulers.mainThread())
        ?.subscribe(weatherCacheObserver(latitude, longitude))
  }

  private fun weatherCacheObserver(
    latitude: Double,
    longitude: Double
  ):
      SingleObserver<in GeoLocation> {
    return object : BaseObserver<GeoLocation?>() {

      override
      fun onSuccess(response: GeoLocation) {
        view!!.hideLoading()
        lazySet(DISPOSED)
        view!!.updateWeatherDetails(response)
      }

      override
      fun onError(ex: Throwable) {
        //getView().hideLoading();
        Log.e(TAG, ex.message)
        onWeatherAPICall(latitude, longitude)
      }
    }
  }

  private fun onWeatherAPICall(
    latitude: Double,
    longitude: Double
  ) {
    if (view != null) {
      if (view!!.isNetworkConnected()) {
        processWeatherContent(latitude, longitude)
      } else {
        view!!.showError(string.msg_network_error)
      }
    }
  }

  /**
   * Retrieve the weather data
   */
  private fun processWeatherContent(
    latitude: Double,
    longitude: Double
  ) {
    view!!.showLoading()
    val weatherAPIHandler = WeatherAPIHandler(CoreConstants.BASE_URL)
    val request = Builder()
        .setLatitude(latitude)
        .setLongitude(longitude)
        .build()
    weatherContentRequest(weatherAPIHandler, request)
  }

  /**
   * Subscribing the weather content
   *
   * @param weatherAPIHandler API handler
   */
  private fun weatherContentRequest(
    weatherAPIHandler: WeatherAPIHandler,
    request: WeatherRequest
  ) {
    weatherAPIHandler.getWeatherDetails(request, CoreConstants.APP_ID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(weatherContentObserver())
  }

  /**
   * Handle the success/error response of weather content
   *
   * @return Single observer of result
   */
  private fun weatherContentObserver(): SingleObserver<in WeatherResponse> {
    return object : BaseObserver<WeatherResponse?>() {

      override
      fun onSuccess(response: WeatherResponse) {
        //getView().hideLoading();
        lazySet(DISPOSED)
        updatePersistence(response)
      }

      override
      fun onError(ex: Throwable) {
        view!!.hideLoading()
        Log.e(TAG, ex.message)
      }
    }
  }

  private fun updatePersistence(response: WeatherResponse) {
    val geoLocation = GeoLocation()
    geoLocation.latitude = view!!.currentLatitude
    geoLocation.longitude = view!!.currentLongitude

    if (response.main != null) {
      geoLocation.temperature = getFormattedCurrentTemp(response.main!!.temp!!)
      geoLocation.overAllTemperature = getFormattedOverallTemp(
          response.main!!.tempMin!!,
          response.main!!.tempMax!!
      )
    }
    if (response.weather != null && response.weather!!.isNotEmpty() && response.wind != null
    ) {
      geoLocation.description = getFormattedDescription(
          response.weather!![0].description,
          response.wind!!.speed!!
      )
    }
    insertLocationData(geoLocation)
  }

  private fun insertLocationData(geoLocation: GeoLocation) {
    view!!.locationDB?.geoLocationDao
        ?.insertLocation(geoLocation)
        ?.subscribeOn(Schedulers.io())
        ?.observeOn(AndroidSchedulers.mainThread())
        ?.subscribe(object : CompletableObserver {
          override fun onSubscribe(d: Disposable) {}
          override fun onComplete() {
            view!!.hideLoading()
            view!!.updateWeatherDetails(geoLocation)
          }

          override fun onError(e: Throwable) {
            view!!.hideLoading()
            view!!.updateWeatherDetails(geoLocation)
          }
        })
  }
}