package com.example.mayank.kgptracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.prefs.Preferences;

/**
 * Created by sahil333 on 26/5/16.
 */
public class CustomHurlStack extends HurlStack {
    private Context mContext;
    public CustomHurlStack(Context context){
    mContext = context;
    }
    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.LOGIN_FILE,Context.MODE_PRIVATE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization",/*Constants.ACCESS_TOKEN*/preferences.getString(Constants.TOKEN_TYPE,"")
            +" "+preferences.getString(Constants.ACCESS_TOKEN,"")
            );
           // Log.d("MyIntentService",preferences.getString(Constants.TOKEN_TYPE,"")
              //      +" "+preferences.getString(Constants.ACCESS_TOKEN,""));
            return connection;
    }
}
