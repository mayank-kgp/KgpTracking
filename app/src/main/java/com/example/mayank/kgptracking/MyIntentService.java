package com.example.mayank.kgptracking;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;
public class MyIntentService extends IntentService {
    public static final String ACTION_GETBUSDATA = "com.example.mayank.kgptracking.action.GETBUSDATA";
    public static final String ACTION_GETTRACKDATA = "com.example.mayank.kgptracking.action.GETTRACKDATA";
    public static final String ACTION_PUTUSERDATA= "com.example.mayank.kgptracking.action.PUTUSERDATA";
    public static final String ACTION_GETBUSSTOP= "com.example.mayank.kgptracking.action.GETBUSSTOP";
    public static boolean loop = true;
    public MyIntentService() {
        super("MyIntentService");
    }
    private static boolean isTokenExpired(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.LOGIN_FILE,Context.MODE_PRIVATE);
        float exp_time = preferences.getFloat(Constants.TOKEN_EXP, (float) 1.0);
        if(System.currentTimeMillis()*1000 > exp_time){
            Log.d("MyIntentService","Token Expired" + System.currentTimeMillis()*1000 + "  " + exp_time );
            Intent i = new Intent(context,Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            MyIntentService.loop = false;
            context.getSharedPreferences(Constants.LOGIN_FILE, Context.MODE_PRIVATE).edit().clear().commit();
            context.startActivity(i);
            return true;
        }
        else return false;
    }
    public static void startGetBusData(Context context) {
        if(!isTokenExpired(context)){
            Intent intent = new Intent(context, MyIntentService.class);
            intent.setAction(ACTION_GETBUSDATA);
            context.startService(intent);
        }
    }

    public static void startGetTrackData(Context context) {
        if(!isTokenExpired(context)) {
            Intent intent = new Intent(context, MyIntentService.class);
            intent.setAction(ACTION_GETTRACKDATA);
            Log.d("MyIntentService", "Track data started");
            context.startService(intent);
        }
    }
    public static void startPutUserData(Context context,String idtoken) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_PUTUSERDATA);
        intent.putExtra("ID-token",idtoken);
        Log.d("MyIntentService","Put data started");
        context.startService(intent);
    }
    public static void startLoopTrackData(final Context context){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                startjob(context);
            }
        });
        t.start();
    }
    public static void startGetBusStop(Context context, Marker mMarker) {
        if(!isTokenExpired(context)) {
            Intent intent = new Intent(context, MyIntentService.class);
            intent.setAction(ACTION_GETBUSSTOP);
            context.startService(intent);
            if (mMarker.isInfoWindowShown() == false) {
                mMarker.showInfoWindow();
            }
        }
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GETBUSDATA.equals(action)) {
                handleActionGetBusData();
            }
            else if(ACTION_GETTRACKDATA.equals(action)){
                handleActionGetTrackData();
            }
            else if(ACTION_PUTUSERDATA.equals(action)){
                handleActionPutUserData(intent.getStringExtra("ID-token"));
            }
            else if(ACTION_GETBUSSTOP.equals(action)){
                handleActionGetBusStop();
            }
        }
    }
    private void handleActionGetBusStop() {
        if(MainMap.mUserLocation!=null) {
            String url = Constants.API_BUSSTOPURL + "?user_lat=" + MainMap.mUserLocation.latitude + "&user_lon=" + MainMap.mUserLocation.longitude;
            getResponseString(url, ACTION_GETBUSSTOP, Request.Method.GET, null);
        }
    }
    private static void startjob(Context context){
        Log.d("abcd","startjob executecd");
        MyIntentService.startGetTrackData(context);
        while(loop) {
            try {
                Thread.sleep(5000);
                Log.d("abcd", "Try working");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startjob(context);
        }
    }
    private void handleActionPutUserData(String idtoken) {
        getResponse(Constants.API_USERDATAURL,ACTION_PUTUSERDATA,Request.Method.PUT,idtoken);
    }
    private void handleActionGetTrackData() {
        getResponseString(Constants.API_TRACKURL,ACTION_GETTRACKDATA,Request.Method.GET,null);
    }
    private void handleActionGetBusData() {
        // TODO: Handle action Foo
        getResponseString(Constants.API_BUSDATAURL,ACTION_GETBUSDATA,Request.Method.GET,null);;
    }
    private void getResponse(String url, final String action, int method, final String putdata){

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(),
                new CustomHurlStack(this));
        //Log.d("MyIntentService",putdata);
        JSONObject params = null;
        if(putdata != null) {
            params = new JSONObject();
            try {
                params.put("id-token",putdata);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        JsonObjectRequest request = new JsonObjectRequest(method, url,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent i = new Intent();
                if(action.equals(ACTION_GETBUSDATA)){
                    i.setAction(ACTION_GETBUSDATA);
                }
                else if(action.equals(ACTION_GETTRACKDATA)){
                    i.setAction(ACTION_GETTRACKDATA);
                }
                else if(action.equals(ACTION_PUTUSERDATA)){
                    i.setAction(ACTION_PUTUSERDATA
                    );
                }
                i.putExtra(Constants.INTENT_RESPONSE,response.toString());
                Log.d("MyIntentService",action + " : " + response.toString());
                LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if(error instanceof NoConnectionError) {
//                    Toast.makeText(MyIntentService.this, "Network Error, No Internet Connection", Toast.LENGTH_LONG).show();
//                }
                Log.d("MyIntentservice",error.toString() + error.getMessage());
            }
        });
        mRequestQueue.add(request);
    }
    private void getResponseString(String url, final String action, int method, final String putdata){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(),
                new CustomHurlStack(this));
        //Log.d("MyIntentService",putdata);
        /*JSONObject params = null;
        if(putdata != null) {
            params = new JSONObject();
            try {
                params.put("id-token",putdata);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }*/
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent i = new Intent();
                if(action.equals(ACTION_GETBUSDATA)){
                    i.setAction(ACTION_GETBUSDATA);
                }
                else if(action.equals(ACTION_GETTRACKDATA)){
                    i.setAction(ACTION_GETTRACKDATA);
                }
                else if(action.equals(ACTION_PUTUSERDATA)){
                    i.setAction(ACTION_PUTUSERDATA);
                }
                else if(action.equals(ACTION_GETBUSSTOP)){
                    i.setAction(ACTION_GETBUSSTOP);
                }
                i.putExtra(Constants.INTENT_RESPONSE,response);
                Log.d("MyIntentService",action + " : " + response);
                LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if(error instanceof NoConnectionError) {
//                    Toast.makeText(MyIntentService.this, "Network Error, No Internet Connection", Toast.LENGTH_LONG).show();
//                }
                Log.d("MyIntentservice",error.toString() + error.getMessage());
            }
        });
        mRequestQueue.add(request);
    }
}