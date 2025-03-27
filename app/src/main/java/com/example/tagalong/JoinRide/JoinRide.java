package com.example.tagalong.JoinRide;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.R;

public class JoinRide extends AppCompatActivity {

    RecyclerView joinARideRecyclerView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_ride);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        JoinARideAdapater joinARideAdapter = new JoinARideAdapater(this);
        joinARideAdapter.setViewOnCLickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToDetailsPage = new Intent(context, JoinARideDetails.class);
                startActivity(goToDetailsPage);

                //Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show();
            }
        });


        joinARideRecyclerView = (RecyclerView) this.findViewById(R.id.joinARideRecyclerView);
        joinARideRecyclerView.setAdapter(joinARideAdapter);
        joinARideRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void goBackToHomePageFromJoinARide(View view)
    {
        finish();
    }
}