package com.example.tagalong;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class Locator {
    private Location GPSLocation, NetworkLocation;
    double latitude;
    double longitude;
    String userCountry;
    String userAddress;

    public Locator()
    {

    }

    public void getLocation()
    {
        //LocationManager locationManager = (LocationManager) Context.getSystemService(Context.LOCATION_SERVICE);
    }

}
