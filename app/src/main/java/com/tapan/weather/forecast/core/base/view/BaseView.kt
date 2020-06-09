package com.tapan.weather.forecast.core.base.view

import androidx.annotation.StringRes

public interface BaseView {

  fun showLoading()

  fun hideLoading()

  fun showError(@StringRes resId: Int)

  fun showError(message: String)

  fun showLoadingDialog()

  fun isNetworkConnected(): Boolean
}