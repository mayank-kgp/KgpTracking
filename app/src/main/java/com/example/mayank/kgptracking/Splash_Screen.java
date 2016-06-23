package com.example.mayank.kgptracking;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class Splash_Screen extends AppCompatActivity {
    private Receiver mReceive = new Receiver(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        final boolean[] isConnected = {Util.checkConnection(Splash_Screen.this)};
        if (isConnected[0]) {
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        //ParseUser user= ParseUser.getCurrentUser();

                        sleep(3000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {


                        SharedPreferences preferences = getSharedPreferences(Constants.LOGIN_FILE, Context.MODE_PRIVATE);
                        if (preferences.contains(Constants.isLogin) && preferences.getBoolean(Constants.isLogin, false)) {
                            MyIntentService.startGetBusData(Splash_Screen.this);
                            MyIntentService.startGetTrackData(Splash_Screen.this);
                            IntentFilter filter = new IntentFilter();
                            filter.addAction(MyIntentService.ACTION_GETBUSDATA);
                            filter.addAction(MyIntentService.ACTION_GETTRACKDATA);
                            LocalBroadcastManager.getInstance(Splash_Screen.this).
                                    registerReceiver(mReceive, filter);
                        } else {
                            Intent i = new Intent(Splash_Screen.this, Login.class);
                            startActivity(i);
                            finish();
                        }
                    }

                }
            };
            timerThread.start();
        } else {

            RelativeLayout coordinatorLayout = (RelativeLayout) findViewById(R.id
                    .coordinatorLayout);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No Network", Snackbar.LENGTH_INDEFINITE);


            snackbar.show();

            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        //ParseUser user= ParseUser.getCurrentUser();
                        while (isConnected[0] == false) {
                            sleep(1000);
                            if (Util.checkConnection(Splash_Screen.this) == true)
                                isConnected[0] = true;

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {


                        SharedPreferences preferences = getSharedPreferences(Constants.LOGIN_FILE, Context.MODE_PRIVATE);
                        if (preferences.contains(Constants.isLogin) && preferences.getBoolean(Constants.isLogin, false)) {
                            MyIntentService.startGetBusData(Splash_Screen.this);
                            MyIntentService.startGetTrackData(Splash_Screen.this);
                            IntentFilter filter = new IntentFilter();
                            filter.addAction(MyIntentService.ACTION_GETBUSDATA);
                            filter.addAction(MyIntentService.ACTION_GETTRACKDATA);
                            LocalBroadcastManager.getInstance(Splash_Screen.this).
                                    registerReceiver(mReceive, filter);
                        } else {
                            Intent i = new Intent(Splash_Screen.this, Login.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            };
            timerThread.start();

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