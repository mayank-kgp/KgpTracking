package com.example.mayank.kgptracking;

/**
 * Created by mayank on 15/5/16.
 */
public class Constants {
    public static final String PACKAGE_NAME = "com.example.mayank.kgptracking";

    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";
    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".ACTIVITY_EXTRA";
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 0;
    public static final String RESPONSE_DATA = "data";
    public static final String API_BASEURL = "http://mayankjindal.in/Tracking_api/index.php/";
    public static final String API_USERDATAURL = API_BASEURL+"UserData/user";
    public static final String API_TRACKURL = API_BASEURL+"Tracking/track?buscode=all";
    public static final String API_BUSDATAURL= API_BASEURL+"Busdata/bus?buscode=all";
    public static final String RESPONSE_STATUS = "status";
    public static final String INTENT_BUSDATA = "BusData";
    public static final String INTENT_RESPONSE = "response";
    public static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMDM0NDQ0ODI4OTMyNzMwNjk2NjUiLCJlbWFpbCI6InNhaGlsdnMwMDBAZ21haWwuY29tIiwibmFtZSI6InNhaGlsIGNoYWRkaGEiLCJleHAiOjE0Nzk2NjExMTEuMjYxOH0.KoWf-Tb2KrtiAbPxP65feBiYoZcMBsGyy8tvir7QlfE";
}
