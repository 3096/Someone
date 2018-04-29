package com.the3096.someone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    LocationHelper locationHelper;

    final boolean sending = true;

    Button button;
    EditText textField;

    Thread networkThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textField = findViewById(R.id.editText);

        locationHelper = new LocationHelper(this);

        networkThread = new Thread(new NetworkThread());
        networkThread.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class NetworkThread implements Runnable {

        public void run() {
            String server = "192.168.1.106";
            int port = 1337;

            while(true) {
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
                        textField.setText("Listening...");

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

                            String readStr = lat + "," + lon;
                            textField.setText(readStr);
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
