package com.example.tagalong.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;
import com.google.android.material.snackbar.Snackbar;

public class ResetPasswordCode extends AppCompatActivity {

    EditText resetEmail;
    Button sendEmailCode;

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
        sendEmailCode = (Button) findViewById(R.id.resetCodeBtn);
        sendEmailCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Logic to handle the process of sending an email to reset the password
                use:
                    resetEmail to request the email to send to the reset code
                 */

                Snackbar.make(ResetPasswordCode.this, findViewById(R.id.reset_code_main), "Email Sent", 3000).show();

                goToEmailConfirmation();

            }
        });
    }

    public void goToEmailConfirmation()
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
