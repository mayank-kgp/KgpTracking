package com.example.mayank.kgptracking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TrackService extends Service {
    public final static  String trackaction = "GET_TRACK_DATA";
    public TrackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         
        return super.onStartCommand(intent, flags, startId);
    }
}
