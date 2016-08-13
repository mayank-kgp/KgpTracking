package com.example.mayank.kgptracking;


import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private MarkerOptions mDirectionOptions;
    private Marker mDirectionMarker;
    private Context mContext;
    private String mBusCode;
    private List<LatLng> mRoute;
    private Double mCOG;
    private boolean mActive = false;
    static boolean buttonclicke = false;



    public Bus(Context context, String busRoute,String route,double cog, String buscode, String busNumber, String busName, Double Lat, Double Lng){
        mLat = Lat;
        mLng = Lng;
        mBusCode = buscode;
        mLocation = new LatLng(mLat,mLng);
        mBusName = busName;
        mBusNumber = busNumber;
        mBusRoute = busRoute;
        mContext = context;
        mCOG = cog;
        mRoute = new ArrayList<LatLng>();
        try {
            JSONArray JSONroute = new JSONObject(route).getJSONArray("Route");
            for(int i = 0 ; i<JSONroute.length(); i++){
                mRoute.add(new LatLng(JSONroute.getJSONObject(i).getDouble("lat"),JSONroute.getJSONObject(i).getDouble("lng")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        //View rootView = super.findViewById(android.R.id.content);
        LinearLayout lv = (LinearLayout) rootView.findViewById(R.id.horizontalLinear);
       // HorizontalScrollView hv = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);

//        LinearLayout busview = new LinearLayout(context);
//        busview.setOrientation(LinearLayout.VERTICAL);
//        TextView busnumbertext = new TextView(context);
//        Log.d("mayankbus",String.format("%s", mBusNumber));
//        busnumbertext.setText(String.format("%s", mBusNumber));
//        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        busnumbertext.setLayoutParams(param1);

        Button bv = new Button(context);
        //Button bv1 = new Button(context);
        bv.setText(mBusName);
      //  bv1.setText("mayank");
       // String bus_id = "Bus_"+(mBusCount+1);
        bv.setId(MainMap.mBusCount);
        bv.setBackgroundColor(Color.WHITE);
        bv.setBackground(getAdaptiveRippleDrawable(R.color.black_overlay,R.color.blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bv.setTextAppearance(R.style.BusButton);
        }
//        busview.addView(bv);
//        busview.addView(busnumbertext);
//
//        lv.addView(busview);
        lv.addView(bv);
    //   hv.setVisibility(View.INVISIBLE);
     //   lv.setMinimumWidth(200);
      //  lv.setWeightSum(1.0f);

       // lv.setWeightSum(lv.getChildCount()+1);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1);
        for (int i=0; i < lv.getChildCount(); i++){

            Log.d("mayank2","i"+i);
            View v = lv.getChildAt(i);
//            LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams)  v.getLayoutParams();
//
//            // Set only target params:
//            loparams.height = 0;
         //   param.width = lv.getWidth();
           // param.weight = 1;
         //   param.width = 0;
            v.setLayoutParams(param);
            v.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ripple,null));
           //Log.d("mayank2",""+v.getWidth());
        //    Log.d("mayank2",""+lv.getWeightSum());
        }
        //((Activity)context
//        ((Activity)context).setContentView(rootView);
        mBusButton = bv;
        mBusButton.setOnClickListener((View.OnClickListener) context); // Ask Mayank set onClickListener to the context of MainMap
        mMarkerOptions = new MarkerOptions()
                            .position(mLocation)
                            .title(mBusName)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_48));
        //mMarker = m_map.addMarker(mMarkerOptions);
        mDirectionOptions = new MarkerOptions()
                                .position(mLocation)
                                .flat(true)
                                .rotation((float) cog)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.arrow));
        MainMap.mBusCount++;
        Log.d("Inside Construct",MainMap.mBusCount+"");
    }


    public static Drawable getAdaptiveRippleDrawable(
            int normalColor, int pressedColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(pressedColor),
                    null, getRippleMask(normalColor));
        } else {
            return getStateListDrawable(normalColor, pressedColor);
        }
    }

    private static Drawable getRippleMask(int color) {
        float[] outerRadii = new float[8];
        // 3 is radius of final ripple,
        // instead of 3 you can give required final radius
        Arrays.fill(outerRadii, 3);

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public static StateListDrawable getStateListDrawable(
            int normalColor, int pressedColor) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_focused},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_activated},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{},
                new ColorDrawable(normalColor));
        return states;
    }

    public String getmBusName() {
        return mBusName;
    }

    public String getmBusRoute() {
        return mBusRoute;
    }

    public String getmBusNumber() {

        return mBusNumber;
    }







    public Marker getMarker(){
        return mMarker;
    }
    public void setMarker(Marker marker){
        mMarker = marker;
    }
    public void setDirectionMarker(Marker marker){
        mDirectionMarker = marker;
    }
    public MarkerOptions getMarkerOptions(){
        return mMarkerOptions;
    }
    public MarkerOptions getDirectionOptions(){
        return mDirectionOptions;
    }
    public void setBusPosition(Double Lat,Double Lng,double cog){
        mLat = Lat;
        mLng = Lng;
        mLocation = new LatLng(Lat,Lng);
        mMarker.setPosition(mLocation);
        mCOG = cog;
        mDirectionMarker.setPosition(mLocation);
        mDirectionMarker.setRotation((float) cog);
    }
    public String getBusCode(){
        return mBusCode;
    }
    public boolean isActive(){
        return mActive;
    }
    public void setBusInFocus(/*Bus activeBus*/) {
        buttonclicke = true;
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
            mBusButton.setTextColor(Color.WHITE);
            CameraPosition newPosition = new CameraPosition.Builder()
                    .target(mLocation)
                    .zoom(17)
                    .tilt(25)
                    .build();
            MainMap.m_map.animateCamera(CameraUpdateFactory.newCameraPosition(newPosition),1000,null);

            MainMap.mActiveBus = this;
            mActive = true;
            MyIntentService.startGetBusStop(mContext,mMarker);

           /* if(MainMap.mUserLocation != null)
            findRouteFromPosition(MainMap.mUserLocation.latitude ,MainMap.mUserLocation.longitude);
            else{
               Log.d("MayankApp","user location is null");
            }*/
            /*return this;*/
        }
    }
    public void setBusOutFocus(/*Bus activeBus*/){
       /* if(activeBus != null){
            activeBus.setBusInOrOutFocus(null);
        }*/
        /*if(isActive())*/{
            mBusButton.setBackgroundColor(Color.WHITE);
            mBusButton.setTextColor(Color.BLACK);
            Log.d("BUS CLASS","HERE");
            Log.d("abcd","hide1");
            mMarker.hideInfoWindow();
            MainMap.mActiveBus = null;
            mActive = false;
            if(MainMap.mActivePolyline != null){
                MainMap.mActivePolyline.remove();
                MainMap.mActivePolyline = null;
            }
            if(MainMap.mBusStop != null){
                MainMap.mBusStop = null;
            }
            if(MainMap.mBusStopMarker != null){
                MainMap.mBusStopMarker.remove();
                MainMap.mBusStopMarker = null;
            }
            if(MainMap.mLoc != null){
                MainMap.mLoc = null;
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
        Log.d("KGPTracking","Find Route" );
        Routing routing;
        if(Lat == null || Lng == null){
            routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(mLocation /* start */,MainMap.mUserLocation /* end */)
                    .build();
        }
        else{
            routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(mLocation /* start */,new LatLng(Lat,Lng),MainMap.mUserLocation /* end */)
                    .build();
        }
        routing.execute();
    }
    public void showRouteOnMap(ArrayList<Route> routes, int shortestRouteIndex){
        Log.d("KGPTracking","Show Route" );
        Gson gson = new Gson();
        Log.d("BUS",gson.toJson(routes));
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(mContext.getResources().getColor(R.color.blue));
        polyOptions.width(10);
        polyOptions.addAll(routes.get(shortestRouteIndex).getPoints());
        if(MainMap.mBusStop != null){
            if(MainMap.mBusStopMarker == null) {
                MarkerOptions busStopOptions = new MarkerOptions()
                        .position(MainMap.mBusStop)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.busstop))
                        .title("Bus Stop - " + MainMap.mLoc);
                MainMap.mBusStopMarker = MainMap.m_map.addMarker(busStopOptions);
                MainMap.mBusStopMarker.showInfoWindow();
            }
        }
        if(MainMap.mActivePolyline != null){ // remove Current Active Polyline everytime before addin g new active Polyline
            MainMap.mActivePolyline.remove();
            MainMap.mActivePolyline = null;
        }
        if(this.equals(MainMap.mActiveBus)) {  // Check if Active Bus right now is equal to the original Bus Where this callback is called
            MainMap.mActivePolyline = MainMap.m_map.addPolyline(polyOptions);
        }
        else if(MainMap.mActiveBus != null){
            MainMap.mActiveBus.setBusInFocus();
        }
    }
    public void showRouteOnMap(){
        Log.d("KGPTracking","Show Route" );
//        Gson gson = new Gson();
//        Log.d("BUS",gson.toJson(routes));
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(mContext.getResources().getColor(R.color.blue));
        polyOptions.width(10);
        Log.d("KGPTracking",mRoute.toString());
        polyOptions.addAll(mRoute);
        if(MainMap.mBusStop != null){
            if(MainMap.mBusStopMarker == null) {
                MarkerOptions busStopOptions = new MarkerOptions()
                        .position(MainMap.mBusStop)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.busstop))
                        .title("Bus Stop - " + MainMap.mLoc);
                  MainMap.mBusStopMarker = MainMap.m_map.addMarker(busStopOptions);

 //               MainMap.mBusStopMarker.showInfoWindow();
            }
        }
        if(MainMap.mActivePolyline != null){ // remove Current Active Polyline everytime before addin g new active Polyline
            MainMap.mActivePolyline.remove();
            MainMap.mActivePolyline = null;
        }
        if(this.equals(MainMap.mActiveBus)) {  // Check if Active Bus right now is equal to the original Bus Where this callback is called
            MainMap.mActivePolyline = MainMap.m_map.addPolyline(polyOptions);
        }
        else if(MainMap.mActiveBus != null){
            MainMap.mActiveBus.setBusInFocus();
        }
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
       // showRouteOnMap(arrayList,shortestRouteIndex);
    }
    @Override
    public void onRoutingCancelled() {
    }

}
