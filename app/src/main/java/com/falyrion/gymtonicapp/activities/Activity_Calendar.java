package com.falyrion.gymtonicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_Calendar extends AppCompatActivity {

    /**
     * This activity is called by pressing the calendar-button in the Fragment_Nutrition. It will display
     * a calendar to choose a date and return this new date to the main activity.
     */

    private String date;
    private String newDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Get current date
        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }
        newDate = date;

        // Set up toolbar
        Toolbar toolbarActivityAddStats = findViewById(R.id.toolbarActivityCalendar);
        toolbarActivityAddStats.setTitle("Change date");
        setSupportActionBar(toolbarActivityAddStats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Buttons
        Button buttonConfirm = findViewById(R.id.buttonConfirmSetDate);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Main.class);
                intent.putExtra("date", newDate);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });

        Button buttonCancel = findViewById(R.id.buttonCancelSetDate);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Main.class);
                intent.putExtra("date", date);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });

        // Set up calendar item
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Convert selected date to format "dd-mm-yyyy"
                String dayString = String.valueOf(day);
                if (day < 10) {
                    dayString = "0" + dayString;
                }

                String monthString = String.valueOf(month + 1);  // month + 1 because january is 0
                if ((month + 1) < 10) {
                    monthString = "0" + monthString;
                }

                newDate = dayString + "-" + monthString + "-" + String.valueOf(year);

                buttonConfirm.setBackgroundResource(R.drawable.shape_box_round_pop);
            }
        });
    }
}