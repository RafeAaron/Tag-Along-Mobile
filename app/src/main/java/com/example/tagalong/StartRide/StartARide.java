package com.example.tagalong.StartRide;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;

import org.w3c.dom.Text;

public class StartARide extends AppCompatActivity {

    RadioGroup seatsAvailable;
    EditText numberOfSeatsSpecified;
    TextView numberOfSeatsSpecifiedHeading;
    EditText startLocation;
    EditText destination;
    Button advertiseRide;
    int numberOfAvailableSeats = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_aride);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        seatsAvailable = (RadioGroup) findViewById(R.id.numberOfSeatsSelected);
        numberOfSeatsSpecified = (EditText) findViewById(R.id.numberOfSeatsSpecified);
        numberOfSeatsSpecifiedHeading = (TextView) findViewById(R.id.numberOfSeatsSpecifiedHeading);

        advertiseRide = (Button) findViewById(R.id.buttonToAdvertiseRide);

        startLocation = (EditText) findViewById(R.id.startPosition);
        destination = (EditText) findViewById(R.id.endPosition);

        numberOfSeatsSpecified.setVisibility(INVISIBLE);
        numberOfSeatsSpecifiedHeading.setVisibility(INVISIBLE);

        advertiseRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberOfAvailableSeats == 100) {
                    numberOfAvailableSeats = Integer.valueOf(numberOfSeatsSpecified.getText().toString());
                }

                /*
                Logic to handle advertisement of available seats.

                use:
                    startLocation for the starting point
                    endLocation for the ending point
                    numberOfAvailableSeats for the numberOfAvailable seats
                 */

                goToSuccessStartARidePage();

            }
        });

        seatsAvailable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                numberOfSeatsSpecified.setVisibility(INVISIBLE);
                numberOfSeatsSpecifiedHeading.setVisibility(INVISIBLE);

                if(checkedId == R.id.numberOfSeats1)
                {

                    numberOfAvailableSeats = 1;

                }else if(checkedId == R.id.numberOfSeats2)
                {
                    numberOfAvailableSeats = 2;
                }
                else if(checkedId == R.id.numberOfSeats3)
                {

                    numberOfAvailableSeats = 3;

                }
                else if(checkedId == R.id.numberOfSeats4)
                {

                    numberOfAvailableSeats = 4;

                }
                else if(checkedId == R.id.numberOfSeats5_)
                {
                    numberOfAvailableSeats = 100;

                    numberOfSeatsSpecified.setVisibility(VISIBLE);
                    numberOfSeatsSpecifiedHeading.setVisibility(VISIBLE);

                }
            }
        });

    }

    public void goBackStartARide(View view)
    {
        finish();
    }

    public void goToSuccessStartARidePage()
    {

        Intent goToSuccessPage = new Intent(this, StartARideConfirmationPage.class);
        startActivity(goToSuccessPage);
        finish();
    }
}