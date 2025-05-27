package com.example.tagalong.Authentication;

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

import com.example.tagalong.OnBoarding.OnBoardingScreenTwo;
import com.example.tagalong.R;
import com.example.tagalong.home.HomeMain;

public class SignUp  extends AppCompatActivity {

    EditText userName;
    EditText userEmail;
    EditText userPassword;
    EditText userConfirmedPassword;

    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = (EditText) findViewById(R.id.usernameSignUp);
        userEmail = (EditText) findViewById(R.id.signUpEmail);
        userPassword = (EditText) findViewById(R.id.signUpPassword);
        userConfirmedPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        signUpBtn = (Button) findViewById(R.id.signUnBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Logic to handle the sign up process
                use:
                    userName to handle the user's desired username
                    userEmail to handle the user's email
                    userPassword to handle the user's password
                    userConfirmedPassword to handle the user's confirmed password
                 */

                if(userPassword.getText().toString().length() < 8)
                {
                    Log.e("Sign Up Error", "Password must be 8 characters or more");
                    Toast.makeText(SignUp.this, "Password must be 8 characters or more", Toast.LENGTH_SHORT).show();
                }else if(!userPassword.getText().toString().contentEquals(userConfirmedPassword.getText().toString()))
                {
                    Log.e("Sign Up Error", "Password must match Confirmed Password");
                    Toast.makeText(SignUp.this, "Password must match Confirmed Password", Toast.LENGTH_SHORT).show();
                }else if(userName.getText().toString().isEmpty() || userEmail.getText().toString().isEmpty() || userPassword.getText().toString().isEmpty())
                {
                    Log.e("Sign Up Error", "Please enter all relevant details");
                    Toast.makeText(SignUp.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }else{
                    goToNextPage(userName.getText().toString(), userEmail.getText().toString(), userPassword.getText().toString());
                }
            }
        });

    }

    public void goToSignIn(View view)
    {
        Intent goToSignIn = new Intent(this, sign_in.class);
        startActivity(goToSignIn);
        finish();
    }

    public void goToNextPage(String name, String email, String password)
    {
        Intent goToNextIntent = new Intent(this, OnBoardingScreenTwo.class);
        goToNextIntent.putExtra("name", name);
        goToNextIntent.putExtra("email", email);
        goToNextIntent.putExtra("password", password);
        startActivity(goToNextIntent);
        finish();
    }

}
