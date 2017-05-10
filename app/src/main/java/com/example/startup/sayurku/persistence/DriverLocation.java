package com.example.startup.sayurku.persistence;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Startup on 4/23/17.
 */

public class DriverLocation {
   public String lat_cur;
    public String long_cur;
    public String type;

    public  DriverLocation()
    {
        lat_cur="";
        long_cur="";
        type="";
    }

    public LatLng getLatLang()
    {
        return new LatLng(Double.parseDouble(lat_cur),Double.parseDouble(long_cur));
    }

}
