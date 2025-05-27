package com.example.tagalong.OnBoarding;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.example.tagalong.home.HomeMain;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnBoardingScreenTwo extends AppCompatActivity {

    EditText firstName;
    EditText lastName;

    EditText age;
    EditText gender;
    RadioGroup userRole;

    String confirmedUserRole = "";
    String lastNameConfirmed;
    String firstNameConfirmed;

    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_boarding_screen_two);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        userRole = (RadioGroup) findViewById(R.id.userRole);
        lastName = (EditText) findViewById(R.id.onboardingScreenTwoLastName);
        firstName = (EditText) findViewById(R.id.onboardingScreenTwoFirstName);
        age = (EditText) findViewById(R.id.onboardingScreenTwoAge);
        gender = (EditText) findViewById(R.id.onboardingScreenTwoGender);


        finish = (Button) findViewById(R.id.onboardingScreenTwoFinish);

        Intent incomingIntent = getIntent();
        Bundle extraBundle = incomingIntent.getExtras();

        String username = extraBundle.getString("name");
        String email = extraBundle.getString("email");
        String password = extraBundle.getString("password");

        userRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.driver)
                {
                    confirmedUserRole = "Driver";
                }

                if(checkedId == R.id.traveller)
                {
                    confirmedUserRole = "Traveller";
                }

                if(checkedId == R.id.both)
                {
                    confirmedUserRole = "Traveller/Driver";
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastNameConfirmed = lastName.getText().toString();
                firstNameConfirmed = firstName.getText().toString();

                /*
                Use the following to sign up a user. If it succeeds go to the home page with their details
                username -> string of desired username
                email -> string of desired email
                password -> string of desired password
                confirmedUserRole -> string of desired user role
                lastNameConfirmed -> string of confirmed last name
                firstNameComfirmed -> string of confirmed first name
                 */

                NetworkConnection connection = new NetworkConnection();

                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                if(connection.checkConnection())
                {

                        if(lastNameConfirmed.isEmpty() || firstNameConfirmed.isEmpty())
                        {

                            OnBoardingScreenTwo.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnBoardingScreenTwo.this, "Please fill in both the last name and the first name", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }else if(confirmedUserRole.length() == 0){

                            OnBoardingScreenTwo.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnBoardingScreenTwo.this, "User Role Not Confirmed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            return;

                        }else {

                            try {
                                JSONObject json = new JSONObject();
                                json.put("user_name", username);
                                json.put("first_name", firstNameConfirmed);
                                json.put("last_name", lastNameConfirmed);
                                json.put("email", email);
                                json.put("password", password);
                                json.put("role", confirmedUserRole);
                                json.put("age", age.getText().toString());
                                json.put("gender", gender.getText().toString());

                                JSONObject response = connection.postData(json, "addUser");

                                if (response.has("User_ID")) {
                                    int userid = response.getInt("User_ID");

                                    JSONObject emailCredentiials = new JSONObject();
                                    emailCredentiials.put("user_email_address", email);
                                    emailCredentiials.put("user_name", username);

                                    if (Objects.equals(confirmedUserRole, "Driver")) {

                                        JSONObject accountInfo = new JSONObject();
                                        accountInfo.put("user_id", userid);

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        String currentDateAndTime = sdf.format(new Date());

                                        accountInfo.put("date", currentDateAndTime);

                                        JSONObject json4 = connection.postData(accountInfo, "addUserAccount");

                                        if(!json4.has("Account")) {

                                            Log.e("Error", "Account Creation Failed");

                                        }

                                        goToDriverOnBoardingPage(Integer.toString(userid));

                                    } else {

                                        JSONObject jsonUser = new JSONObject();
                                        jsonUser.put("user_id", userid);
                                        JSONObject response2 = connection.getData(jsonUser, "getUser");

                                        if(response2.has("User"))
                                        {
                                            Log.i("Success", "I am now here");

                                            JSONObject accountInfo = new JSONObject();
                                            accountInfo.put("user_id", userid);

                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                            String currentDateAndTime = sdf.format(new Date());

                                            accountInfo.put("date", currentDateAndTime);

                                            JSONObject json4 =connection.postData(accountInfo, "addUserAccount");

                                            if(json4.has("Account")) {

                                                goToHomePage(response2.toString());
                                            }
                                        }else {
                                            OnBoardingScreenTwo.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(OnBoardingScreenTwo.this, "There was an error authenticating the driver", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }

                                    JSONObject responseEmail = connection.postData(emailCredentiials, "sendWelcomeEmail");

                                    if(responseEmail.has("Message"))
                                    {
                                        Log.i("Email Success", "Welcome to the family");
                                    }else
                                    {
                                        Log.e("Failure", "There was a problem with the email system");
                                    }

                                }else if(response.has("Error"))
                                {
                                    Toast.makeText(OnBoardingScreenTwo.this, "Email Already Taken", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Log.e("Sign Up Error", "This email is already taken");
                                    OnBoardingScreenTwo.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(OnBoardingScreenTwo.this, "This email is already taken", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return;
                                }
                            } catch (JSONException ex) {
                                Log.e("Error", "There was a JSON related error: " + ex);

                                OnBoardingScreenTwo.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(OnBoardingScreenTwo.this, "There was a JSON related error: " + ex.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                }
                }

                });

                newThread.start();

            }
        });
    }

    public void goToHomePage(String userId)
    {
        Intent goToHomePage = new Intent(this, HomeMain.class);
        goToHomePage.putExtra("User ID", userId);
        startActivity(goToHomePage);
        finish();
    }

    public void goToDriverOnBoardingPage(String userId)
    {
        Intent goToDriverOnBoarding = new Intent(this, DriverOnBoarding.class);
        goToDriverOnBoarding.putExtra("User ID", userId);
        startActivity(goToDriverOnBoarding);
        finish();
    }
}