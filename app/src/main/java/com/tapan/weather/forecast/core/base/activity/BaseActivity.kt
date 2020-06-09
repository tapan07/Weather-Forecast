package com.tapan.weather.forecast.core.base.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tapan.weather.forecast.R
import com.tapan.weather.forecast.core.base.custom.CustomDialog
import com.tapan.weather.forecast.core.base.view.BaseView

abstract class BaseActivity : AppCompatActivity(), BaseView {

  private var mProgressDialog: CustomDialog? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutResource())
    showLoadingDialog()
  }

  protected abstract fun layoutResource(): Int

  protected abstract fun setUp()

  override fun showLoading() {
    if (mProgressDialog != null && !mProgressDialog!!.isShowing) {
      mProgressDialog!!.show()
    }
  }

  override fun showLoadingDialog() {
    mProgressDialog = CustomDialog(this)
    mProgressDialog?.setCancelable(false)
    mProgressDialog?.setCanceledOnTouchOutside(false)
    mProgressDialog?.setMessage(getString(R.string.msg_loading))
    if (mProgressDialog?.window != null) {
      mProgressDialog!!.window
          .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
  }

  override fun isNetworkConnected(): Boolean {
    val networkInfo = getNetworkInformation(this) ?: return false
    return networkInfo.isConnected
  }

  override fun hideLoading() {
    if (mProgressDialog!!.isShowing) {
      mProgressDialog?.cancel()
    }
  }

  override fun showError(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG)
        .show()
  }

  override fun showError(resId: Int) {
    showError(getString(resId))
  }

  private fun getNetworkInformation(context: Context): NetworkInfo? {
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo
  }
}