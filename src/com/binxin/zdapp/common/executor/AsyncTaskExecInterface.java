package com.binxin.zdapp.common.executor;

import android.os.AsyncTask;

public interface AsyncTaskExecInterface {

  <T> void execute(AsyncTask<T,?,?> task, T... args);

}
