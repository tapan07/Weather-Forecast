package com.tapan.weather.forecast.core.base.presenter

import com.tapan.weather.forecast.R
import com.tapan.weather.forecast.core.base.callback.BaseListener
import com.tapan.weather.forecast.core.base.view.BaseView
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

open class BasePresenter<V : BaseView> : BaseListener<V> {

  var view: V? = null

  val isViewAttached: Boolean
    get() = view != null

  companion object {
    private const val API_STATUS_CODE_LOCAL_ERROR = 0
  }

  override fun onAttach(view: V) {
    this.view = view
  }

  override fun onDetach() {
    view = null
  }

  override fun handleError(error: Throwable) {
    if (error is HttpException) {
      when (error.code()) {
        HttpsURLConnection.HTTP_UNAUTHORIZED ->
          view?.showError("Unauthorised User")
        HttpsURLConnection.HTTP_FORBIDDEN,
        HttpsURLConnection.HTTP_BAD_REQUEST,
        HttpsURLConnection.HTTP_INTERNAL_ERROR ->
          view?.showError(R.string.unknown_error)
        API_STATUS_CODE_LOCAL_ERROR ->
          view?.showError("No Internet Connection")
        else ->
          view?.showError(error.getLocalizedMessage())
      }
    } else {
      error.message?.let { view?.showError(it) }
    }
  }
}