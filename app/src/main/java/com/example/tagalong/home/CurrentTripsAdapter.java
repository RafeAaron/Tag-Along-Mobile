package com.example.tagalong.home;

import static androidx.core.content.ContextCompat.getSystemService;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.example.tagalong.StartRide.StartARide;
import com.example.tagalong.payments.PaymentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;

public class CurrentTripsAdapter extends RecyclerView.Adapter<CurrentTripsAdapter.CurrentTripsViewHolder> {

    String[] names = new String[0];
    String[] passengers = new String[0];

    String[] distance = new String[0];
    String[] gender = new String[0];

    LocationManager lcManager;

    View.OnClickListener onclickListener;

    Context context;

    public CurrentTripsAdapter(JSONObject object, LocationManager lcManager, Context context)
    {

        Log.e("Error", object.toString());

        this.lcManager = lcManager;
        this.context = context;

        try {
            distance = new String[object.getJSONObject("Passengers").length()];
            names = new String[object.getJSONObject("Passengers").length()];
            passengers = new String[object.getJSONObject("Passengers").length()];
            gender = new String[object.getJSONObject("Passengers").length()];
        } catch (JSONException e) {
            Log.e("Error", "There was a problem parsing the JSON" + e.toString());
        }

        try {
            Log.i("Error", object.getJSONObject("Passengers").getJSONObject("" + 0).toString());
        } catch (JSONException e) {
            Log.e("Error", "Failed");
        }

        if(object.has("Passengers")) {

            Thread newThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    NetworkConnection conn = new NetworkConnection();
                    try {

                    for(int a = 0; a < object.getJSONObject("Passengers").length(); a++)
                    {
                        JSONObject req = new JSONObject();


                            req.put("user_id", parseInt(object.getJSONObject("Passengers").getJSONObject("" + a).get("user_id").toString()));
                            JSONObject res = conn.getData(req, "getUser");



                            names[a] = res.getJSONObject("User").get("first_name").toString() + " " + res.getJSONObject("User").get("last_name").toString();
                            passengers[a] = res.getJSONObject("User").get("age").toString();
                            gender[a] = res.getJSONObject("User").get("gender").toString();
                            distance[a] = "5.78m"; //Float.toString(getDistance(Double.parseDouble(res.getJSONObject("User").get("current_location_x").toString()), Double.parseDouble(res.getJSONObject("User").get("current_location_y").toString()))) + "m";



                    }
                    } catch (JSONException e) {
                        Log.e("Error", "Failed to parse JSON data: " + e.toString());
                    }

                }
            });

            newThread.start();
            try {
                newThread.join();
            } catch (InterruptedException e) {
                Log.e("Error", "Failed to load passenger details");
            }
        }else{



        }

    }

    public static class CurrentTripsViewHolder extends RecyclerView.ViewHolder{


        TextView driverAndPassengers;
        TextView route;
        ImageView gender;

        public CurrentTripsViewHolder(View view) {
            super(view);

            driverAndPassengers = view.findViewById(R.id.driverDetailsAndPassengers);
            route = view.findViewById(R.id.routeToTake);
            gender = view.findViewById(R.id.genderOfPassengerAbstract);
        }

        public TextView getDriverAndPassengers()
        {
            return driverAndPassengers;
        }

        public TextView getRoute()
        {
            return route;
        }

        public ImageView getGender()
        {
            return gender;
        }
    }

    public void setonclickListener(View.OnClickListener onclickListener)
    {
        this.onclickListener = onclickListener;
    }

    @NonNull
    @Override
    public CurrentTripsViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_abstract, parent, false);

        CurrentTripsViewHolder viewHolder = new CurrentTripsViewHolder(view);

        viewHolder.gender =(ImageView) view.findViewById(R.id.genderOfTripAbstract);
        viewHolder.driverAndPassengers =(TextView) view.findViewById(R.id.driverDetailsAndPassengers);
        viewHolder.route =(TextView) view.findViewById(R.id.routeToTake);
        view.setOnClickListener(onclickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrentTripsViewHolder holder, int position) {

        String driverAndNumberOfPassengers = "";
        String routeTaken = "";

        if(gender[position] == "Male")
        {

            driverAndNumberOfPassengers += "Mr. " + names[position] + " (" + passengers[position] + ")";
            routeTaken += distance[position];

            holder.getDriverAndPassengers().setText(driverAndNumberOfPassengers);
            holder.getRoute().setText(routeTaken);
            //holder.getGender().setImageResource(R.drawable.ic_profile_icon);
            holder.gender.setImageResource(R.drawable.ic_profile_icon);

        }else {
            driverAndNumberOfPassengers += "Mrs. " + names[position] + " (" + passengers[position] + ")";
            routeTaken += distance[position];

            holder.getDriverAndPassengers().setText(driverAndNumberOfPassengers);
            holder.getRoute().setText(routeTaken);
            //holder.getGender().setImageResource(R.drawable.ic_female_icon);
            holder.gender.setImageResource(R.drawable.ic_female_icon);
        }

    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public float getDistance(double locate_x, double locate_y) {

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this.context, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }

        try {

            if(!this.lcManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(this.context, "Please enable Location and try again", Toast.LENGTH_SHORT).show();
                return 0;

            }else{

                Location gpsLocation = this.lcManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                double location_x = gpsLocation.getLatitude();
                double location_y = gpsLocation.getLongitude();

                Location location = new Location("");
                location.setLatitude(location_x);
                location.setLongitude(location_y);

                Location location2 = new Location("");
                location.setLatitude(locate_x);
                location.setLongitude(locate_y);

                float distanceToLocation = location.distanceTo(location2);

                if(location_x == 0.0 || location_y == 0.0)
                {
                    Toast.makeText(this.context, "Location unavailable, please wait a few secconds", Toast.LENGTH_SHORT).show();
                    return 0;
                }

                return distanceToLocation;
            }
        } catch (Exception e) {
            Log.e("Location Error", "Failed to get location");
        }

        return 0;

    }
}
