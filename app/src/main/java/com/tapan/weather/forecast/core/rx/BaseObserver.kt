package com.tapan.weather.forecast.core.rx

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper
import java.util.concurrent.atomic.AtomicReference

abstract class BaseObserver<T> : AtomicReference<Disposable?>(),
    SingleObserver<T>, Disposable {

  override fun onSubscribe(d: Disposable) {
    DisposableHelper.setOnce(this, d)
  }

  override fun dispose() {
    DisposableHelper.dispose(this)
  }

  override fun isDisposed(): Boolean {
    return false
  }
}