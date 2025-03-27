package com.example.tagalong.JoinRide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.R;

public class JoinARideDetails extends AppCompatActivity {

    RecyclerView joinARideDetailsRecyclerView;
    TextView currentNumberOfPeopleInTrip;

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

        currentNumberOfPeopleInTrip = (TextView) findViewById(R.id.numberOfPassengerInCurrentTrip);

        JoinARideDetailsAdapter joinARideDetailsAdapter = new JoinARideDetailsAdapter();
        String numberOfPassengers = String.valueOf(joinARideDetailsAdapter.getItemCount());
        currentNumberOfPeopleInTrip.setText(numberOfPassengers);

        joinARideDetailsRecyclerView = (RecyclerView) this.findViewById(R.id.listOfPassengersInCurrentTrip);
        joinARideDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        joinARideDetailsRecyclerView.setAdapter(joinARideDetailsAdapter);

    }

    public void goBackToJoinARide(View view)
    {
        finish();
    }

    public void goToSuccessJoinARidePage(View view)
    {
        Intent goToConfirmationOfRequestToJoin = new Intent(this, ConfirmationOfRequestToJoin.class);
        startActivity(goToConfirmationOfRequestToJoin);
        finish();
    }

}