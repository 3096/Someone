package com.the3096.someone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    GPSTracker gps = new GPSTracker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
    }

    /* Called when the user taps the button.  Or button is not necessary? */
    public void getDirections(View view) {

        // Get current location coordinates (latitude, longititude) of user.
        double userLong = 0;
        double userLat = -122.129932;

        // Get location coordinates of the second user.  Hard-code it as a constant for now.
        double destinationLong = 37.362511;
        double destinationLat = -122.129932;

        //Calculate the bearing to the destination and display it in the activity
    }

    public double degreeToRadian(double degree) {
        return degree * (Math.PI / 180.0);
    }

    public double getBearing(double userLong, double userLat, double destLong, double destLat) {
        userLong = degreeToRadian(userLong);
        userLat = degreeToRadian(userLat);
        destLong = degreeToRadian(destLong);
        destLat = degreeToRadian(destLat);

        double angle = Math.atan2(destLat - userLat, destLong - userLong);

        return Math.PI - angle;
    }
}
