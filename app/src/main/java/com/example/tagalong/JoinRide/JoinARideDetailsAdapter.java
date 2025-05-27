package com.example.tagalong.JoinRide;

import com.example.tagalong.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class JoinARideDetailsAdapter extends RecyclerView.Adapter<JoinARideDetailsAdapter.JoinARideDetailsViewHolder> {

    String[] passengers;
    String[] ageOfPassengers;

    String[] genderOfPassengers;
    int number = 0;

    public JoinARideDetailsAdapter(JSONObject object)
    {

        if(Objects.equals("{}", object.toString())){
            this.passengers = new String[0];
            this.ageOfPassengers = new String[0];
            this.genderOfPassengers = new String[0];
        }else
        {
            this.passengers = new String[object.names().length()];
            this.ageOfPassengers = new String[object.names().length()];
            this.genderOfPassengers = new String[object.names().length()];

            for(int i = 0; i < object.names().length(); i++)
            {
                try {

                    Log.i("Success", object.get("" + i).toString());
                    JSONObject detail = new JSONObject(object.get("" + i).toString());

                    this.passengers[i] = detail.getString("first_name") + " " + detail.getString("last_name");
                    this.ageOfPassengers[i] = detail.get("age").toString();
                    this.genderOfPassengers[i] = detail.getString("gender");
                    this.number++;

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

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

        if(Objects.equals(genderOfPassengers[position], "Male"))
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
        return this.number;
    }
}
