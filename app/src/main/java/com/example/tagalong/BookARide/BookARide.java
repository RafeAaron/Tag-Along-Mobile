package com.example.tagalong.BookARide;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class BookARide extends AppCompatActivity {

    CalendarView calendar;
    Button bookRide;

    EditText startDestination;
    EditText endDestination;

    TimePicker timePicker;

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

        bookRide = (Button) findViewById(R.id.bookARideBtn);
        calendar = (CalendarView) findViewById(R.id.bookingCalender);

        startDestination = (EditText) findViewById(R.id.startingDestination);
        endDestination = (EditText) findViewById(R.id.endingDestination);
        timePicker = (TimePicker) findViewById(R.id.timeText);



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                date = String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth);
                Log.e("Date", date);
                if(dateIsValid(String.valueOf(dayOfMonth), String.valueOf(month + 1), String.valueOf(year)) != 1)
                {
                    Toast.makeText(BookARide.this, "Date picked is invalid", Toast.LENGTH_SHORT).show();
                    date = "invalid";
                }
            }
        });

        bookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Objects.equals(date, "invalid") || Objects.equals(date, ""))
                {
                    Toast.makeText(BookARide.this,"Please choose a valid date", Toast.LENGTH_SHORT).show();
                }else{

                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("user_id", parseInt(getIntent().getStringExtra("userId")));
                        jsonObject.put("date", date);
                        jsonObject.put("start_location", startDestination.getText().toString());
                        jsonObject.put("end_location", endDestination.getText().toString());
                        jsonObject.put("time", timePicker.getHour() + ":" + timePicker.getMinute());

                        Thread newThread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                NetworkConnection connection = new NetworkConnection();
                                JSONObject res = connection.postData(jsonObject, "advertiseBooking");

                                if(res.has("errno"))
                                {
                                    Log.e("Error", "There was an error advertising your booking");
                                }else{
                                    goToBookingConfirmation();
                                }

                            }
                        });

                        newThread.start();
                        newThread.join();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }

            }
        });


    }

    public void goBackToHomeFromBookARide(View view)
    {
        finish();
    }

    public void goToBookingConfirmation()
    {
        Intent myIntent = new Intent(this, BookARideConfirmation.class);
        startActivity(myIntent);
        finish();
    }

    public int dateIsValid(String day, String month, String year)
    {

        int isValid = -1;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateAndTime = sdf.format(new Date());

        String currentDay = (String) currentDateAndTime.subSequence(0, 2);
        String currentMonth = (String) currentDateAndTime.subSequence(3, 5);
        String currentYear = (String) currentDateAndTime.substring(6, 10);

        Log.i("Day", currentDay + " " + day);
        Log.i("Month", currentMonth + " " + month);
        Log.i("Year", currentYear + " " + year);

        if(parseInt(currentDay) > parseInt(day))
        {
            isValid = -10;

            return isValid;

        }else if(parseInt(currentMonth) > parseInt(month))
        {

            isValid = -9;

        }else if(parseInt(year) < parseInt(currentYear))
        {
            isValid = -8;
        }

        else{
            isValid = 1;
            return isValid;
        }

        return isValid;

    }
}