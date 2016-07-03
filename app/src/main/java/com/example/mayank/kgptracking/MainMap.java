package com.example.mayank.kgptracking;
// added mBusCount,mActiveBus and mActivePolyline : Sahil

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

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
    private Receiver receive;
    NavigationView navigationView;

    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private ArrayList<MyMarker> mMyMarkersArray;
    public static LatLng mBusStop = null;
    public static String mLoc = null; // user location got from Bus Stop
    public static Marker mBusStopMarker = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getIntent().getStringExtra()

        mMarkersHashMap = new HashMap<Marker, MyMarker>();
        mMyMarkersArray = new ArrayList<MyMarker>();

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
                        parseDouble (track.getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0).getString(Constants.RESPONSE_LAT)),
                        parseDouble(track.getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0).getString(Constants.RESPONSE_LON))
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
//        may = (TextView) findViewById(R.id.may);
//        may.setSelected(true);

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
        View v = navigationView.inflateHeaderView(R.layout.nav_header_main_map);
        SharedPreferences prefs = getSharedPreferences(Constants.LOGIN_FILE,Context.MODE_PRIVATE);
        String username = prefs.getString(Constants.USERNAME, null);
        String useremail = prefs.getString(Constants.EMAIL, null);
        String picurl = prefs.getString(Constants.PIC_URL, null);
        Log.d("mayank123",username);
        if(username!=null){
            TextView usernametext =  (TextView) v.findViewById(R.id.username);
            if (usernametext != null) {
                usernametext.setText(username);
            }
            else{
                Log.d("mayank123","null1");
            }
        }
        if(useremail!=null){
            TextView useremailtext =  (TextView) v.findViewById(R.id.useremail);
            if (useremailtext != null) {
                useremailtext.setText(useremail);
            }
            else{
                Log.d("mayank123","null1");
            }
        }
        if(picurl!=null){
            ImageView userimage =  (ImageView) v.findViewById(R.id.imageuser);
            if (userimage != null) {
                Glide.with(this).load(picurl).into(userimage);
            }
            else{
                Log.d("mayank123","null1");
            }
        }
        View navheaderView = navigationView.getHeaderView(0);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        client.connect();
        MyIntentService.startLoopTrackData(this);
        receive = new Receiver();
        IntentFilter filter = new IntentFilter(MyIntentService.ACTION_GETTRACKDATA);
        filter.addAction(MyIntentService.ACTION_GETBUSSTOP);
        LocalBroadcastManager.getInstance(this).registerReceiver(receive,filter);
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
        if(id == R.id.action_logout){
            getSharedPreferences(Constants.LOGIN_FILE, Context.MODE_PRIVATE).edit().clear().apply();
        }

        if (id == R.id.action_logout) {

            Intent i = new Intent(this,Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            MyIntentService.loop = false;
            getSharedPreferences(Constants.LOGIN_FILE, Context.MODE_PRIVATE).edit().clear().commit();
            startActivity(i);
            finish();
        }
// else if (id == R.id.nav_gallery) {
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
        m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        currentloc = CameraPosition.builder()
                .target(new LatLng(22.3216178, 87.3009507))
                .zoom(17)
                .bearing(70)
                .tilt(25)
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
//            final int y = i;
            Log.d("mapreadyfunction", "calledhere");
            mBuses.get(i).setMarker(m_map.addMarker(mBuses.get(i).getMarkerOptions()));
            Log.d("mayank123", "Bus code" + mBuses.get(i).getBusCode());


            mMarkersHashMap.put(mBuses.get(i).getMarker(), new MyMarker(mBuses.get(i)));


            m_map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {

                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.windowlayout, null);

                    // Getting the position from the marker
                    LatLng latLng = arg0.getPosition();

                    if (mMarkersHashMap.get(arg0)!= null) {

                        MyMarker mar = mMarkersHashMap.get(arg0);
                        Log.d("mayank123", mar.getBus().getBusCode());
                        Log.d("mayank123", "notnullbus");

                        Bus mybus = mMarkersHashMap.get(arg0).getBus();
                        TextView busName = (TextView) v.findViewById(R.id.busName);
                        TextView route = (TextView) v.findViewById(R.id.route);
                        TextView busNumber = (TextView) v.findViewById(R.id.busNumber);
                        TextView busDistance = (TextView) v.findViewById(R.id.busDistance);

                        busName.setText(mybus.getmBusName());
                        route.setText(mybus.getmBusRoute());
                        busNumber.setText(mybus.getmBusNumber());

                    } else {
                        TextView busName = (TextView) v.findViewById(R.id.busName);
                        TextView route = (TextView) v.findViewById(R.id.route);
                        TextView busNumber = (TextView) v.findViewById(R.id.busNumber);
                        TextView busDistance = (TextView) v.findViewById(R.id.busDistance);

                        busName.setText("Bus Stop - " + MainMap.mLoc);
                        route.setVisibility(View.GONE);
                        busNumber.setVisibility(View.GONE);
                        busDistance.setVisibility(View.GONE);
                        Log.d("mayank123", "nullbus");
                    }
                    // Getting reference to the TextView to set latitude

                    // Getting reference to the TextView to set longitude


                    // Setting the latitude


                    // Setting the longitude


                    // Returning the view containing InfoWindow contents
                    return v;

                }
            });

            m_map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    if (!arg0.isInfoWindowShown()) {
                        arg0.showInfoWindow();
                    } else {
                        arg0.hideInfoWindow();
                    }


                    return true;

                }
            });

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
        mBusStop = null;
        mLoc = null; // user location got from Bus Stop
        mBusStopMarker = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receive);
    }
    private class Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyIntentService.ACTION_GETTRACKDATA)) {
                try {
                    JSONObject response = new JSONObject(intent.getStringExtra(Constants.INTENT_RESPONSE));
                    if (response.getBoolean(Constants.RESPONSE_STATUS)) {
                        JSONArray data = response.getJSONArray(Constants.RESPONSE_DATA);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject track = data.getJSONObject(i);
                            for (int j = 0; j < mBuses.size(); j++) {
                                if (mBuses.get(j).getBusCode().equals(track.getString(Constants.RESPONSE_BUSCODE))) {
                                    mBuses.get(i).setBusPosition(parseDouble(track.getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0).getString(Constants.RESPONSE_LAT)),
                                            parseDouble(track.getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0).getString(Constants.RESPONSE_LON)));
                                }
                            }
                        }
                        if(mActiveBus != null && mBusStop != null){
                            mActiveBus.findRouteFromPosition(mBusStop.latitude,mBusStop.longitude);
                        }
                        Log.d("update", "Updated");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(intent.getAction().equals(MyIntentService.ACTION_GETBUSSTOP)){
                try {
                    Log.d(LOG_TAG,"RECEIVED");
                    JSONObject response = new JSONObject(intent.getStringExtra(Constants.INTENT_RESPONSE));
                    if(response.getBoolean(Constants.RESPONSE_STATUS)){
                        JSONObject data = response.getJSONObject(Constants.RESPONSE_DATA);
                        if(!data.getString(Constants.RESPONSE_LOC).equals("Rest")) {
                            mLoc = data.getString(Constants.RESPONSE_LOC);
                            mBusStop = new LatLng(data.getJSONObject(Constants.RESPONSE_BUSSTOP).getDouble(Constants.RESPONSE_LAT),
                                    data.getJSONObject(Constants.RESPONSE_BUSSTOP).getDouble(Constants.RESPONSE_LON)
                            );
                            if (mActiveBus != null) {
                                mActiveBus.findRouteFromPosition(mBusStop.latitude, mBusStop.longitude);
                            }
                        }
                        else if(mActiveBus != null) {
                            mActiveBus.findRouteFromPosition(null, null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
