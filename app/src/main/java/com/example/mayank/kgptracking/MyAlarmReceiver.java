package com.example.mayank.kgptracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by user on 6/14/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent i = new Intent(context, MyIntentService.class);
//        i.putExtra("foo", "bar");
//        context.startService(i);
        MyIntentService.startGetTrackData(context);
        Log.d("abcd","onrecieve called");
    }
}
