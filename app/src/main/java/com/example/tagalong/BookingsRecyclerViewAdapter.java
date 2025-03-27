package com.example.tagalong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingsRecyclerViewAdapter extends RecyclerView.Adapter<BookingsRecyclerViewAdapter.BookingsViewHolder> {

    Context parentContext;

    public BookingsRecyclerViewAdapter(Context context)
    {
        this.parentContext = context;
    }

    String[] nameOfDriver = {"Rafe Aaron", "Micheal John", "Lennon Friedman"};

    String[] dates = {"21/10/2023", "04/09/2022", "06/10/2023"};
    String[] start = {"Ruharo", "Kashanyarazi", "Mbarara University"};

    String[] end = {"Mbarara Town", "Ruharo", "Rwebikona"};

    String[] timeAgreed = {"13:45 pm", "08: 30 pm", "17: 00 pm"};

    String[] feeAgreed = {"3479", "7389", "3780"};

    @NonNull
    @Override
    public BookingsRecyclerViewAdapter.BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_template, parent, false);
        return new BookingsViewHolder(view);
    }

    

    @Override
    public void onBindViewHolder(@NonNull BookingsRecyclerViewAdapter.BookingsViewHolder holder, int position) {

        holder.nameOfDriver.setText(nameOfDriver[position] + "(" + dates[position] + ")");
        holder.route.setText(start[position] + " -> " + end[position]);
        holder.timeAgreed.setText(timeAgreed[position]);
        holder.amount.setText("UGX " + feeAgreed[position]);

    }

    @Override
    public int getItemCount() {
        return nameOfDriver.length;
    }

    public static class BookingsViewHolder extends RecyclerView.ViewHolder
    {

        TextView nameOfDriver;
        TextView amount;
        TextView route;
        TextView timeAgreed;

        public BookingsViewHolder(View view)
        {
            super(view);

            this.nameOfDriver = (TextView) view.findViewById(R.id.nameOfDriverBooked);
            this.route = (TextView) view.findViewById(R.id.startAndEndDestinationBooked);
            this.amount = (TextView) view.findViewById(R.id.agreedFee);
            this.timeAgreed = (TextView) view.findViewById(R.id.timeBooked);
        }

    }
}
