package com.example.android.demo;



public class Get_Connection_Status {

    private static String url_api = "https://api-skin-dis.herokuapp.com" ;
    private static Boolean APIConnection =true;
    private static Boolean InternetConnection=true;

    public static Boolean getAPIConnection() {
        return APIConnection;
    }

    public static Boolean getInternetConnection() {
        return InternetConnection;
    }

    public static void setInternetConnection(Boolean internetConnection) {
        InternetConnection = internetConnection;
    }

    public static void setAPIConnection(Boolean APIConnection) {
        Get_Connection_Status.APIConnection = APIConnection;
    }

    public static String getUrl_api() {
        return url_api;
    }

    public static void setUrl_api(String url_api) {
        Get_Connection_Status.url_api = url_api;
    }
}
