package com.example.mayank.kgptracking;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity {
    private Receiver mReceive = new Receiver(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        SharedPreferences preferences = getSharedPreferences(Constants.LOGIN_FILE, Context.MODE_PRIVATE);
        if(preferences.contains(Constants.isLogin) && preferences.getBoolean(Constants.isLogin,false)) {
            MyIntentService.startGetBusData(this);
            MyIntentService.startGetTrackData(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(MyIntentService.ACTION_GETBUSDATA);
            filter.addAction(MyIntentService.ACTION_GETTRACKDATA);
            LocalBroadcastManager.getInstance(this).
                    registerReceiver(mReceive, filter);
        }
        else{
            Intent i = new Intent(this,Login.class);
            startActivity(i);
            finish();
        }

    }
    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceive);
        super.onDestroy();
    }

}
//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMDM0NDQ0ODI4OTMyNzMwNjk2NjUiLCJlbWFpbCI6InNhaGlsdnMwMDBAZ21haWwuY29tIiwibmFtZSI6InNhaGlsIGNoYWRkaGEiLCJleHAiOjE0Nzk1NTk2NjYuNDM2OX0.DYn0Cu8Fe4fYizLBns_0owR0GEPo5n4xqSoEOyX7JpA