package com.example.tagalong.StartRide;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Objects;

public class StartARide extends AppCompatActivity {

    RadioGroup seatsAvailable;
    EditText numberOfSeatsSpecified;
    TextView numberOfSeatsSpecifiedHeading;
    EditText startLocation;
    EditText destination;
    Button advertiseRide;
    int numberOfAvailableSeats = -1;

    double location_x;
    double location_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_aride);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        seatsAvailable = (RadioGroup) findViewById(R.id.numberOfSeatsSelected);
        numberOfSeatsSpecified = (EditText) findViewById(R.id.numberOfSeatsSpecified);
        numberOfSeatsSpecifiedHeading = (TextView) findViewById(R.id.numberOfSeatsSpecifiedHeading);

        advertiseRide = (Button) findViewById(R.id.buttonToAdvertiseRide);

        startLocation = (EditText) findViewById(R.id.startPosition);
        destination = (EditText) findViewById(R.id.endPosition);

        numberOfSeatsSpecified.setVisibility(INVISIBLE);
        numberOfSeatsSpecifiedHeading.setVisibility(INVISIBLE);

        advertiseRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberOfAvailableSeats == 100) {
                    numberOfAvailableSeats = Integer.valueOf(numberOfSeatsSpecified.getText().toString());
                }

                if (ActivityCompat.checkSelfPermission(StartARide.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(StartARide.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(StartARide.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(StartARide.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                }

                try {

                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        {
                            Toast.makeText(StartARide.this, "Please enable Location and try again", Toast.LENGTH_SHORT).show();
                            return;

                        }else{

                            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            //Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            location_x = gpsLocation.getLatitude();
                            location_y = gpsLocation.getLongitude();

                            if(location_x == 0.0 || location_y == 0.0)
                            {
                                Toast.makeText(StartARide.this, "Location unavailable, please wait a few secconds", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                } catch (Exception e) {
                    Log.e("Location Error", "Failed to get location");
                }

                /*
                Logic to handle advertisement of available seats.

                use:
                    startLocation for the starting point
                    endLocation for the ending point
                    numberOfAvailableSeats for the numberOfAvailable seats
                 */

                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("start_location", startLocation.getText().toString());
                    jsonObject.put("end_location", destination.getText().toString());
                    jsonObject.put("current_location_x", Double.toString(location_x));
                    jsonObject.put("current_location_y", Double.toString(location_y));
                    jsonObject.put("number_of_passengers", numberOfAvailableSeats);
                    jsonObject.put("route", "4 -> 6 -> 8 -> 5 -> 9 -> 6 -> 9");
                    jsonObject.put("current_amount", 4500);

                    Thread newThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject json = new JSONObject();
                            try {

                                JSONObject obj = isDriverInActiveRide();

                                if(obj.has("result"))
                                {
                                    String result = obj.get("result").toString();

                                    Log.i("Your Data", result);

                                    if(parseInt(result) == 1)
                                    {
                                        StartARide.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(StartARide.this, "User is already present in a ride", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }else{
                                        startARide(jsonObject);
                                        goToSuccessStartARidePage();
                                    }

                                }

                            } catch (JSONException e) {
                                Log.e("Error", e.toString());
                            }
                        }
                    });
                    newThread.start();
                    try {
                        newThread.join();
                    } catch (InterruptedException e) {
                        Log.e("Error", e.toString());
                    }

                }catch (JSONException ex)
                {
                    Log.e("JSON Error", "There is an error parsing the JSON");
                }

            }
        });

        seatsAvailable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                numberOfSeatsSpecified.setVisibility(INVISIBLE);
                numberOfSeatsSpecifiedHeading.setVisibility(INVISIBLE);

                if(checkedId == R.id.numberOfSeats1)
                {

                    numberOfAvailableSeats = 1;

                }else if(checkedId == R.id.numberOfSeats2)
                {
                    numberOfAvailableSeats = 2;
                }
                else if(checkedId == R.id.numberOfSeats3)
                {

                    numberOfAvailableSeats = 3;

                }
                else if(checkedId == R.id.numberOfSeats4)
                {

                    numberOfAvailableSeats = 4;

                }
                else if(checkedId == R.id.numberOfSeats5_)
                {
                    numberOfAvailableSeats = 100;

                    numberOfSeatsSpecified.setVisibility(VISIBLE);
                    numberOfSeatsSpecifiedHeading.setVisibility(VISIBLE);

                }
            }
        });

    }

    public void goBackStartARide(View view)
    {
        finish();
    }

    public void goToSuccessStartARidePage()
    {

        Intent goToSuccessPage = new Intent(StartARide.this, StartARideConfirmationPage.class);
        startActivity(goToSuccessPage);
        finish();
    }

    public boolean startARide(JSONObject parameters) {

        boolean success = false;
            Thread newThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    NetworkConnection connection = new NetworkConnection();

                    if (connection.checkConnection()) {

                        JSONObject response = connection.postData(parameters, "addRide");


                        if(!response.has("errno"))
                        {
                            //Toast.makeText(StartARide.this, "Started ride successfully", Toast.LENGTH_SHORT).show();

                            try {
                                int ride_id = parseInt(response.get("ride_id").toString());
                                JSONObject userDetails = new JSONObject(getIntent().getStringExtra("userInfo"));

                                JSONObject passengerDetails = new JSONObject();
                                passengerDetails.put("ride_id", ride_id);
                                passengerDetails.put("user_id", userDetails.getJSONObject("User").getInt("id"));

                                JSONObject response2 = connection.postData(passengerDetails, "addPassenger");

                                if(response2.has("errno"))
                                {
                                    Log.e("JSON Error", "There was an error updating ride details");
                                }else{
                                    Log.i("Ride Update Successful", "Ride details updated successfully");

                                    goToSuccessStartARidePage();
                                }

                            }catch (JSONException jsex)
                            {
                                Log.e("JSON Error", "There was an error parsing the JSON");
                            }
                        }else{
                            Toast.makeText(StartARide.this, "There was an error starting the ride", Toast.LENGTH_SHORT).show();
                        }




                    } else {
                        Toast.makeText(StartARide.this, "Failed to upload details", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            newThread.start();

        return success;

    }

    public JSONObject isDriverInActiveRide()
    {

        final JSONObject[] status = {new JSONObject()};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                NetworkConnection connection = new NetworkConnection();

                try {



                    JSONObject userData = new JSONObject();
                    userData.put("user_id", parseInt(new JSONObject(getIntent().getStringExtra("userInfo")).getJSONObject("User").get("id").toString()));

                    JSONObject res = connection.getData(userData, "isDriverInActiveRide");

                    //Log.i("Network Response", res.toString());

                    if(res.has("result"))
                    {
                        if(Objects.equals(res.get("result").toString(), "true"))
                        {
                            status[0].put("result", 1);
                            status[0].put("ride_id", parseInt(res.get("ride_id").toString()));
                        }else{
                            status[0].put("result", 0);
                        }
                    }

                } catch (JSONException e) {
                    Log.e("Error", "Failed to get rides information " + e.toString());
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Error", "Failed to get rides information " + e.toString());
        }

        return status[0];

    }
}