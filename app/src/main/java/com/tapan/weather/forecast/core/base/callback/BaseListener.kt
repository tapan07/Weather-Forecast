package com.tapan.weather.forecast.core.base.callback

import com.tapan.weather.forecast.core.base.view.BaseView

interface BaseListener<V : BaseView> {
  fun onAttach(view: V)
  fun onDetach()
  fun handleError(error: Throwable)
}