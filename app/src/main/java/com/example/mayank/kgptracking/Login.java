package com.example.mayank.kgptracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
        ,View.OnClickListener {
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 69;
    private static final String TAG = "login Check";
    private LoginReceiver mReceiver = new LoginReceiver();
    private Receiver mBroadcastReceiver = new Receiver(this);
    SignInButton  signInButton;
    GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       */ // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        if (signInButton != null) {
            signInButton.setSize(SignInButton.SIZE_WIDE);
           // signInButton.setColorScheme(SignInButton.COLOR_DARK);
            signInButton.setScopes(gso.getScopeArray());
        }


        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
               // .addApi(Plus.API,gso)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(MyIntentService.ACTION_GETBUSDATA);
        filter2.addAction(MyIntentService.ACTION_GETTRACKDATA);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(mBroadcastReceiver, filter2);
        IntentFilter filter = new IntentFilter(MyIntentService.ACTION_PUTUSERDATA);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
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
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"connection failed",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            MyIntentService.startPutUserData(this,acct.getIdToken());
        } else {
            // Signed out, show unauthenticated UI.
           Log.d( TAG, result.getStatus().toString());
           //Log.d( TAG, result.getStatus().toString());
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    
    private class LoginReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
        // Log.d(TAG,intent.getStringExtra(Constants.INTENT_RESPONSE));
            try {
                JSONObject response = new JSONObject(intent.getStringExtra(Constants.INTENT_RESPONSE));
                if(response.getBoolean(Constants.RESPONSE_STATUS)) {
                    JSONObject data = response.getJSONObject(Constants.RESPONSE_DATA);
                    SharedPreferences preferences = getSharedPreferences(Constants.LOGIN_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(Constants.isLogin, true);
                    editor.putString(Constants.ACCESS_TOKEN, data.getString(Constants.ACCESS_TOKEN));
                    editor.putString(Constants.USERNAME, data.getString(Constants.USERNAME));
                    editor.putString(Constants.EMAIL, data.getString(Constants.EMAIL));
                    editor.putFloat(Constants.TOKEN_EXP, BigDecimal.valueOf(data.getDouble(Constants.TOKEN_EXP)).floatValue());
                    editor.putString(Constants.PIC_URL, data.getString(Constants.PIC_URL));
                    editor.putString(Constants.TOKEN_TYPE, data.getString(Constants.TOKEN_TYPE));
                    editor.commit();
                    Log.d("MyIntentService",data.getString(Constants.ACCESS_TOKEN));
                    MyIntentService.startGetBusData(getApplicationContext());
                    MyIntentService.startGetTrackData(getApplicationContext());
                    Log.d("FromPreference",preferences.getString(Constants.ACCESS_TOKEN,"not there"));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Login failed. Please Try Again! " +response.getString(Constants.RESPONSE_ERROR),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Couldn't process the response from server!",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
