package com.example.mayank.kgptracking;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class Bus implements RoutingListener {
    private String mBusName;
    private String mBusNumber;
    private String mBusRoute;
    private LatLng mLocation;
    private Double mLat;
    private Double mLng;
    private Button mBusButton;
    private Marker mMarker;
    private MarkerOptions mMarkerOptions;
    private Context mContext;
    private boolean mActive = false;
    public Bus(Context context,String busRoute,String busNumber,String busName,Double Lat, Double Lng){
        mLat = Lat;
        mLng = Lng;
        mLocation = new LatLng(mLat,mLng);
        mBusName = busName;
        mBusNumber = busNumber;
        mBusRoute = busRoute;
        mContext = context;
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        //View rootView = super.findViewById(android.R.id.content);
        LinearLayout lv = (LinearLayout) rootView.findViewById(R.id.horizontalLinear);

        Button bv = new Button(context);
        bv.setText(mBusName);
       // String bus_id = "Bus_"+(mBusCount+1);
        bv.setId(MainMap.mBusCount);
        bv.setBackgroundColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bv.setTextAppearance(R.style.BusButton);
        }
        lv.addView(bv);
        //((Activity)context
        //((Activity)context).setContentView(rootView);
        mBusButton = bv;
        mBusButton.setOnClickListener((View.OnClickListener) context); // Ask Mayank set onClickListener to the context of MainMap
        mMarkerOptions = new MarkerOptions()
                            .position(mLocation)
                            .title(mBusName)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        //mMarker = m_map.addMarker(mMarkerOptions);
        MainMap.mBusCount++;
        Log.d("Inside Construct",MainMap.mBusCount+"");
    }


    public Marker getMarker(){
        return mMarker;
    }
    public void setMarker(Marker marker){
        mMarker = marker;
    }
    public MarkerOptions getMarkerOptions(){
        return mMarkerOptions;
    }
    public void setBusPosition(Double Lat,Double Lng){
        mMarker.setPosition(new LatLng(Lat,Lng));
    }
    public boolean isActive(){
        return mActive;
    }
    public void setBusInFocus(/*Bus activeBus*/) {
       /* if(activeBus != null){
            activeBus.setBusInOrOutFocus(null);
        }*/

        /*if(isActive()){
            mBusButton.setBackgroundColor(Color.WHITE);
            mMarker.hideInfoWindow();
            mActive = false;
            returnaldi bhrne se kya lena dena tuhe null;
        }*/
        /*else*/{
            mBusButton.setBackgroundColor(Color.BLUE);
            mMarker.showInfoWindow();
            MainMap.mActiveBus = this;
            mActive = true;
            if(MainMap.mActivePolyline != null){
                MainMap.mActivePolyline.remove();
                MainMap.mActivePolyline = null;
            }
            if(MainMap.mUserLocation != null)
            findRouteFromPosition(MainMap.mUserLocation.latitude ,MainMap.mUserLocation.longitude);
            else{
               Log.d("MayankApp","user location is null");
            }
            /*return this;*/
        }
    }
    public void setBusOutFocus(/*Bus activeBus*/) {
       /* if(activeBus != null){
            activeBus.setBusInOrOutFocus(null);
        }*/

        /*if(isActive())*/{
            mBusButton.setBackgroundColor(Color.WHITE);
            mMarker.hideInfoWindow();
            MainMap.mActiveBus = null;
            mActive = false;
            if(MainMap.mActivePolyline != null){
            MainMap.mActivePolyline.remove();
            MainMap.mActivePolyline = null;
            }
            /*return null;*/
        }
        /*else*//*{
            mBusButton.setBackgroundColor(Color.BLUE);
            mMarker.showInfoWindow();
            mActive = true;
            *//*return this;*//*
        }*/
    }
    public void findRouteFromPosition(Double Lat, Double Lng){
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(mLocation /* start */,new LatLng(Lat,Lng) /* end */)
                .build();
        routing.execute();
    }
    public void showRouteOnMap(ArrayList<Route> routes, int shortestRouteIndex){
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(mContext.getResources().getColor(R.color.blue));
        polyOptions.width(10);
        polyOptions.addAll(routes.get(shortestRouteIndex).getPoints());
        if(MainMap.mActivePolyline != null){
            MainMap.mActivePolyline.remove();
            MainMap.mActivePolyline = null;
        }
        MainMap.mActivePolyline = MainMap.m_map.addPolyline(polyOptions);
    }
    @Override
    public void onRoutingFailure(RouteException e) {
        //Toast.makeText(this,"Couldn't fecth the route :(",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int shortestRouteIndex) {
        showRouteOnMap(arrayList,shortestRouteIndex);
    }

    @Override
    public void onRoutingCancelled() {

    }
}
