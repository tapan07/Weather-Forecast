package com.tapan.weather.forecast.core.base.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import com.tapan.weather.forecast.R;

public class CustomDialog extends Dialog {
  private AppCompatTextView mErrorMessage;

  public CustomDialog(@NonNull Context context) {
    super(context);
  }

  public CustomDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  protected CustomDialog(@NonNull Context context, boolean cancelable,
      @Nullable OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_custom);
    mErrorMessage = findViewById(R.id.error_message);
  }

  public void setMessage(String message) {
    mErrorMessage.setText(message);
  }
}
