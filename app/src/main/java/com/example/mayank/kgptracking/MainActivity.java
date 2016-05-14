package com.example.mayank.kgptracking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {
    GoogleMap m_map;
    boolean mapReady=false;

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

    LatLng renton=new LatLng(47.489805, -122.120502);
    LatLng kirkland=new LatLng(47.7301986, -122.1768858);
    LatLng everett=new LatLng(47.978748,-122.202001);
    LatLng lynnwood=new LatLng(47.819533,-122.32288);
    LatLng montlake=new LatLng(47.7973733,-122.3281771);
    LatLng kent=new LatLng(47.385938,-122.258212);
    LatLng showare=new LatLng(47.38702,-122.23986);
    static final CameraPosition SEATTLE = CameraPosition.builder()
            .target(new LatLng(47.6204,-122.2491))
            .zoom(10)
            .bearing(0)
            .tilt(45)
            .build();
    StreetViewPanorama panorama;

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

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
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
        panorama = streetViewPanorama;
        panorama.setPosition(new LatLng(36.0579667,-122.1430996));
        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                .bearing(180)
                .build();
        panorama.animateTo(camera,10000);
    }
}
