package com.example.tagalong.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goBack(View view)
    {
        finish();
    }
    public void updateProfile(View view)
    {

        //Action to be performed when update profile button has been pressed
        Intent updateProfile = new Intent(this, ProfileChangeConfirmation.class);
        startActivity(updateProfile);

    }
}