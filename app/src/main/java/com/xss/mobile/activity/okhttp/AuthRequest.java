package com.xss.mobile.activity.okhttp;

/**
 * Created by xss on 2017/11/8.
 */

public class AuthRequest {

    public String response_type = "code";
    public String client_id	= "4v2r3chY5o";
    public String ksid = "MDVmMDhjN2UtYjQ5Mi00YTNmLThmOWYzIzYm";
    public String scope	= "all";
    public String state	= "5530c600-7752-4ea1-8951-6aa18ed09206";
    public String redirect_uri = "https://localhost:3001";
    public String action = "SUBMIT";
    public String appStatus	= "ONLINE";
    public String loginUri = "https://app-api-shop.alpha.elenet.me/arena/invoke/";


    private static AuthRequest mInstance;

    private String location;

    public static AuthRequest getInstance() {
        if (mInstance == null) {
            mInstance = new AuthRequest();
        }
        return mInstance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
