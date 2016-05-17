package com.example.mayank.kgptracking;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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

import java.util.ArrayList;

public class MainMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener
        ,ResultCallback<Status> {

    TextView may;
    GoogleMap m_map;
    boolean mapReady=false;
    MarkerOptions markbus1;
    MarkerOptions markbus2;
    MarkerOptions markbus3;
    MarkerOptions markbus4;
    MarkerOptions markbus5;
    MarkerOptions markbus6;
    MarkerOptions markbus7;
    MarkerOptions markbus8;
    Marker marker1;
    Marker marker2;
    Marker marker3;
    Marker marker4;
    Marker marker5;
    Marker marker6;
    Marker marker7;
    Marker marker8;
    Button bus1;
    Button bus2;
    Button bus3;
    Button bus4;
    Button bus5;
    Button bus6;
    Button bus7;
    Button bus8;
    ArrayList<LatLng> polylist1;
    ArrayList<LatLng> polylist2;
    ArrayList<LatLng> polylist3;
    ArrayList<LatLng> polylist4;
    ArrayList<LatLng> polylist5;
    ArrayList<LatLng> polylist6;
    ArrayList<LatLng> polylist7;
    ArrayList<LatLng> polylist8;
    PolylineOptions polylineOptions1;
    PolylineOptions polylineOptions2;
    PolylineOptions polylineOptions3;
    PolylineOptions polylineOptions4;
    PolylineOptions polylineOptions5;
    PolylineOptions polylineOptions6;
    PolylineOptions polylineOptions7;
    PolylineOptions polylineOptions8;
    Polyline polyline1;
    Polyline polyline2;
    Polyline polyline3;
    Polyline polyline4;
    Polyline polyline5;
    Polyline polyline6;
    Polyline polyline7;
    Polyline polyline8;


      CameraPosition currentloc;

    LatLng latlngbus1;
    LatLng latlngbus2;
    LatLng latlngbus3;
    LatLng latlngbus4;
    LatLng latlngbus5;
    LatLng latlngbus6;
    LatLng latlngbus7;
    LatLng latlngbus8;

    private final String LOG_TAG = "MayankApp";

    private GoogleApiClient mGoogleApiClient;

    ScrollView mScrollView;

    Polyline activepolyline;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        latlngbus1 = new LatLng(22.3216277,87.3009507);
        latlngbus2 = new LatLng(22.3087906,87.3040474);
        latlngbus3 = new LatLng(22.3094332,87.3084944);
        latlngbus4 = new LatLng(22.3177048,87.3070111);
        latlngbus5 = new LatLng(22.3203539,87.3026035);
        latlngbus6 = new LatLng(22.3169741,87.2936269);
        latlngbus7 = new LatLng(22.3213312,87.2973794);
        latlngbus8 = new LatLng(22.3199529,87.2973606);

        polylist1 = new ArrayList<LatLng>();
        polylist2 = new ArrayList<LatLng>();
        polylist3 = new ArrayList<LatLng>();
        polylist4 = new ArrayList<LatLng>();
        polylist5 = new ArrayList<LatLng>();
        polylist6 = new ArrayList<LatLng>();
        polylist7 = new ArrayList<LatLng>();
        polylist8 = new ArrayList<LatLng>();
        polylineOptions1 = new PolylineOptions();
        polylineOptions2 = new PolylineOptions();
        polylineOptions3 = new PolylineOptions();
        polylineOptions4 = new PolylineOptions();
        polylineOptions5 = new PolylineOptions();
        polylineOptions6 = new PolylineOptions();
        polylineOptions7 = new PolylineOptions();
        polylineOptions8 = new PolylineOptions();

        polylist1.add(0,new LatLng(22.32199,87.3063487));
        polylist1.add(1,new LatLng(22.3216885,87.3036128));
        polylist1.add(2,new LatLng(22.3186439,87.2962743));
        polylist2.add(0,new LatLng(22.3223386,87.3013665));
        polylist2.add(1,new LatLng(22.3167259,87.3146313));
        polylist1.add(2,new LatLng(22.3223634,87.3079043));

        polylineOptions1.addAll(polylist1).geodesic(true);
        polylineOptions2.addAll(polylist2);


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

        bus1 = (Button) findViewById(R.id.bus1);
        bus2 = (Button) findViewById(R.id.bus2);
        bus3 = (Button) findViewById(R.id.bus3);
        bus4 = (Button) findViewById(R.id.bus4);
        bus5 = (Button) findViewById(R.id.bus5);
        bus6 = (Button) findViewById(R.id.bus6);
        bus7 = (Button) findViewById(R.id.bus7);
        bus8 = (Button) findViewById(R.id.bus8);


        if(mapReady) {
            m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        markbus1 = new MarkerOptions()
                .position(latlngbus1)
                .title("Renton")
                .snippet("Bus1")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        markbus2 = new MarkerOptions()
                .position(latlngbus2)
                .title("Renton")
                .snippet("Bus2")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        markbus3 = new MarkerOptions()
                .position(latlngbus3)
                .title("Renton")
                .snippet("Bus3")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        markbus4 = new MarkerOptions()
                .position(latlngbus4)
                .title("Renton")
                .snippet("Bus4")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        markbus5 = new MarkerOptions()
                .position(latlngbus5)
                .title("Renton")
                .snippet("Bus5")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        markbus6 = new MarkerOptions()
                .position(latlngbus6)
                .title("Renton")
                .snippet("Bus6")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        markbus7 = new MarkerOptions()
                .position(latlngbus7)
                .title("Renton")
                .snippet("Bus7")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        markbus8 = new MarkerOptions()
                .position(latlngbus8)
                .title("Renton")
                .snippet("Bus8")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));

        bus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bus1.setFocusable(true);
                bus1.setBackgroundColor(Color.BLUE);

                if(marker1.isInfoWindowShown()){
                    marker1.hideInfoWindow();
                }
                else {
                    marker1.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
            activepolyline =  m_map.addPolyline(polylineOptions1);


            }
        });
        bus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    bus2.setFocusable(true);
                bus2.setBackgroundColor(Color.BLUE);

                if(marker2.isInfoWindowShown()){
                    marker2.hideInfoWindow();
                }
                else {
                    marker2.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
                activepolyline =  m_map.addPolyline(polylineOptions2);

            }
        });
        bus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bus3.setFocusable(true);
                bus3.setBackgroundColor(Color.BLUE);

                if(marker3.isInfoWindowShown()){
                    marker3.hideInfoWindow();
                }
                else {
                    marker3.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
                activepolyline =  m_map.addPolyline(polylineOptions3);
            }
        });
        bus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bus4.setFocusable(true);

                if(marker4.isInfoWindowShown()){
                    marker4.hideInfoWindow();
                }
                else {
                    marker4.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
                activepolyline =  m_map.addPolyline(polylineOptions4);
            }
        });
        bus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bus5.setFocusable(true);

                if(marker5.isInfoWindowShown()){
                    marker5.hideInfoWindow();
                }
                else {
                    marker5.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
                activepolyline =  m_map.addPolyline(polylineOptions5);
            }
        });
        bus6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bus6.setFocusable(true);
                if(marker6.isInfoWindowShown()){
                    marker6.hideInfoWindow();
                }
                else {
                    marker6.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
                activepolyline =  m_map.addPolyline(polylineOptions6);
            }
        });
        bus7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bus7.setFocusable(true);
                if(marker7.isInfoWindowShown()){
                    marker7.hideInfoWindow();
                }
                else {
                    marker7.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
                activepolyline =  m_map.addPolyline(polylineOptions7);
            }
        });
        bus8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // bus8.setFocusable(true);
                if(marker7.isInfoWindowShown()){
                    marker7.hideInfoWindow();
                }
                else {
                    marker7.showInfoWindow();
                }
                if(activepolyline!=null) {
                    activepolyline.remove();
                }
                activepolyline =  m_map.addPolyline(polylineOptions8);
            }
        });



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
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleApiClient connection has failed" + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("mapreadyfunction","called");
        mapReady=true;
        m_map = googleMap;
        currentloc = CameraPosition.builder()
                .target(new LatLng(22.3216178,87.3009507))
                .zoom(17)
                .bearing(0)
             //   .tilt(45)
                .build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(currentloc));
    marker1 =  m_map.addMarker(markbus1);
        marker2=     m_map.addMarker(markbus2);
        marker3 = m_map.addMarker(markbus3);
        marker4 =  m_map.addMarker(markbus4);
        marker5 = m_map.addMarker(markbus5);
        marker6 = m_map.addMarker(markbus6);
        marker7 =  m_map.addMarker(markbus7);
        marker8 = m_map.addMarker(markbus8);
    }

    @Override
    public void onResult(@NonNull Status status) {

    }
}
