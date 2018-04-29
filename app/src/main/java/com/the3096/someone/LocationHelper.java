package com.the3096.someone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

class LocationHelper {
    private Activity mContext;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location mLocation;
    private String mLocationProvider;

    LocationHelper(Activity context) {
        mContext = context;
        mLocationProvider = LocationManager.NETWORK_PROVIDER;

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            init();
        }
    }

    @SuppressLint("MissingPermission")
    void init() {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                mLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        mLocationManager.requestLocationUpdates(mLocationProvider, 0, 0, mLocationListener);
    }

    @SuppressLint("MissingPermission")
    Location getLocation() {
        if(mLocation != null)
            return mLocation;
        return mLocationManager.getLastKnownLocation(mLocationProvider);
      }
}
