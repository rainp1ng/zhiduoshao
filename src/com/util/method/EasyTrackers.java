package com.util.method;

import android.app.Activity;
import android.os.Bundle;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * An example Activity using Google Analytics and EasyTracker.
 */
public class EasyTrackers extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();
     // The rest of your onStart() code.
    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
  }

  @Override
  public void onStop() {
    super.onStop();
     // The rest of your onStop() code.
    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
  }
}