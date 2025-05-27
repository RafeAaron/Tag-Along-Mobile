package com.example.tagalong.OnBoarding;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.Authentication.sign_in;
import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.example.tagalong.home.HomeMain;

import org.json.JSONException;
import org.json.JSONObject;

public class DriverOnBoarding extends AppCompatActivity {

    EditText carModel;
    EditText carType;
    EditText carColor;
    EditText carNumberPlate;

    Button continueToHomePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_on_boarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        carModel = (EditText) findViewById(R.id.driverOnBoardingCarModel);
        carType = (EditText) findViewById(R.id.driverOnBoardingCarType);
        carColor = (EditText) findViewById(R.id.driverOnBoardingCarColor);
        carNumberPlate = (EditText) findViewById(R.id.driverOnBoardingNumberPlate);

        continueToHomePage = (Button) findViewById(R.id.driverOnBoardingFinish);

        continueToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carModelFinal = carModel.getText().toString();
                String carTypeFinal = carType.getText().toString();
                String carColorFinal = carColor.getText().toString();
                String carNumberPlateFinal = carNumberPlate.getText().toString();
                String userID = getIntent().getExtras().get("User ID").toString();

                /*
                    Use the above variables to sign up a driver
                 */

                NetworkConnection newConnection = new NetworkConnection();

                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (newConnection.checkConnection()) {
                            try {

                                JSONObject json = new JSONObject();
                                json.put("car_model", carModelFinal);
                                json.put("type", carTypeFinal);
                                json.put("color", carColorFinal);
                                json.put("number_plate", carNumberPlateFinal);
                                json.put("user_id", parseInt(userID));

                                JSONObject jsonObject = newConnection.postData(json, "make_driver");

                                if (jsonObject.has("driver_id")) {

                                    DriverOnBoarding.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DriverOnBoarding.this, "Driver Registration Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Log.i("Register Driver Success", "User is now registered as a driver. ID: " + jsonObject.get("driver_id"));

                                    JSONObject jsonUser = new JSONObject();
                                    jsonUser.put("user_id", parseInt(userID));
                                    JSONObject response = newConnection.getData(jsonUser, "getUser");

                                    if(response.has("User"))
                                    {
                                        goToHome(response.toString());
                                    }else {
                                        DriverOnBoarding.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DriverOnBoarding.this, "User not recognised, try different credentials", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }


                                } else {
                                    Log.e("Register Driver Error", "There was an error registering the driver");

                                    DriverOnBoarding.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DriverOnBoarding.this, "There was an error registering the driver", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }


                            } catch (JSONException jsex) {
                                Log.e("JSONError", "There was an error parsing the JSON data");

                                DriverOnBoarding.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DriverOnBoarding.this, "There was an error parsing JSON Data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                    }

                });

                newThread.start();

            }
        });

    }

    public void goToHome(String userId)
    {
        Intent goToHomePage = new Intent(this, HomeMain.class);
        goToHomePage.putExtra("User ID", userId);
        startActivity(goToHomePage);
        finish();
    }
}