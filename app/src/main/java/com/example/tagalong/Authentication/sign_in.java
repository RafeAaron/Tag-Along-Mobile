package com.example.tagalong.Authentication;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.OnBoarding.OnboardingScreenOne;
import com.example.tagalong.R;
import com.example.tagalong.home.HomeMain;

import org.json.JSONException;
import org.json.JSONObject;

public class sign_in extends AppCompatActivity {

    NetworkConnection connection;

    EditText userName;
    EditText password;
    Button signInBtn;

    TextView errorUsername;
    TextView errorPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.signInPassword);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        errorUsername = (TextView) findViewById(R.id.usernameErrorMessage);
        errorPassword = (TextView) findViewById(R.id.passwordErrorMessage);

        errorUsername.setVisibility(INVISIBLE);
        errorPassword.setVisibility(INVISIBLE);

        if(userName.isFocused())
        {
            errorUsername.setVisibility(INVISIBLE);
        }

        if(password.isFocused())
        {
            errorPassword.setVisibility(INVISIBLE);
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                errorUsername.setVisibility(INVISIBLE);
                errorPassword.setVisibility(INVISIBLE);

                /*
                Logic to handle the sign in process
                use:
                    errorUsername to handle username related errors
                    errorPassword to handle password related errors
                    userName to obtain the username
                    password to obtain the entered password
                 */

                //Handling empty inputs
                if(userName.getText().toString().isEmpty())
                {
                    errorUsername.setText("Please provide a username");
                    errorUsername.setVisibility(VISIBLE);
                }

                if(password.getText().toString().isEmpty())
                {
                    errorPassword.setText("Please enter password");
                    errorPassword.setVisibility(VISIBLE);
                }

                if(!userName.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
                {
                    Thread checkDetails = new Thread(new Runnable() {
                        @Override
                        public void run() {


                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("user_name", userName.getText().toString());
                                jsonObject.put("password", password.getText().toString());

                                connection = new NetworkConnection();

                                if(!(connection.checkConnection()))
                                {
                                    sign_in.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(sign_in.this, "Server is currently unavailable", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else
                                {
                                    JSONObject response = connection.postData(jsonObject, "verifyUser");

                                    if(response.has("User"))
                                    {
                                        goToHomePage(response.toString());
                                    }else {
                                        sign_in.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(sign_in.this, "User not recognised, try different credentials", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }


                            }
                            catch (JSONException ex)
                            {
                                sign_in.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(sign_in.this, "Failed to parse input: " + ex, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                    checkDetails.start();

                }

            }
        });
    }

    public void goToHomePage(String userID)
    {
        Intent goToHomePage = new Intent(this, HomeMain.class);
        goToHomePage.putExtra("User ID", userID);
        startActivity(goToHomePage);
        finish();
    }

    public void goToOnBoardingPage(View view)
    {
        Intent goToOnBoarding = new Intent(this, OnboardingScreenOne.class);
        startActivity(goToOnBoarding);
    }

    public void goToResetEmailPage(View view)
    {
        Intent goToResetPassword = new Intent(this, ResetPasswordCode.class);
        startActivity(goToResetPassword);
    }
}