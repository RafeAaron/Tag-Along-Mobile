package com.example.tagalong.BookARide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;

public class BookARide extends AppCompatActivity {

    CalendarView calendar;

    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_aride);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendar = (CalendarView) findViewById(R.id.bookingCalender);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                date = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(dayOfMonth);
            }
        });
    }

    public void goBackToHomeFromBookARide(View view)
    {
        finish();
    }

    public void goToBookingConfirmation(View view)
    {
        Intent myIntent = new Intent(this, BookARideConfirmation.class);
        startActivity(myIntent);
        finish();
    }
}