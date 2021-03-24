package com.sonu.retrofitexample;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyHandlerThread extends HandlerThread {
    private Handler handler;
    private static final String TAG = "MyHandlerThread";
    public MyHandlerThread() {
        super("logging" , Process.THREAD_PRIORITY_BACKGROUND);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {

                for (int i = 0; i < 5; i++) {
                Log.d(TAG, "run handleMessage: " + i);
                SystemClock.sleep(1000);
            }
            }
        };
    }

    public Handler getHandler() {
        return handler;
    }
}
