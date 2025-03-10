package com.example.tagalong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResetPasswordCode extends AppCompatActivity {

    EditText resetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reset_code_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resetEmail = (EditText) findViewById(R.id.resetEmail);
    }

    public void goToEmailConfirmation(View view)
    {
        Intent goToEmailConfirmation = new Intent(this, ResetEmailConfirmation.class);
        startActivity(goToEmailConfirmation);
        finish();
    }

    public void goToSignIn(View view)
    {
        Intent goToSignInIntent = new Intent(this, sign_in.class);
        startActivity(goToSignInIntent);
        finish();
    }
}
