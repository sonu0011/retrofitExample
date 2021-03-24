package com.sonu.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import static com.sonu.retrofitexample.MyHandler.TASK_A;
import static com.sonu.retrofitexample.MyHandler.TASK_B;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG = "ThreadActivity";
    private MyThread thread = new MyThread();
    Button button;
    private volatile boolean isRunning = false;
    Handler handler = new Handler();
    private ProgressBar progressBar;
    private Object token = new Object();

    MyLooperThread myLooperThread = new MyLooperThread();
    private MyHandlerThread handlerThread = new MyHandlerThread();
    private Myrunnable1 runnable = new Myrunnable1();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        button = findViewById(R.id.btn);
        handlerThread.start();
        progressBar = findViewById(R.id.progress_bar);
//        handler = new Handler(handlerThread.getLooper());

    }


    public void startThread(View view) {
        myLooperThread.start();
    }

    public void stopThread(View view) {
        myLooperThread.looper.quit();
    }

    public void taskA(View view) {

        Message message = Message.obtain();
        message.what = TASK_A;
        myLooperThread.handler.sendMessage(message);

//        Handler handler = new Handler(myLooperThread.looper);
//        handler.post(() -> {
//            for (int i = 0; i < 10; i++) {
//                Log.d(TAG, "run: " + i);
//                SystemClock.sleep(1000);
//            }
//        });
    }

    public void taskB(View view) {

        Message message = Message.obtain();
        message.what = TASK_B;
        myLooperThread.handler.sendMessage(message);
    }

    public void doWork(View view) {
//        handler.postDelayed(new Myrunnable1(), 2000);

        //reqeuires api 28
//        handlerThread.getHandler().postDelayed(runnable, token, System.currentTimeMillis());
        handlerThread.getHandler().post(runnable);
//        handlerThread.getHandler().postAtFrontOfQueue(new Myrunnable2());
        Message message = Message.obtain();
        message.what = 1;

        handlerThread.getHandler().sendMessage(message);


        Message message1 = Message.obtain(handlerThread.getHandler());
        message.what = 2;
        message1.sendToTarget();

    }

    public void removeMessage(View view) {
        handlerThread.getHandler().removeCallbacks(runnable);
    }

    public void asnyckTask(View view) {
        new MyAsynckTask(this).execute(10);
    }


    private static class MyThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Log.d(TAG, "run: " + i);
                SystemClock.sleep(1000);
            }
        }
    }

    private class Myrunnable implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (isRunning) return;
                if (i == 5) {
                    Handler bgLooper = new Handler(Looper.getMainLooper());
                    bgLooper.post(() -> {
                        button.setText("50% completed");

                    });

//                    button.post(() -> {
//                        button.setText("50% completed");
//
//                    });
//                    runOnUiThread(() -> {
//                        button.setText("50% completed");
//                    });
                }
                Log.d(TAG, "run: " + i);
                SystemClock.sleep(1000);
            }
        }
    }

    public void start(View view) {

        isRunning = false;

//        thread.start();
        Myrunnable myrunnable = new Myrunnable();
        new Thread(myrunnable).start();
    }

    public void stop(View view) {
        isRunning = true;
    }

    private static class Myrunnable1 implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                Log.d(TAG, "Myrunnable1: " + i);
                SystemClock.sleep(1000);
            }
        }
    }

    private static class Myrunnable2 implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Log.d(TAG, "Myrunnable2: " + i);
                SystemClock.sleep(1000);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();

    }

    private static class MyAsynckTask extends AsyncTask<Integer, Integer, String> {

        WeakReference<ThreadActivity> activityWeakReference;

        MyAsynckTask(ThreadActivity activity) {

            activityWeakReference = new WeakReference<ThreadActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ThreadActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ThreadActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {
                return;
            }

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ThreadActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.progressBar.setProgress(values[0]);


        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Work finished!";
        }
    }
}