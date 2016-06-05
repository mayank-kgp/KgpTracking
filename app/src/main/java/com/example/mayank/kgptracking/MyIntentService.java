package com.example.mayank.kgptracking;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MyIntentService extends IntentService {

    public static final String ACTION_GETBUSDATA = "com.example.mayank.kgptracking.action.GETBUSDATA";
    public static final String ACTION_GETTRACKDATA = "com.example.mayank.kgptracking.action.GETTRACKDATA";


    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startGetBusData(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_GETBUSDATA);
        context.startService(intent);
    }
    public static void startGetTrackData(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_GETTRACKDATA);
        Log.d("MyIntentService","Track data started");
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GETBUSDATA.equals(action)) {
                handleActionGetBusData();
            }
            if(ACTION_GETTRACKDATA.equals(action)){
                handleActionGetTrackData();
            }
        }
    }

    private void handleActionGetTrackData() {
        getResponse(Constants.API_TRACKURL,ACTION_GETTRACKDATA);

    }

    private void handleActionGetBusData() {
        // TODO: Handle action Foo
        getResponse(Constants.API_BUSDATAURL,ACTION_GETBUSDATA);;
    }

    private void getResponse(String url, final String action){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(),
                new CustomHurlStack());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Intent i = new Intent();
                    if(action.equals(ACTION_GETBUSDATA)){
                        i.setAction(ACTION_GETBUSDATA);
                    }
                    else if(action.equals(ACTION_GETTRACKDATA)){
                        i.setAction(ACTION_GETTRACKDATA);
                    }
                    i.putExtra(Constants.INTENT_RESPONSE,response.toString());
                    Log.d("MyIntentService",action + " : " + response.toString());
                    LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(i);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MyIntentservice",error.toString());
                }
            });
        mRequestQueue.add(request);
        }
}
