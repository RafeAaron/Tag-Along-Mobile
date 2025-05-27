package com.example.tagalong.Fragments;

import static android.view.View.INVISIBLE;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tagalong.JoinRide.JoinARideDetails;
import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.example.tagalong.home.CurrentTripsAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Objects;

public class HomeFragment extends Fragment {

    Context parentContext;
    View layout;

    String name;

    JSONObject userInfoLocal;

    LocationManager lcManager;

    public HomeFragment(Context context, String name, JSONObject userInfo, LocationManager lcManager) {
        // Required empty public constructor
        this.parentContext = context;
        this.name = name;
        this.userInfoLocal = userInfo;
        this.lcManager = lcManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View endLayout = inflater.inflate(R.layout.fragment_home, container, false);

        try {

            if(Objects.equals(userInfoLocal.getJSONObject("User").get("role").toString(), "Driver")) {
                LinearLayout ll = (LinearLayout) endLayout.findViewById(R.id.bookARide);
                endLayout.findViewById(R.id.bookARide).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(parentContext, "Feature Unavailable for user", Toast.LENGTH_SHORT);
                    }
                });

                ll.setBackgroundColor(Color.BLACK);

                TextView text = (TextView) endLayout.findViewById(R.id.currentTripsInProgressHeading);
                text.setText("Potential Passengers");

                JSONObject object = isDriverInActiveRide();;

                Log.i("Result", object.toString());

                Log.i("Just Some Info", object.toString());

                if(parseInt(object.get("result").toString()) == 1)
                {
                    TextView rideId = (TextView) endLayout.findViewById(R.id.rideNumber);
                    TextView driverName = (TextView) endLayout.findViewById(R.id.driverNameOnTrip);
                    TextView numberOfPassengers = (TextView) endLayout.findViewById(R.id.passengerCount);

                    rideId.setText("Ride ID: #" + parseInt(object.get("ride_id").toString()));
                    driverName.setText("Driver: " + userInfoLocal.getJSONObject("User").get("first_name").toString() + " " + userInfoLocal.getJSONObject("User").get("last_name").toString());
                    numberOfPassengers.setText("Number of Passengers: " + getPassengersInRide(parseInt(object.get("ride_id").toString())).length());
                }

                //TextView driverName = (TextView) endLayout.findViewById(R.id.)

            }else{
                endLayout.findViewById(R.id.startARide).setBackgroundColor(Color.BLACK);
                TextView numberOfPassengers = (TextView) endLayout.findViewById(R.id.passengerCount);
                numberOfPassengers.setText("Number of Passengers: " + "N/A");
                endLayout.findViewById(R.id.startARide).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(parentContext, "Feature Unavailable for user", Toast.LENGTH_SHORT);
                    }
                });
                endLayout.findViewById(R.id.currentTripsInProgressList).setVisibility(INVISIBLE);
                endLayout.findViewById(R.id.currentTripsInProgressHeading).setVisibility(INVISIBLE);
            }
        } catch (JSONException e) {
            Log.e("Error", "Error parsing JSON: " + e.toString() + userInfoLocal.toString());
        }

        RecyclerView homeRecyclerView = (RecyclerView) endLayout.findViewById(R.id.currentTripsInProgressList);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this.parentContext));

        Log.e("Passengers", this.getPassengersInterested().toString());

        CurrentTripsAdapter myCurrentTripsAdapter = new CurrentTripsAdapter(this.getPassengersInterested(), this.lcManager, parentContext);
        myCurrentTripsAdapter.setonclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetailsPage = new Intent(parentContext, JoinARideDetails.class);
                startActivity(goToDetailsPage);
            }
        });

        homeRecyclerView.setAdapter(myCurrentTripsAdapter);
        homeRecyclerView.setMinimumHeight( (myCurrentTripsAdapter.getItemCount() * 2) * 87);

        this.layout = endLayout;

        setWelcomeText(this.name, endLayout);

        return endLayout;
    }

    public View getView()
    {
        return this.layout;
    }

    public void setWelcomeText(String name, View view)
    {

        TextView textView = view.findViewById(R.id.welcomeHeading);
        textView.setText("Welcome " + name);

    }

    public JSONObject getPassengersInterested()
    {

        JSONObject json = new JSONObject();

        JSONObject req = new JSONObject();

        try {
            req.put("user_id", parseInt(this.userInfoLocal.getJSONObject("User").get("id").toString()));

            NetworkConnection connection = new NetworkConnection();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject result = connection.getData(req, "getRideWithUserID");

                    if(result.has("Users"))
                    {
                        try {
                            if(result.getJSONArray("Users").length() != 0){

                                int ride_id = parseInt(result.getJSONArray("Users").getJSONObject(0).get("ride_id").toString());

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("ride_id", ride_id);

                                NetworkConnection conn = new NetworkConnection();
                                JSONObject res2 = conn.getData(jsonObject, "getPassengersRequestingRide");

                                if(res2.has("Error"))
                                {
                                    Log.e("Error", "There was an error with the system" + res2.toString());
                                }else{

                                    JSONObject js = new JSONObject();

                                    for(int a = 0; a < res2.getJSONArray("Passengers").length(); a++)
                                    {
                                        js.put(Integer.toString(a), res2.getJSONArray("Passengers").getJSONObject(a));


                                    }

                                    json.put("Passengers", js);

                                }

                            }
                        } catch (JSONException e) {
                            Log.e("Error", "Failed to parse JSON data: " + e);
                        }
                    }
                }
            });
            thread.start();
            thread.join();

        } catch (JSONException | InterruptedException e) {
            Log.e("Error", "Failed to parse JSON data: " + this.userInfoLocal +" " +e);
        }

        return json;

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
                    userData.put("user_id", parseInt(userInfoLocal.getJSONObject("User").get("id").toString()));

                    JSONObject res = connection.getData(userData, "isDriverInActiveRide");

                    Log.i("Network Response", res.toString());

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

    public JSONObject getPassengersInRide(int ride_id)
    {

        final JSONObject[] status = {new JSONObject()};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                NetworkConnection connection = new NetworkConnection();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("ride_id", ride_id);

                } catch (JSONException e) {
                    Log.e("Error", "Failed to parse JSON data: " + e.toString());
                }

                JSONObject res = connection.getData(obj, "getPassengersInRide");

                try {
                    if(res.has("number_Of_Passengers"))
                    {
                        status[0].put("number", parseInt(res.get("number_Of_Passengers").toString()));
                    }else{
                        Log.e("Error", "Failed to get ride information");
                    }

                } catch (JSONException e) {
                    Toast.makeText(parentContext, "Failed to get rides information", Toast.LENGTH_SHORT).show();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Toast.makeText(parentContext, "Failed to get driver details", Toast.LENGTH_SHORT).show();
        }

        return status[0];

    }
}