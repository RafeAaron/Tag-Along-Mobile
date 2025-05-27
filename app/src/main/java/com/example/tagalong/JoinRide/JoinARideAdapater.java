package com.example.tagalong.JoinRide;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.R;
import com.example.tagalong.home.CurrentTripsAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Objects;

public class JoinARideAdapater extends RecyclerView.Adapter<JoinARideAdapater.JoinARideViewHolder> {

    String[] namesOfDrivers = {"Rafe Aaron", "Ralph Monopoly", "Gracious Nominus", "Blue Whales"};
    String[] numberOfPassengers = {"4", "1", "4", "3"};
    String[] startLocation = {"TASO Village", "Ruharo", "Kashanyarazi", "Mbarara Town"};
    String[] endLocation = {"Ruharo", "Mbarara Town Council", "Ruharo", "Kakoba"};

    String[] genders;
    Context parentContext;

    int[] ids;

    private static int number = 0;


    public JoinARideAdapater(Context context, String[] driversNames, String[] startLocations, String[] endLocations, String[] numberOfPassengers, int[] ids, String[] gender)
    {
        this.parentContext = context;
        this.namesOfDrivers = driversNames;
        this.startLocation = startLocations;
        this.endLocation = endLocations;
        this.numberOfPassengers = numberOfPassengers;
        this.ids = ids;
        this.genders = gender;

        number = 0;
    }

    View.OnClickListener viewOnClickListener;

    public void setViewOnCLickListener(View.OnClickListener onViewClickListener)
    {
        this.viewOnClickListener = onViewClickListener;
    }

    @NonNull
    @Override
    public JoinARideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_abstract, parent, false);

        JoinARideAdapater.JoinARideViewHolder viewHolder = new JoinARideAdapater.JoinARideViewHolder(view);
        viewHolder.setParentContext(this.parentContext);
        viewHolder.setItemOnClickListener(viewOnClickListener);

        JSONObject jsonOB = new JSONObject();
        try {
            jsonOB.put("id", ids[number]);
            view.setTag(jsonOB);
            number++;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JoinARideViewHolder holder, int position) {

        String driverAndNumberOfPassengers = "";
        String routeTaken = "";


        driverAndNumberOfPassengers += namesOfDrivers[position] + " (" + numberOfPassengers[position] + " passengers )";
        routeTaken += startLocation[position] + " -> " + endLocation[position];

        holder.getDriverAndPassengers().setText(driverAndNumberOfPassengers);
        holder.getRoute().setText(routeTaken);

        //Log.e("Success", genders[position]);

        if(Objects.equals(genders[position], "Male")) {
            holder.getGender().setImageResource(R.drawable.ic_profile_icon);

        }else {
            holder.getGender().setImageResource(R.drawable.ic_female_icon);
        }

    }

    @Override
    public int getItemCount() {
        return namesOfDrivers.length;
    }

    public static class JoinARideViewHolder extends RecyclerView.ViewHolder
    {

        TextView driverAndPassengers;
        TextView routeDetails;
        ImageView genderImage;

        Context parentContextViewHolder;

        View myView;

     public JoinARideViewHolder(View view)
     {
         super(view);
         this.driverAndPassengers = view.findViewById(R.id.driverDetailsAndPassengers);
         this.routeDetails = view.findViewById(R.id.routeToTake);
         this.genderImage = view.findViewById(R.id.genderOfTripAbstract);

         this.myView = view;
     }

     public void setItemOnClickListener(View.OnClickListener onClickListener)
     {

         myView.setOnClickListener(onClickListener);
     }

     public void setParentContext(Context context)
     {
         this.parentContextViewHolder = context;
     }

        public TextView getDriverAndPassengers()
        {
            return this.driverAndPassengers;
        }

        public TextView getRoute()
        {
            return this.routeDetails;
        }

        public ImageView getGender()
        {
            return this.genderImage;
        }
    }
}
