package com.example.tagalong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

public class HomeMain extends AppCompatActivity {

    FrameLayout frame;
    int currentFragment = 1;
    int userId;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        frame = findViewById(R.id.fragmentHolder);
        userId = getIntent().getIntExtra("User ID", 0);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentHolder, new HomeFragment()).commit();
    }

    public void chooseFragment(View view)
    {


        if(view.getId() == R.id.homeIcon && currentFragment != 1)
        {
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, new HomeFragment()).commit();
            currentFragment = 1;
        }

        else if (view.getId() == R.id.verificationIcon && currentFragment != 2) {
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, new VerificationFragment(this)).commit();
            currentFragment = 2;
        }

        else if (view.getId() == R.id.paymentIcon && currentFragment != 3) {
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, new PaymentFragment()).commit();
            currentFragment = 3;
        }
    }

    public void goToVerificationSuccess(View view)
    {
        Intent goToVerificationSuccessActivity = new Intent(this, VerificationSuccess.class);
        startActivity(goToVerificationSuccessActivity);
    }

    public void goToPaymentInitialization(View view)
    {
        Intent goToPaymentInitializationPage = new Intent(this, PaymentInitialization.class);
        startActivity(goToPaymentInitializationPage);
    }
}