package com.example.tagalong.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tagalong.JoinRide.JoinARideDetails;
import com.example.tagalong.R;
import com.example.tagalong.home.CurrentTripsAdapter;

public class HomeFragment extends Fragment {

    Context parentContext;
    View layout;

    public HomeFragment(Context context) {
        // Required empty public constructor
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
        View endLayout = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView homeRecyclerView = (RecyclerView) endLayout.findViewById(R.id.currentTripsInProgressList);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this.parentContext));

        CurrentTripsAdapter myCurrentTripsAdapter = new CurrentTripsAdapter();
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

        return endLayout;
    }

    public View getView()
    {
        return this.layout;
    }
}