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
    public static final String API_BASEURL = "http://10.5.30.181/Tracking_api/index.php/";
    public static final String API_USERDATAURL = API_BASEURL+"Userdata/user";
    public static final String API_TRACKURL = API_BASEURL+"Tracking/track?buscode=all";
    public static final String API_BUSDATAURL= API_BASEURL+"Busdata/bus?buscode=all";
    public static final String API_BUSSTOPURL= API_BASEURL+"Service/busstop";
    public static final String RESPONSE_STATUS = "status";
    public static final String RESPONSE_ERROR= "error";
    public static final String INTENT_BUSDATA = "BusData";
    public static final String INTENT_RESPONSE = "response";
    public static final String RESPONSE_BUSNAME = "bus_name";
    public static final String RESPONSE_BUSNUMBER = "bus_number";
    public static final String RESPONSE_BUSCODE = "bus_code";
    public static final String RESPONSE_BUSROUTE = "bus_route";
    public static final String RESPONSE_ROUTE = "route";
    public static final String RESPONSE_COG = "cog";
    public static final String RESPONSE_ACTIVE = "active";
    public static final String RESPONSE_COORDINATES= "coordinates";
    public static final String RESPONSE_LAT= "lat";
    public static final String RESPONSE_LON= "lon";
    public static final String RESPONSE_BUSSTOP= "bus_stop";
    public static final String RESPONSE_LOC= "loc";
    //public static final String ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMDM0NDQ0ODI4OTMyNzMwNjk2NjUiLCJlbWFpbCI6InNhaGlsdnMwMDBAZ21haWwuY29tIiwibmFtZSI6InNhaGlsIGNoYWRkaGEiLCJleHAiOjE0Nzk2NjExMTEuMjYxOH0.KoWf-Tb2KrtiAbPxP65feBiYoZcMBsGyy8tvir7QlfE";
    public static final String LOG_TAG = "MyIntentService";
    public static final String LOGIN_FILE = "login_info";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String PIC_URL = "pic_url";
    public static final String EMAIL = "email";
    public static final String USERNAME = "name";
    public static final String TOKEN_EXP = "exp";
    public static final String TOKEN_TYPE = "token_type";
    public static final String isLogin = "isLogin";
}