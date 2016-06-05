package com.example.mayank.kgptracking;
// added mBusCount,mActiveBus and mActivePolyline : Sahil

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Double.parseDouble;

public class MainMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
        , ResultCallback<Status>, View.OnClickListener {

    TextView may;
    public static int mBusCount = 0; // Sahil
    public static Polyline mActivePolyline = null; // Sahil
    public static Bus mActiveBus = null; // Sahil
    public static GoogleMap m_map;
    public static LatLng mUserLocation = null;
    boolean mapReady = false;
    CameraPosition currentloc;

    private final String LOG_TAG = "MayankApp";


    ScrollView mScrollView;

    ArrayList<Bus> mBuses = new ArrayList<Bus>();

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getIntent().getStringExtra()
        HashMap<String,String> data;
        data = (HashMap<String,String>) getIntent().getSerializableExtra("DATA");
        JSONArray Busdata = null;
        JSONArray Trackdata = null;
        try {
            Busdata = new JSONArray(data.get("Busdata"));
            Trackdata = new JSONArray(data.get("Trackdata"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i  = 0; i< Busdata.length() ;i++){
            try {
                JSONObject bus = Busdata.getJSONObject(i);
                JSONObject track = Trackdata.getJSONObject(i);
                mBuses.add(new Bus(this,bus.getString(Constants.RESPONSE_BUSROUTE),
                        bus.getString(Constants.RESPONSE_BUSCODE),bus.getString(Constants.RESPONSE_BUSNUMBER),
                        bus.getString(Constants.RESPONSE_BUSNAME),
                        parseDouble(track.getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0).getString(Constants.RESPONSE_LAT)),
                        parseDouble(track.getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0).getString(Constants.RESPONSE_LON))
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        may = (TextView) findViewById(R.id.may);
        may.setSelected(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        if (m_map == null) {
             m_map = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            m_map.getUiSettings().setZoomControlsEnabled(true);
            mScrollView = (ScrollView) findViewById(R.id.scrollView);
            //parent scrollview in xml, give your scrollview id value

            ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .setListener(new CustomMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });

        }


        if (mapReady) {
            m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        buildGoogleApiClient();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //     getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        client.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                //.addApi(ActivityRecognition.API)
                .build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG,"Connected");
      /*  getUserLocation();*/
    }

   /* public LatLng getUserLocation() {
        if (client.isConnected()) {
            Log.d(LOG_TAG,"Connected getUserLocation");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                mUserLocation = null;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(client);
            mUserLocation = new LatLng(location.getLatitude(), location.getLatitude());
            Log.d(LOG_TAG,""+mUserLocation.latitude);
            return mUserLocation;
        } else {
            Log.d(LOG_TAG,"Not connected");
            return null;
        }
    }
*/
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");
        Toast.makeText(this, "GoogleApiClient connection has been suspend", Toast.LENGTH_LONG);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleApiClient connection has failed" + connectionResult.getErrorCode());
        Toast.makeText(this, "GoogleApiClient connection has been Failed", Toast.LENGTH_LONG);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;
        currentloc = CameraPosition.builder()
                .target(new LatLng(22.3216178, 87.3009507))
                .zoom(17)
                .bearing(0)
                //   .tilt(45)
                .build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(currentloc));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        m_map.setMyLocationEnabled(true);

        m_map.getUiSettings().setMyLocationButtonEnabled(true);
        Log.d("mapreadyfunction", mBusCount + "");
        for (int i = 0; i < mBusCount; i++) {
            Log.d("mapreadyfunction", "calledhere");
            mBuses.get(i).setMarker(m_map.addMarker(mBuses.get(i).getMarkerOptions()));
        }
        m_map.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mBusCount; i++) {
            if (v.getId() == i) {
               /* Test test = new Test();
                test.execute();*/
                if (mActiveBus == null)
                    mBuses.get(i).setBusInFocus();
                else if (mActiveBus.equals(mBuses.get(i))) {
                    mActiveBus.setBusOutFocus();
                }
                else {
                    mActiveBus.setBusOutFocus();
                    mBuses.get(i).setBusInFocus();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.mayank.kgptracking/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.mayank.kgptracking/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Log.d(LOG_TAG,"Location chagned called");
            mUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(Constants.LOG_TAG,"onDestroy");
        mBusCount = 0;
        mActivePolyline = null; // Sahil
        mActiveBus = null; // Sahil
        mUserLocation = null;
        mBuses = null;
        m_map = null;
    }
}
