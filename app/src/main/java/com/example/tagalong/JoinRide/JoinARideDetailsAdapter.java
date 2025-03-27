package com.example.tagalong.JoinRide;

import com.example.tagalong.R;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class JoinARideDetailsAdapter extends RecyclerView.Adapter<JoinARideDetailsAdapter.JoinARideDetailsViewHolder> {

    String[] passengers = { "Rafe Aaron", "Braxton Killmonger", "Ritah Stone"};
    String[] ageOfPassengers = {"43", "26", "27"};

    String[] genderOfPassengers = {"Male", "Male", "Female"};

    public static class JoinARideDetailsViewHolder extends RecyclerView.ViewHolder
    {

        TextView nameOfPassenger;
        TextView ageOfPassenger;
        ImageView passengerGender;

        public JoinARideDetailsViewHolder(View view)
        {
            super(view);

            this.nameOfPassenger = (TextView) view.findViewById(R.id.nameOfPassengerAbstract);
            this.ageOfPassenger = (TextView) view.findViewById(R.id.ageOfPassengerAbstract);
            this.passengerGender = (ImageView)view.findViewById(R.id.genderOfPassengerAbstract);
        }

    }

    @NonNull
    @Override
    public JoinARideDetailsAdapter.JoinARideDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View finalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_abstract, parent, false);
        return new JoinARideDetailsViewHolder(finalView);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinARideDetailsAdapter.JoinARideDetailsViewHolder holder, int position) {

        if(this.genderOfPassengers[position] == "Male")
        {
            holder.passengerGender.setImageResource(R.drawable.ic_profile_icon);
            holder.nameOfPassenger.setText(this.passengers[position]);
            holder.ageOfPassenger.setText(this.ageOfPassengers[position]);
        }
        else {
            holder.passengerGender.setImageResource(R.drawable.ic_female_icon);
            holder.nameOfPassenger.setText(this.passengers[position]);
            holder.ageOfPassenger.setText(this.ageOfPassengers[position]);
        }

    }

    @Override
    public int getItemCount() {
        return passengers.length;
    }
}
