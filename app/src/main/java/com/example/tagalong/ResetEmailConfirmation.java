package com.example.tagalong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResetEmailConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_code_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reset_code_confirm_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent goToSignIn = new Intent(this, sign_in.class);

        Handler swicthPages = new Handler();
        swicthPages.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(goToSignIn);
                finish();
            }
        }, 2000);
    }

}
