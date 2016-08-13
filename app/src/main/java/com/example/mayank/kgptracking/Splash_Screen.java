package com.example.mayank.kgptracking;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Splash_Screen extends AppCompatActivity {

    boolean gps = false;
    private Receiver mReceive = new Receiver(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        Log.d("Splash","sdadsa");
        //ActionBar actionBar = getActionBar();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    69);
        }
//        if (actionBar != null) {
//            actionBar.hide();
//        }
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    }

    @Override
    protected void onResume() {
        gpscheck();
        Log.d("mayank123",""+gps);
        if(gps) {
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
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
        super.onResume();
    }



    private void gpscheck(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        else{
            gps = true;
        }

    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        builder.show();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

            @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceive);
        super.onDestroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 69: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(this,"App needs to access user location for better experience",Toast.LENGTH_LONG);
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMDM0NDQ0ODI4OTMyNzMwNjk2NjUiLCJlbWFpbCI6InNhaGlsdnMwMDBAZ21haWwuY29tIiwibmFtZSI6InNhaGlsIGNoYWRkaGEiLCJleHAiOjE0Nzk1NTk2NjYuNDM2OX0.DYn0Cu8Fe4fYizLBns_0owR0GEPo5n4xqSoEOyX7JpA