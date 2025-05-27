package com.example.tagalong.Verification;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class VerificationSuccess extends AppCompatActivity {

    TextView carModel;
    TextView carColor;
    TextView carType;
    TextView carNumberPlate;

    TextView driverName;
    TextView driverContact;
    TextView headingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.verification_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verificationConfirmationMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        carModel = (TextView) findViewById(R.id.car_model);
        carColor = (TextView) findViewById(R.id.car_color);
        carType = (TextView) findViewById(R.id.car_type);
        carNumberPlate = (TextView) findViewById(R.id.car_number_plate);
        driverContact = (TextView) findViewById(R.id.driver_contact);
        headingName = (TextView) findViewById(R.id.verificationConfirmationHeading);

        try {

            JSONObject obje = new JSONObject(getIntent().getExtras().getString("car_information"));
            JSONObject obje2 = new JSONObject(getIntent().getExtras().getString("user_information"));

            carModel.setText("Car Model: " + obje.get("car_model"));
            carColor.setText("Car Color: " + obje.get("color"));
            carType.setText("Car Type: " + obje.get("type"));
            carNumberPlate.setText("Car Number Plate: " + obje.get("number_plate"));

            if(Objects.equals(obje2.get("gender"), "Male"))
            {
                headingName.setText("Mr. " + obje2.get("first_name") + " " + obje2.get("last_name"));
            }else{
                headingName.setText("Mrs. " + obje2.get("first_name") + " " + obje2.get("last_name"));
            }

            driverContact.setText("Username: " + obje2.get("user_name"));


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public void goToHomePageFromVerification(View view)
    {
        finish();
    }
}
