package com.example.tagalong.home;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.tagalong.BookARide.BookARide;
import com.example.tagalong.DriverNotRegistered;
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

import org.json.JSONException;
import org.json.JSONObject;

public class HomeMain extends AppCompatActivity {

    FrameLayout frame;
    int currentFragment = 1;
    String userInformation;
    FragmentManager fragmentManager;
    HomeFragment homeFragment;
    LocationManager lcManager;

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
        userInformation = getIntent().getStringExtra("User ID");
        fragmentManager = getSupportFragmentManager();

        lcManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            JSONObject userInfo = new JSONObject(this.userInformation);

            this.homeFragment = new HomeFragment(this, userInfo.getJSONObject("User").get("first_name") + " " + userInfo.getJSONObject("User").get("last_name"), userInfo, this.lcManager);
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, this.homeFragment).commit();
            this.homePage = homeFragment.getView();

        } catch (JSONException e) {
            Toast.makeText(this, "There was an error parsing the input: " + e, Toast.LENGTH_SHORT).show();
        }
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
        /*
        else if (view.getId() == R.id.mapIcon && currentFragment != 3) {

            MapFragment mapFragment = new MapFragment(this);

            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, mapFragment).commit();
            currentFragment = 3;
        }*/

        else if (view.getId() == R.id.paymentIcon && currentFragment != 4) {

            try {
                JSONObject userInfo = new JSONObject(this.userInformation);
                PaymentFragment paymentFragment = new PaymentFragment(this, userInfo);

                fragmentManager.beginTransaction().replace(R.id.fragmentHolder, paymentFragment).commit();
                currentFragment = 4;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


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

        try {
            JSONObject userInfo = new JSONObject(this.userInformation);
            goToPaymentInitializationPage.putExtra("userId", parseInt(userInfo.getJSONObject("User").get("id").toString()));
            goToPaymentInitializationPage.putExtra("user", userInfo.getJSONObject("User").toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to parse JSON Data", Toast.LENGTH_SHORT).show();
        }

        startActivity(goToPaymentInitializationPage);
    }

    public void clickedProfileButton(View view)
    {
        //Action to be performed when the profile picture is clicked
        //Toast.makeText(this, "Profile Button Clicked", Toast.LENGTH_SHORT).show();

        JSONObject userInfo = null;
        try {
            userInfo = new JSONObject(this.userInformation);
            Intent openProfilePage = new Intent(this, Profile.class);
            openProfilePage.putExtra("user", userInfo.getJSONObject("User").toString());
            startActivity(openProfilePage);
        } catch (JSONException e) {
            Toast.makeText(this, "There was an error parsing JSON Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToStartARide(View view)
    {
        Intent goToStartARidePageIntent = new Intent(this, StartARide.class);
        goToStartARidePageIntent.putExtra("userInfo", this.userInformation);
        startActivity(goToStartARidePageIntent);
    }

    public void goToErrorDriver(View view)
    {
        Intent goToErrorDriver = new Intent(this, DriverNotRegistered.class);
        startActivity(goToErrorDriver);
    }

    public void goToJoinARide(View view)
    {
        Intent goToJoinARidePageIntent = new Intent(this, JoinRide.class);
        try {
            JSONObject userInfo = new JSONObject(this.userInformation);
            goToJoinARidePageIntent.putExtra("userId", userInfo.getJSONObject("User").get("id").toString());
            startActivity(goToJoinARidePageIntent);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Toast.makeText(this, "To be implemented later", Toast.LENGTH_SHORT).show();
    }

    public void goToBookARide(View view) {
        Intent goToBookARidePageIntent = new Intent(this, BookARide.class);

        try {
            JSONObject userInfo = new JSONObject(this.userInformation);
            goToBookARidePageIntent.putExtra("userId", userInfo.getJSONObject("User").get("id").toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        startActivity(goToBookARidePageIntent);
    }
}