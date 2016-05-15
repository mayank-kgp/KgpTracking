package com.example.mayank.kgptracking;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        OnStreetViewPanoramaReadyCallback, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener
,ResultCallback<Status>{
//    GoogleMap m_map;
//    boolean mapReady=false;

//    MarkerOptions renton;
//
//    MarkerOptions kirkland;
//
//    MarkerOptions everett;
//
//    MarkerOptions lynnwood;
//
//    MarkerOptions montlake;
//
//    MarkerOptions kent;
//
//    MarkerOptions showare;


//      CameraPosition NEWYORK = CameraPosition.builder()
//            .target(new LatLng(40.784,-73.9857))
//            .zoom(21)
//            .bearing(0)
//            .tilt(45)
//            .build();


//    static final CameraPosition DUBLIN = CameraPosition.builder()
//            .target(new LatLng(53.3478,-6.2597))
//            .zoom(17)
//            .bearing(90)
//            .tilt(45)
//            .build();
//
//
//    static final CameraPosition TOKYO = CameraPosition.builder()
//            .target(new LatLng(35.6895,139.6917))
//            .zoom(17)
//            .bearing(90)
//            .tilt(45)
//            .build();

//    LatLng renton=new LatLng(47.489805, -122.120502);
//    LatLng kirkland=new LatLng(47.7301986, -122.1768858);
//    LatLng everett=new LatLng(47.978748,-122.202001);
//    LatLng lynnwood=new LatLng(47.819533,-122.32288);
//    LatLng montlake=new LatLng(47.7973733,-122.3281771);
//    LatLng kent=new LatLng(47.385938,-122.258212);
//    LatLng showare=new LatLng(47.38702,-122.23986);
//    static final CameraPosition SEATTLE = CameraPosition.builder()
//            .target(new LatLng(47.6204,-122.2491))
//            .zoom(10)
//            .bearing(0)
//            .tilt(45)
//            .build();
//    StreetViewPanorama panorama;

    private final String LOG_TAG = "LaurenceTestApp";
    private TextView txtOutput;
    private TextView updatetime;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    protected Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    private Button mRequestActivityUpdatesButton;
    private Button mRemoveActivityUpdatesButton;
  private TextView mStatusText;
    protected ActivityDetectionBroadcastReceiver mBroadcastReciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        Button btnMap = (Button) findViewById(R.id.btnMap);
//        if (btnMap != null) {
//            btnMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mapReady)
//                        m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                }
//            });
//        }
//
//        Button btnSatellite = (Button) findViewById(R.id.btnSatellite);
//        if (btnSatellite != null) {
//            btnSatellite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mapReady)
//                        m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                }
//            });
//        }
//
//        Button btnHybrid = (Button) findViewById(R.id.btnHybrid);
//        if (btnHybrid != null) {
//            btnHybrid.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mapReady)
//                        m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                }
//            });
//        }

//        Button btnSeattle = (Button) findViewById(R.id.btnSeattle);
//        if (btnSeattle != null) {
//            btnSeattle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mapReady)
//                        flyTo(SEATTLE);
//                }
//            });
//        }
//
//        Button btnDublin = (Button) findViewById(R.id.btnDublin);
//        if (btnDublin != null) {
//            btnDublin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mapReady)
//                        flyTo(DUBLIN);
//                }
//            });
//        }
//
//        Button btnTokyo = (Button) findViewById(R.id.btnTokyo);
//        if (btnTokyo != null) {
//            btnTokyo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mapReady)
//                        flyTo(TOKYO);
//                }
//            });
//        }
//        renton = new MarkerOptions()
//                .position(new LatLng(47.489805, -122.120502))
//                .title("Renton")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//
//        kirkland = new MarkerOptions()
//                .position(new LatLng(47.7301986, -122.1768858))
//                .title("Kirkland")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//
//        everett = new MarkerOptions()
//                .position(new LatLng(47.978748,-122.202001))
//                .title("Everett")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//
//        lynnwood = new MarkerOptions()
//                .position(new LatLng(47.819533,-122.32288))
//                .title("Lynnwood")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//
//        montlake = new MarkerOptions()
//                .position(new LatLng(47.7973733,-122.3281771))
//                .title("Montlake Terrace")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//
//        kent = new MarkerOptions()
//                .position(new LatLng(47.385938,-122.258212))
//                .title("Kent Valley")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//
//        showare = new MarkerOptions()
//                .position(new LatLng(47.38702,-122.23986))
//                .title("Showare Center")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));


//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

//        StreetViewPanoramaFragment streetViewPanoramaFragment =
//                (StreetViewPanoramaFragment) getFragmentManager()
//                        .findFragmentById(R.id.streetviewpanorama);
//        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
//        txtOutput = (TextView) findViewById(R.id.txtOutput);
//        updatetime = (TextView) findViewById(R.id.updatetime);
//        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
//        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        mStatusText = (TextView) findViewById(R.id.detectedActivities);
        mBroadcastReciever = new ActivityDetectionBroadcastReceiver();

        mRequestActivityUpdatesButton = (Button) findViewById(R.id.request_activity_updates_button);
        mRemoveActivityUpdatesButton = (Button) findViewById(R.id.remove_activity_updates_button);
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
           //     .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mapReady=true;
//        m_map = googleMap;
////        LatLng newYork = new LatLng(40.7484,-73.9857);
////        CameraPosition target = CameraPosition.builder().target(newYork).zoom(14).build();
//        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(SEATTLE));
//   //     m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//      //  flyTo(NEWYORK);
////        m_map.addMarker(renton);
////        m_map.addMarker(kirkland);
////        m_map.addMarker(everett);
////        m_map.addMarker(lynnwood);
////        m_map.addMarker(montlake);
////        m_map.addMarker(kent);
////        m_map.addMarker(showare);
////        flyTo(SEATTLE);
//      //  m_map.addPolygon(new PolygonOptions().geodesic(true).add(renton).add(kirkland).add(everett).add(lynnwood).add(montlake).add(kent).add(showare).add(renton));
//        m_map.addCircle(new CircleOptions()
//                .center(renton)
//                .radius(5000)
//                .strokeColor(Color.GREEN)
//                .fillColor(Color.argb(64,0,255,0)));
//
//    }
//    private void flyTo(CameraPosition target)
//    {
//        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000,   new GoogleMap.CancelableCallback() {
//            @Override
//            public void onFinish() {
//                Toast.makeText(getBaseContext(), "Animation to Sydney complete",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(getBaseContext(), "Animation to Sydney canceled",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
//        panorama = streetViewPanorama;
//        panorama.setPosition(new LatLng(36.0579667,-122.1430996));
//        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
//                .bearing(180)
//                .build();
//        panorama.animateTo(camera,10000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReciever);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReciever,new IntentFilter(Constants.BROADCAST_ACTION));
        super.onResume();
    }

    public void requestActivityUpdatesButtonHandeler(View view){
        if(!mGoogleApiClient.isConnected()){
            Toast.makeText(this,getString(R.string.not_connected),Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient,
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,getActivityDetectionPendingIntent()).setResultCallback(this);
        mRequestActivityUpdatesButton.setEnabled(false);
        mRemoveActivityUpdatesButton.setEnabled(true);
    }
    public void removeActivityUpdatesButtonHandeler(View view){
        if(!mGoogleApiClient.isConnected()){
            Toast.makeText(this,getString(R.string.not_connected),Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient
                ,getActivityDetectionPendingIntent()).setResultCallback(this);
        mRequestActivityUpdatesButton.setEnabled(true);
        mRemoveActivityUpdatesButton.setEnabled(false);
    }

    private PendingIntent getActivityDetectionPendingIntent(){
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(7000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
           //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
          // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
//       mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleApiClient connection has failed" + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, location.toString());
        //txtOutput.setText(location.toString());
        updatetime.setText(DateFormat.getTimeInstance().format(new Date()));
        txtOutput.setText(String.valueOf(location.getLatitude()));
    }

    public String getActivityString(int detectedActivityType){
        Resources resources = this.getResources();
        switch (detectedActivityType){
            case DetectedActivity.IN_VEHICLE:
                return  resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return  resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return  resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return  resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return  resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return  resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return  resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return  resources.getString(R.string.walking);
            default:
                return  resources.getString(R.string.unidentifiable_activity);

        }
    }

    @Override
    public void onResult(@NonNull Status status) {
        if(status.isSuccess()){
            Log.e("mayank","Successfully added activity detection");
        }
        else {
            Log.e("mayank","Error in adding or removing activity detection");
        }
    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver{
        protected static final String TAG = "receiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> updateActivities = intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
            String strStatus = "";
            for (DetectedActivity thisActivity : updateActivities){
                strStatus += getActivityString(thisActivity.getType())+thisActivity.getConfidence()+"%\n";

            }
            mStatusText.setText(strStatus);
        }

    }

}
