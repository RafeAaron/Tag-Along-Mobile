package com.example.tagalong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.Authentication.sign_in;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    NetworkConnection connectionToServer;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        continueBtn = (Button) findViewById(R.id.btnMain);

        Thread internetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                connectionToServer = new NetworkConnection();
                boolean serverPresence = connectionToServer.checkConnection();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(serverPresence)
                        {
                            continueBtn.setEnabled(true);
                        }else{
                            Toast.makeText(MainActivity.this, "Can't establish connection to server", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        internetThread.start();
    }

    public void displayConnectionSuccess()
    {
        Toast.makeText(this, "Connection successful", Toast.LENGTH_SHORT).show();
    }

    public void nextPage(View view)
    {
        Intent signInIntent = new Intent(this, sign_in.class);
        startActivity(signInIntent);
        finish();
    }
}