package com.tapan.weather.forecast.core.base.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import com.tapan.weather.forecast.R

class CustomDialog(context: Context) : Dialog(context) {

  private var mErrorMessage: AppCompatTextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    )

    setContentView(R.layout.dialog_custom)
    mErrorMessage = findViewById(R.id.error_message)
  }

  fun setMessage(message: String?) {
    mErrorMessage?.text = message
  }
}