package com.example.tagalong.JoinRide;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.function.Consumer;

public class JoinRide extends AppCompatActivity {

    RecyclerView joinARideRecyclerView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_ride);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {

            JSONArray array = getActiveRides().getJSONArray("rides");
            JSONObject[] passengers = getPassengersInRides();

            String[] Drivers = new String[array.length()];
            int[] ids = new int[array.length()];
            String[] startLocations = new String[array.length()];
            String[] endLocations = new String[array.length()];
            String[] numberOfPassengers = new String[array.length()];
            String[] genderOfPassengers = new String[array.length()];
            String[] ageOfPassengers = new String[array.length()];
            JSONObject passengerDetailsAdvanced = new JSONObject();


            for(int i = 0; i < array.length(); i++)
            {

                JSONObject rideDetails = array.getJSONObject(i);

                startLocations[i] = rideDetails.get("start_location").toString();
                endLocations[i] = rideDetails.get("end_location").toString();

                ids[i] = parseInt(rideDetails.get("ride_id").toString());

                JSONArray moreDetails = new JSONArray();

                if(passengers[i].has("Passengers"))
                {
                    moreDetails = passengers[i].getJSONArray("Passengers");
                }

                JSONObject[] passengerDetails = new JSONObject[moreDetails.length()];


                numberOfPassengers[i] = Integer.toString(moreDetails.length() - 1);

                for(int a = 0; a < moreDetails.length(); a++)
                {
                    JSONObject o = moreDetails.getJSONObject(a);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("user_id", parseInt(o.get("user_id").toString()));

                        NetworkConnection connection = new NetworkConnection();

                        int finalA = a;
                        int finalI = i;
                    Thread newThread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    JSONObject userInfo = connection.getData(jsonObject, "getUser").getJSONObject("User");

                                    Log.i("user_id", userInfo.get("gender").toString());

                                    if(finalA == 0) {
                                        Drivers[finalI] = userInfo.get("first_name").toString() + " " +userInfo.get("last_name").toString();


                                    }else {
                                        passengerDetails[finalA - 1] = userInfo;

                                    }

                                    genderOfPassengers[finalI] = userInfo.get("gender").toString();
                                    ageOfPassengers[finalI] = userInfo.get("age").toString();

                                } catch (JSONException e) {
                                    Log.e("Error", "Failed to parse JSON data");
                                }
                            }
                        });

                        newThread.start();
                        newThread.join();
                }

                passengerDetailsAdvanced.put(rideDetails.get("ride_id").toString(), passengerDetails);

            }

            JoinARideAdapater joinARideAdapter = new JoinARideAdapater(this, Drivers, startLocations, endLocations, numberOfPassengers, ids, genderOfPassengers);
            joinARideAdapter.setViewOnCLickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent goToDetailsPage = new Intent(context, JoinARideDetails.class);

                    JSONObject id = (JSONObject) v.getTag();

                    for(int i = 0; i < array.length(); i++)
                    {

                        try {
                            JSONObject obj = array.getJSONObject(i);
                            if(parseInt(obj.get("ride_id").toString()) == parseInt(id.get("id").toString()))
                            {
                                JSONObject objBig = new JSONObject();
                                JSONObject[] jsonObj = ((JSONObject[]) passengerDetailsAdvanced.get(obj.get("ride_id").toString()));
                                for(int a = 0; a < ((JSONObject[]) passengerDetailsAdvanced.get(obj.get("ride_id").toString())).length - 1; a++)
                                {
                                    JSONObject ne = jsonObj[a];
                                    objBig.put("" + a, ne.toString());
                                }

                                goToDetailsPage.putExtra("rideDetails", objBig.toString());

                                //Log.i("Success Details", ((JSONObject) passengerDetailsAdvanced.get(id.get("id").toString())).toString());
                                goToDetailsPage.putExtra("ride_id", obj.get("ride_id").toString());
                                goToDetailsPage.putExtra("user_id", getIntent().getStringExtra("userId"));
                                startActivity(goToDetailsPage);
                            }
                        } catch (JSONException e) {
                            Log.e("Error", "Error parsing JSON" + e);
                        }

                    }
                    //goToDetailsPage.putExtra("rideDetails", passengerDetailsAdvanced.toString());

                    //Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show();
                }
            });

            joinARideRecyclerView = (RecyclerView) this.findViewById(R.id.joinARideRecyclerView);
            joinARideRecyclerView.setAdapter(joinARideAdapter);
            joinARideRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        } catch (JSONException | InterruptedException e) {
            Log.e("JSON Error", "Error Parsing JSON" + e);
        }

    }

    public void goBackToHomePageFromJoinARide(View view)
    {
        finish();
    }

    public JSONObject getActiveRides() throws InterruptedException {

        final JSONObject[] results = {null};

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {

                NetworkConnection connection = new NetworkConnection();

                if(connection.checkConnection())
                {

                    results[0] = connection.postData(new JSONObject(), "getActiveRides");
                    Log.i("Success", results[0].toString());



                }else{
                    Log.e("Network Error", "Failed to connect to network");
                }

            }
        });

        newThread.start();
        newThread.join();

        return results[0];

    }

    public JSONObject[] getPassengersInRides() throws JSONException, InterruptedException {

        JSONObject object = getActiveRides();

        Log.i("Data", object.toString());
        int numberOfRides = object.getJSONArray("rides").length();

        JSONObject[] results = new JSONObject[numberOfRides];

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {

                NetworkConnection connection = new NetworkConnection();

                if(connection.checkConnection())
                {
                    try {
                        JSONArray array = getActiveRides().getJSONArray("rides");

                        for(int i = 0; i < array.length(); i++)
                        {
                            JSONObject ride = new JSONObject(array.get(i).toString());

                            JSONObject rideDetails = new JSONObject();
                            rideDetails.put("ride_id", parseInt(ride.get("ride_id").toString()));

                            results[i] = connection.getData(rideDetails, "getPassengersInRide");

                            Log.i("Success", results[i].toString());

                        }

                    } catch (JSONException e) {
                        Log.e("Error", "Failed to parse JSON data");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    Log.e("Network Error", "Failed to connect to network");
                }

            }
        });

        newThread.start();
        newThread.join();

        return results;
    }
}