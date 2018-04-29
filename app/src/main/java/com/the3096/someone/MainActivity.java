package com.the3096.someone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final private float RADIUS_COMPASS = 200;
    LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);

 	setDot(Math.PI/2);

        final Button button = findViewById(R.id.button);
        final EditText textField = findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Location myLocation = locationHelper.getLocation();
                                          Location destLocation = new Location("fake");

                                          double myLat = myLocation.getLatitude();
                                          double myLon = myLocation.getLongitude();

                                          destLocation.setLatitude(myLat * 1.001);  // Set fake destination coordinates
                                          destLocation.setLongitude(myLat * 1.001);

                                          double bearingToDest = myLocation.bearingTo(destLocation);

                                          String myCoords = "Lat: " + myLat + ", Long:" + myLon + ", Bearing: " + bearingToDest;
                                          textField.setText(myCoords);
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

    public double degreeToRadian(double deg) {
        double converted = deg * (Math.PI / 180.0);
        return converted;
    }
   
    public void setDot(double radian){
        ImageView dot = (ImageView) findViewById(R.id.redDot);
        radian  += (Math.PI)/2;
        float x = (float) Math.cos(radian) * RADIUS_COMPASS + RADIUS_COMPASS;
        float y = (float) Math.sin(radian) * - RADIUS_COMPASS + RADIUS_COMPASS;

        Log.e("x location))", "" +x);
        Log.e("y location))", "" +y);
        dot.setY(y);
        dot.setX(x);
    }
}
