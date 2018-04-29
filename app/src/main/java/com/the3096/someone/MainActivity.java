package com.the3096.someone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);

        final Button button = findViewById(R.id.button);
        final EditText textField = findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textField.setText(locationHelper.getLocation().toString());
            }
        }

        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationHelper.init();
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
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
