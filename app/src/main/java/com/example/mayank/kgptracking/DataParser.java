package com.example.mayank.kgptracking;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;

/**
 * Created by sahil333 on 4/6/16.
 */
public class DataParser {
    public class UserData{
        private String mResponse;
        public UserData(String responseData){

        }
    }

    public class TrackData{
        private JSONObject mResponse;
        private boolean mStatus;
        private String mError;
        private JSONArray mTrackData;
        public TrackData(String responseData) throws JSONException {
            mResponse = new JSONObject(responseData);
            mStatus = mResponse.getBoolean(Constants.RESPONSE_STATUS);
            mTrackData = mResponse.getJSONArray(Constants.RESPONSE_DATA);
            mError = mResponse.getString(Constants.RESPONSE_ERROR);
        }
        public LatLng getCoordinates(String buscode) throws JSONException {
            LatLng coordinates = null;
            for(int i = 0 ; i < mTrackData.length() ; i++){
                if(mTrackData.getJSONObject(i).getString(Constants.RESPONSE_BUSCODE).equals(buscode)){
                    coordinates = new LatLng(parseDouble(mTrackData.getJSONObject(i).
                            getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0)
                            .getString(Constants.RESPONSE_LAT)),
                            parseDouble(mTrackData.getJSONObject(i).
                            getJSONArray(Constants.RESPONSE_COORDINATES).getJSONObject(0)
                            .getString(Constants.RESPONSE_LON)
                            ));
                }
            }
            return coordinates;
        }
        public boolean getStatus(){
            return mStatus;
        }
        public String getError(){
            return mError;
        }
    }

    public class BusData{
        private String mResponse;
        private boolean mStatus;
        private String mError;

    }
}
