package com.example.mayank.kgptracking;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MyIntentService extends IntentService {

    public static final String ACTION_GETBUSDATA = "com.example.mayank.kgptracking.action.GETBUSDATA";
    public static final String ACTION_GETTRACKDATA = "com.example.mayank.kgptracking.action.GETTRACKDATA";
    public static final String ACTION_PUTUSERDATA= "com.example.mayank.kgptracking.action.PUTUSERDATA";


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
    public static void startPutUserData(Context context,String idtoken) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_PUTUSERDATA);
        intent.putExtra("ID-token",idtoken);
        Log.d("MyIntentService","Put data started");
        context.startService(intent);
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
                    Toast.makeText(MyIntentService.this,"Network Error, No Internet Connection",Toast.LENGTH_LONG).show();
                    Log.d("MyIntentservice",error.toString() + error.getMessage());
                }
            });
        mRequestQueue.add(request);
        }
    private void getResponseString(String url, final String action, int method, final String putdata){
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
                Toast.makeText(MyIntentService.this,"Network Error, No Internet Connection",Toast.LENGTH_LONG).show();
                Log.d("MyIntentservice",error.toString() + error.getMessage());
            }
        });
        mRequestQueue.add(request);
    }
}
