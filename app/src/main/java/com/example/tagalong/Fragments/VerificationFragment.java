package com.example.tagalong.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.tagalong.DriverNotRegistered;
import com.example.tagalong.MainActivity;
import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.example.tagalong.Verification.VerificationSuccess;
import com.example.tagalong.home.HomeMain;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.tagalong.home.HomeMain;

public class VerificationFragment extends Fragment {

    Context mainActivity;

    EditText numberPlate;
    public VerificationFragment(Context mainActivity) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_verification, container, false);

        numberPlate = (EditText) view.findViewById(R.id.editTextVerification);
        Button btn = (Button) view.findViewById(R.id.btnVerification);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        NetworkConnection connection = new NetworkConnection();

                        if(connection.checkConnection())
                        {
                            JSONObject object = new JSONObject();
                            try {
                                object.put("number_plate", numberPlate.getText().toString());
                                JSONObject json = connection.getData(object, "getDriverInformation");

                                if(json.has("Driver"))
                                {
                                    Intent goToSuccessfulVerify = new Intent(mainActivity, VerificationSuccess.class);
                                    goToSuccessfulVerify.putExtra("car_information", json.get("Driver").toString());


                                    JSONObject userDetails = new JSONObject();
                                    userDetails.put("user_id", json.getJSONObject("Driver").get("user_id"));

                                    JSONObject res = connection.getData(userDetails, "getUser");

                                    if(res.has("User"))
                                    {
                                        goToSuccessfulVerify.putExtra("user_information", res.get("User").toString());
                                        startActivity(goToSuccessfulVerify);
                                    }
                                }else {


                                    Log.e("Error", "Driver not registered");
                                    goToErrorDriver();



                                }

                            } catch (JSONException e) {
                                Log.e("Error", "Failed to parse JSON: " + e.toString());
                            }

                        }

                    }
                });
                newThread.start();
                try {
                    newThread.join();
                } catch (InterruptedException e) {
                    Log.e("Error", "Failed to rejoin threads: " + e.toString());
                }

            }
        });

        return view;


    }

    public void goToErrorDriver()
    {
        Intent intent = new Intent(mainActivity, DriverNotRegistered.class);
        startActivity(intent);
    }
}