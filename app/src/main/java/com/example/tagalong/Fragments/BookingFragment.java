package com.example.tagalong.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.BookingsRecyclerViewAdapter;
import com.example.tagalong.R;

public class BookingFragment extends Fragment {

    Context parentContext;

    public BookingFragment(Context context)
    {
        this.parentContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        BookingsRecyclerViewAdapter myAdapter = new BookingsRecyclerViewAdapter(parentContext);
        TextView bookingslistHeading = (TextView) view.findViewById(R.id.bookingsAndNumber);

        if(myAdapter.getItemCount() < 1)
        {
            TextView defaultText = (TextView) view.findViewById(R.id.defaultTextBookings);
            defaultText.setVisibility(View.VISIBLE);
            bookingslistHeading.setText("Bookings (0)");

        }else {

            bookingslistHeading.setText("Bookings (" + myAdapter.getItemCount() + ")");

            RecyclerView myRecyclerView = view.findViewById(R.id.recyclerViewForBookingsList);
            myRecyclerView.setLayoutManager(new LinearLayoutManager(parentContext));
            myRecyclerView.setAdapter(myAdapter);
        }

        return view;
    }
}
