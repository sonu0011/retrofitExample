package com.sonu.retrofitexample;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

public class MyLooperThread extends Thread {
    private static final String TAG = "MyLooperThread";
    public Handler handler;
    public Looper looper;
    @Override
    public void run() {

        Looper.prepare();
        looper = Looper.myLooper();
//        handler = new Handler();
        handler = new MyHandler();
        Looper.loop();

        Log.d(TAG, "run: end run()");
    }
}
