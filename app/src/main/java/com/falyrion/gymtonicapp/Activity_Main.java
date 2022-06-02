package com.falyrion.gymtonicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.falyrion.gymtonicapp.data.DatabaseHelper;
import com.falyrion.gymtonicapp.fragments.Fragment_Nutrition;
import com.falyrion.gymtonicapp.fragments.Fragment_Workout;
import com.falyrion.gymtonicapp.fragments.Fragment_Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_Main extends AppCompatActivity {

    public String date;
    private int currentFragmentID = 0;
    public DatabaseHelper databaseHelper;


    private void setFragmentFood(String date) {
        Fragment_Nutrition fragment = new Fragment_Nutrition();
        Bundle args = new Bundle();
        args.putString("date", date);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void setFragmentBodyStats() {
        // Pass
    }

    private void setFragmentWorkout() {
        Fragment_Workout fragment = new Fragment_Workout();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void setFragmentSettings() {
        Fragment_Settings fragment = new Fragment_Settings();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private static void updateLanguage(Context context, String language) {

        if (language.equals("system")) {
            return;
        }

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database
        databaseHelper = new DatabaseHelper(Activity_Main.this);

        // Update language
        Cursor cursor = databaseHelper.getSettingsLanguage();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            updateLanguage(this, cursor.getString(0));
        }
        cursor.close();

        // -----------------------------------------------------------------------------------------
        // Get data if activity was started by another activity
        Intent intent = getIntent();

        // Get current date
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        // If there is a fragmentID submitted take it. Else keep previously set or default one (=0)
        if (getIntent().hasExtra("fragmentID")) {
            currentFragmentID = intent.getIntExtra("fragmentID", 0);
        }

        // Set current fragment based on fragmentID
        switch (currentFragmentID) {
            case 0:
                setFragmentFood(date);
                break;

            case 1:
                setFragmentBodyStats();
                break;

            case 2:
                setFragmentWorkout();
                break;

            case 3:
                setFragmentSettings();
                break;

            default:
                break;
        }

        // -----------------------------------------------------------------------------------------
        // Setup navigation bar
        BottomNavigationView navBar = findViewById(R.id.bottom_navigation);
        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_bar_home:
                        if (currentFragmentID != 0) {
                            setFragmentFood(date);
                            currentFragmentID = 0;
                        }
                        return true;

                    case R.id.nav_bar_stats:
                        if (currentFragmentID != 1) {
                            setFragmentBodyStats();
                            currentFragmentID = 1;
                        }
                        return true;

                    case R.id.nav_bar_exersises:
                        if (currentFragmentID != 2) {
                            setFragmentWorkout();
                            currentFragmentID = 2;
                        }
                        return true;

                    case R.id.nav_bar_settings:
                        if (currentFragmentID != 3) {
                            setFragmentSettings();
                            currentFragmentID = 3;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

}