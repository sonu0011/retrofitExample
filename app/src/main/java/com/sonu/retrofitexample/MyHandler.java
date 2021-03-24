package com.sonu.retrofitexample;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyHandler extends Handler {
    public static final int TASK_A = 1;
    public static final int TASK_B = 2;
    private static final String TAG = "MyHandler";
     @Override
    public void handleMessage(@NonNull Message msg) {

        switch (msg.what){
            case TASK_A  :
                Log.d(TAG, "handleMessage: task A");
                break;
            case  TASK_B :
                Log.d(TAG, "handleMessage: task b");
                break;
        }
    }
}
