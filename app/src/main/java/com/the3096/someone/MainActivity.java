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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    final private float RADIUS_COMPASS = 200;
    LocationHelper locationHelper;

    final boolean sending = true;

    EditText coordField;

    Thread networkThread;

    Location myLocation;

    static double offset = 0.;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);

        networkThread = new Thread(new NetworkThread());
        networkThread.start();

        final Button button = findViewById(R.id.button);
        coordField = findViewById(R.id.boxGPS);
        final EditText lonField = findViewById(R.id.boxLon);
        final EditText latField = findViewById(R.id.boxLat);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLocation = locationHelper.getLocation();
                Location destLocation = new Location("fake");

                double myLat = myLocation.getLatitude();
                double myLon = myLocation.getLongitude();
                double destLon;
                double destLat;

                try {
                    destLon = Double.parseDouble(lonField.getText().toString());
                    destLat = Double.parseDouble(latField.getText().toString());
                } catch(Exception e) {
                    destLon = 0;
                    destLat = 0;
                }
                destLocation.setLatitude(destLat);
                destLocation.setLongitude(destLon);

                double bearingToDest = myLocation.bearingTo(destLocation);

                String myCoords = "Lat: " + myLat + ", Long:" + myLon + ", Bearing: " + bearingToDest;
                coordField.setText(myCoords);
                setDot(bearingToDest);
            }
        } );
    }

    class NetworkThread implements Runnable {

        public void run() {
            String server = "192.168.1.106";
            int port = 1337;

            while (true) {
                try {
                    Socket socket;
                    DataOutputStream dOut;
                    DataInputStream dIn;

                    if (sending) {
                        socket = new Socket(server, port);
                        dOut = new DataOutputStream(socket.getOutputStream());

                        while (true) {
                            dOut.writeByte(1);
                            dOut.writeDouble(locationHelper.getLocation().getLatitude());
                            dOut.flush();

                            dOut.writeByte(2);
                            dOut.writeDouble(locationHelper.getLocation().getLongitude());
                            dOut.flush();

                            Thread.sleep(1000);
                        }
                    } else {

                        ServerSocket serverSocket = new ServerSocket(port);
                        socket = serverSocket.accept();
                        dIn = new DataInputStream(socket.getInputStream());
                        //textField.setText("Listening...");

                        double lat = -1, lon = -1;
                        boolean done = false;
                        while (!done) {
                            byte messageType = dIn.readByte();

                            switch (messageType) {
                                case 1: // Type A
                                    lat = dIn.readDouble();
                                    break;
                                case 2: // Type B
                                    lon = dIn.readDouble();
                                    break;
                                default:
                                    done = true;
                            }

                            String receiveStr = lat+","+lon;
                            coordField.setText(receiveStr);

                            Location receivedLoc = new Location("");
                            receivedLoc.setLatitude(lat);
                            receivedLoc.setLongitude(lon);

                            double bearingToDest = myLocation.bearingTo(receivedLoc);
                            setDot(bearingToDest);
                        }
                    }
                } catch (Exception e) {
                    Log.e("someone", "exception", e);
                } finally {

                }

            }
        }
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

    public void setDot(double deg) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        float radius = width/2;
        double radian = degreeToRadian(deg);
        ImageView dot = (ImageView) findViewById(R.id.redDot);
        radian  += (Math.PI);
        float x = (float) - Math.sin(radian) * radius + radius;
        float y = (float) Math.cos(radian) * radius + radius;

        Log.e("x location))", "" +x);
        Log.e("y location))", "" +y);
        dot.setY(y +20);
        dot.setX(x-50);
    }
}
