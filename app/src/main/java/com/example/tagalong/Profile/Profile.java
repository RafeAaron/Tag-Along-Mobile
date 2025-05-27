package com.example.tagalong.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    TextView name;
    TextView userName;
    TextView age;
    TextView gender;

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

        name = (TextView) findViewById(R.id.profileName);
        userName = (TextView) findViewById(R.id.usernameProfile);
        age = (TextView) findViewById(R.id.userAge);
        gender = (TextView) findViewById(R.id.userGender);

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("user"));
            name.setText(jsonObject.get("first_name").toString() + " " + jsonObject.get("last_name").toString());
            userName.setText(jsonObject.get("user_name").toString());
            age.setText(jsonObject.get("age").toString());
            gender.setText(jsonObject.get("gender").toString());

        } catch (JSONException e) {
            Toast.makeText(this, "Failed to parse JSON Data", Toast.LENGTH_SHORT).show();
        }

    }

    public void goBack(View view)
    {
        finish();
    }
}