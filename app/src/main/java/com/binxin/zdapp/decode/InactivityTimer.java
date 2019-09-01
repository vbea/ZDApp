package com.binxin.zdapp.decode;

import com.binxin.zdapp.common.executor.AsyncTaskExecInterface;
import com.binxin.zdapp.common.executor.AsyncTaskExecManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.util.Log;


/**
 * Finishes an activity after a period of inactivity if the device is on battery power.
 */
public final class InactivityTimer {

  private static final String TAG = InactivityTimer.class.getSimpleName();

  private static final long INACTIVITY_DELAY_MS = 5 * 60 * 1000L;

  private final Activity activity;
  private final AsyncTaskExecInterface taskExec;
  private final BroadcastReceiver powerStatusReceiver;
  private InactivityAsyncTask inactivityTask;

  public InactivityTimer(Activity activity) {
    this.activity = activity;
    taskExec = new AsyncTaskExecManager().build();
    powerStatusReceiver = new PowerStatusReceiver();
    onActivity();
  }

  public synchronized void onActivity() {
    cancel();
    inactivityTask = new InactivityAsyncTask();
    taskExec.execute(inactivityTask);
  }

  public void onPause() {
    cancel();
    activity.unregisterReceiver(powerStatusReceiver);
  }

  public void onResume(){
    activity.registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    onActivity();
  }

  private synchronized  void cancel() {
    AsyncTask<?,?,?> task = inactivityTask;
    if (task != null) {
      task.cancel(true);
      inactivityTask = null;
    }
  }

  public void shutdown() {
    cancel();
  }

  private final class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
      if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
        // 0 indicates that we're on battery
        boolean onBatteryNow = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) <= 0;
        if (onBatteryNow) {
          InactivityTimer.this.onActivity();
        } else {
          InactivityTimer.this.cancel();
        }
      }
    }
  }

  private final class InactivityAsyncTask extends AsyncTask<Object,Object,Object> {
    @Override
    protected Object doInBackground(Object... objects) {
      try {
        Thread.sleep(INACTIVITY_DELAY_MS);
        Log.i(TAG, "Finishing activity due to inactivity");
        activity.finish();
      } catch (InterruptedException e) {
        // continue without killing
      }
      return null;
    }
  }

}
