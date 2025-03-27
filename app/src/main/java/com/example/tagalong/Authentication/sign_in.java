package com.example.tagalong.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;
import com.example.tagalong.home.HomeMain;

public class sign_in extends AppCompatActivity {

    EditText userName;
    EditText password;

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
    }

    public void goToHomePage(View view)
    {
        Intent goToHomePage = new Intent(this, HomeMain.class);
        goToHomePage.putExtra("User ID", 1);
        startActivity(goToHomePage);
        finish();
    }

    public void goToSignUpPage(View view)
    {
        Intent goToSignUp = new Intent(this, SignUp.class);
        startActivity(goToSignUp);
    }

    public void goToResetEmailPage(View view)
    {
        Intent goToResetPassword = new Intent(this, ResetPasswordCode.class);
        startActivity(goToResetPassword);
    }
}