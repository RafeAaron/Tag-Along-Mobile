package com.example.tagalong.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.R;
import com.example.tagalong.payments.PaymentAdapter;

public class CurrentTripsAdapter extends RecyclerView.Adapter<CurrentTripsAdapter.CurrentTripsViewHolder> {

    String[] names = {"Rafe Aaron", "Micheal John", "Blake Diana", "Green Holms", "Rochelle Green"};
    String[] passengers = {"3", "2", "2", "1", "5"};

    String[] start = {"Ruharo", "Kashanyarazi", "Mbarara Town", "Mbarara Hospital", "Rwebikona"};

    String[] stop = {"Boma", "Taso Village", "Rwebikona", "Mbarara Town", "Mbarara Town"};

    String[] gender = {"Male", "Male", "Female", "Male", "Female"};

    View.OnClickListener onclickListener;

    public static class CurrentTripsViewHolder extends RecyclerView.ViewHolder{


        TextView driverAndPassengers;
        TextView route;
        ImageView gender;

        public CurrentTripsViewHolder(View view) {
            super(view);

            driverAndPassengers = view.findViewById(R.id.driverDetailsAndPassengers);
            route = view.findViewById(R.id.routeToTake);
            gender = view.findViewById(R.id.icon_gender_trip_details);
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
        view.setOnClickListener(onclickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrentTripsViewHolder holder, int position) {

        String driverAndNumberOfPassengers = "";
        String routeTaken = "";

        if(gender[position] == "Male")
        {

            driverAndNumberOfPassengers += "Mr. " + names[position] + " (" + passengers[position] + " passengers )";
            routeTaken += start[position] + " -> " + stop[position];

            holder.getDriverAndPassengers().setText(driverAndNumberOfPassengers);
            holder.getRoute().setText(routeTaken);
            holder.getGender().setImageResource(R.drawable.ic_profile_icon);

        }else {
            driverAndNumberOfPassengers += "Mrs. " + names[position] + " (" + passengers[position] + " passengers )";
            routeTaken += start[position] + " -> " + stop[position];

            holder.getDriverAndPassengers().setText(driverAndNumberOfPassengers);
            holder.getRoute().setText(routeTaken);
            holder.getGender().setImageResource(R.drawable.ic_female_icon);
        }

    }

    @Override
    public int getItemCount() {
        return names.length;
    }


}
