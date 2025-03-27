package com.example.tagalong.home;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.tagalong.BookARide.BookARide;
import com.example.tagalong.Fragments.BookingFragment;
import com.example.tagalong.Fragments.HomeFragment;
import com.example.tagalong.Fragments.MapFragment;
import com.example.tagalong.Fragments.PaymentFragment;
import com.example.tagalong.JoinRide.JoinRide;
import com.example.tagalong.Profile.Profile;
import com.example.tagalong.StartRide.StartARide;
import com.example.tagalong.payments.PaymentInitialization;
import com.example.tagalong.R;
import com.example.tagalong.Fragments.VerificationFragment;
import com.example.tagalong.Verification.VerificationSuccess;

public class HomeMain extends AppCompatActivity {

    FrameLayout frame;
    int currentFragment = 1;
    int userId;
    FragmentManager fragmentManager;
    HomeFragment homeFragment;

    private View homePage;

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

        this.homeFragment = new HomeFragment(this);
        fragmentManager.beginTransaction().replace(R.id.fragmentHolder, this.homeFragment).commit();
        this.homePage = homeFragment.getView();
    }

    public void chooseFragment(View view)
    {


        if(view.getId() == R.id.homeIcon && currentFragment != 1)
        {
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, homeFragment).commit();
            currentFragment = 1;
        }

        else if (view.getId() == R.id.verificationIcon && currentFragment != 2) {
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, new VerificationFragment(this)).commit();
            currentFragment = 2;
        }

        else if (view.getId() == R.id.mapIcon && currentFragment != 3) {

            MapFragment mapFragment = new MapFragment(this);

            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, mapFragment).commit();
            currentFragment = 3;
        }

        else if (view.getId() == R.id.paymentIcon && currentFragment != 4) {

            PaymentFragment paymentFragment = new PaymentFragment(this);

            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, paymentFragment).commit();
            currentFragment = 4;
        }

        else if (view.getId() == R.id.booking_icon && currentFragment != 5) {
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, new BookingFragment(this)).commit();
            currentFragment = 5;
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

    public void clickedProfileButton(View view)
    {
        //Action to be performed when the profile picture is clicked
        //Toast.makeText(this, "Profile Button Clicked", Toast.LENGTH_SHORT).show();

        Intent openProfilePage = new Intent(this, Profile.class);
        startActivity(openProfilePage);
    }

    public void goToStartARide(View view)
    {
        Intent goToStartARidePageIntent = new Intent(this, StartARide.class);
        startActivity(goToStartARidePageIntent);
    }

    public void goToJoinARide(View view)
    {
        Intent goToJoinARidePageIntent = new Intent(this, JoinRide.class);
        startActivity(goToJoinARidePageIntent);
        Toast.makeText(this, "To be implemented later", Toast.LENGTH_SHORT).show();
    }

    public void goToBookARide(View view)
    {
        Intent goToBookARidePageIntent = new Intent(this, BookARide.class);
        startActivity(goToBookARidePageIntent);
    }
}