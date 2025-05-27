package com.example.tagalong.JoinRide;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.example.tagalong.StartRide.StartARide;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinARideDetails extends AppCompatActivity {

    RecyclerView joinARideDetailsRecyclerView;
    TextView currentNumberOfPeopleInTrip;

    Button requestBtn;

    double location_x;
    double location_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_aride_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            JSONObject object = new JSONObject(getIntent().getExtras().getString("rideDetails"));

            Log.i("Success", object.toString());

            currentNumberOfPeopleInTrip = (TextView) findViewById(R.id.numberOfPassengerInCurrentTrip);

            JoinARideDetailsAdapter joinARideDetailsAdapter = new JoinARideDetailsAdapter(object);
            String numberOfPassengers = String.valueOf(joinARideDetailsAdapter.getItemCount());
            currentNumberOfPeopleInTrip.setText(numberOfPassengers);

            joinARideDetailsRecyclerView = (RecyclerView) this.findViewById(R.id.listOfPassengersInCurrentTrip);
            joinARideDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            joinARideDetailsRecyclerView.setAdapter(joinARideDetailsAdapter);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        requestBtn = (Button) findViewById(R.id.requestToJoinRide);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(JoinARideDetails.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(JoinARideDetails.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(JoinARideDetails.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(JoinARideDetails.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                }

                try {

                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    {
                        Toast.makeText(JoinARideDetails.this, "Please enable Location and try again", Toast.LENGTH_SHORT).show();
                        return;

                    }else{

                        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        //Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        location_x = gpsLocation.getLatitude();
                        location_y = gpsLocation.getLongitude();

                        if(location_x == 0.0 || location_y == 0.0)
                        {
                            Toast.makeText(JoinARideDetails.this, "Location unavailable, please wait a few secconds", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.e("Location Error", "Failed to get location");
                }

                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject riderDetails = new JSONObject();
                        try {
                            riderDetails.put("user_id", parseInt(getIntent().getStringExtra("user_id")));
                            riderDetails.put("ride_id", parseInt(getIntent().getStringExtra("ride_id")));
                            riderDetails.put("current_location_x", Double.toString(location_x));
                            riderDetails.put("current_location_y", Double.toString(location_y));

                            NetworkConnection connect = new NetworkConnection();
                            JSONObject res = connect.postData(riderDetails, "requestToTagAlong");

                            if(res.has("message"))
                            {
                                goToSuccessJoinARidePage();
                            }else{
                                Log.e("Error", "Failed to request a ride");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
                newThread.start();
                try {
                    newThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    public void goBackToJoinARide(View view)
    {
        finish();
    }

    public void goToSuccessJoinARidePage()
    {
        Intent goToConfirmationOfRequestToJoin = new Intent(this, ConfirmationOfRequestToJoin.class);
        startActivity(goToConfirmationOfRequestToJoin);
        finish();
    }

}