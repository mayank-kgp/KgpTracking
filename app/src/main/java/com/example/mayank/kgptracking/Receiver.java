package com.example.mayank.kgptracking;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Receiver extends BroadcastReceiver {
    private Context mRegContext;
    public Receiver(Context context){
        super();
        mRegContext = context;
    }
    private int mDataCount= 0;
    private HashMap<String,String> mData = new HashMap();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyIntentService","received");
        String response = intent.getStringExtra(Constants.INTENT_RESPONSE);
        String action = intent.getAction();
        if(action.equals(MyIntentService.ACTION_GETBUSDATA)) {
            try {
                String busdata = new JSONObject(response).getJSONArray(Constants.RESPONSE_DATA).toString();
                mData.put("Busdata",busdata);
                mDataCount++;
                Log.d("MyIntentService",""+mDataCount + " " + mData.toString());
            } catch (JSONException e) {
                Log.e("Receiver",e.toString());
            }
            if(mDataCount == 2){
                startMainMap(context);
                }
        }
        else if(action.equals(MyIntentService.ACTION_GETTRACKDATA)){
            try {
                String trackdata = new JSONObject(response).getJSONArray(Constants.RESPONSE_DATA).toString();
                mData.put("Trackdata",trackdata);
                mDataCount++;
            } catch (JSONException e) {
                Log.e("Receiver",e.toString());
            }
            if(mDataCount == 2){
                startMainMap(context);
            }
        }

    }
    private void startMainMap(Context context){
        Intent i = new Intent(context,MainMap.class);
        i.putExtra("DATA",mData);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("MyIntentService",mData.toString());
        context.startActivity(i);
        ((Activity)mRegContext).finish();
    }
}
