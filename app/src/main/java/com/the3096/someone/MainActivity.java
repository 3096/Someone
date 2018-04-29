package com.the3096.someone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final private float RADIUS_COMPASS = 200;
    LocationHelper locationHelper;

    static double offset = 0.;



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
                                          Location myLocation = locationHelper.getLocation();
                                          Location destLocation = new Location("fake");

                                          double myLat = myLocation.getLatitude();
                                          double myLon = myLocation.getLongitude();

                                          destLocation.setLatitude(myLat * 1.001);  // Set fake destination coordinates
                                          destLocation.setLongitude(myLat * 1.001);

                                          double bearingToDest = myLocation.bearingTo(destLocation);

                                          setDot(bearingToDest);

                                          String myCoords = "Lat: " + myLat + ", Long:" + myLon + ", Bearing: " + bearingToDest;
                                          textField.setText(myCoords);
                                          setDot(bearingToDest);
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

    public void setDot(double deg){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        float radius = width/4;
        double radian = degreeToRadian(deg);
        ImageView dot = (ImageView) findViewById(R.id.redDot);
        radian  += (Math.PI)/2;
        float x = (float) Math.cos(radian) * radius + radius;
        float y = (float) Math.sin(radian) * - radius + radius;

        Log.e("x location))", "" +x);
        Log.e("y location))", "" +y);
        dot.setY(y +20);
        dot.setX(x-50);
    }
}
