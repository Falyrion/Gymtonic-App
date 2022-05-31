package com.falyrion.gymtonicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;
import com.falyrion.gymtonicapp.data.DatabaseHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Activity_EditBodyStats extends AppCompatActivity {

    private String date;
    private double[] data;
    private boolean savePossible = false;

    private Button saveButton;


    private class textWatcher implements TextWatcher {
        private int id;
        private textWatcher(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // pass
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // pass
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Update value
            data[id] = Double.parseDouble(editable.toString());

            // Update background resource of save button
            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            savePossible = true;
        }
    }

    private String convertDataToText(double value) {
        // Convert given double to string.
        if (value % 1 == 0) {
            // -> Value has only .0 decimals. Cut it out by converting to int.
            return String.valueOf((int) value);
        } else {
            // -> Value has decimals. Round up to 2 decimal-digits.
            DecimalFormat df = new DecimalFormat("#####.##");
            return String.valueOf(df.format(value));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_body_stats);

        // Get current date
        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        if (getIntent().hasExtra("dataBody")) {
            data = intent.getDoubleArrayExtra("dataBody");
        } else {
            data = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        }

        // -----------------------------------------------------------------------------------------

        // Set up edit-texts
        EditText editTextWeight = findViewById(R.id.editTextWeight);
        editTextWeight.addTextChangedListener(new textWatcher(0));

        EditText editTextChest = findViewById(R.id.editTextChest);
        editTextChest.addTextChangedListener(new textWatcher(1));

        EditText editTextBelly = findViewById(R.id.editTextBelly);
        editTextBelly.addTextChangedListener(new textWatcher(2));

        EditText editTextButt = findViewById(R.id.editTextButt);
        editTextButt.addTextChangedListener(new textWatcher(3));

        EditText editTextWaist = findViewById(R.id.editTextWaist);
        editTextWaist.addTextChangedListener(new textWatcher(4));

        EditText editTextArmRight = findViewById(R.id.editTextArmRight);
        editTextArmRight.addTextChangedListener(new textWatcher(5));

        EditText editTextArmLeft = findViewById(R.id.editTextArmLeft);
        editTextArmLeft.addTextChangedListener(new textWatcher(6));

        EditText editTextLegRight = findViewById(R.id.editTextThighRight);
        editTextLegRight.addTextChangedListener(new textWatcher(7));

        EditText editTextLegLeft = findViewById(R.id.editTextThighLeft);
        editTextLegLeft.addTextChangedListener(new textWatcher(8));

        // Update UI
        editTextWeight.setHint(convertDataToText(data[0]));
        editTextChest.setHint(convertDataToText(data[1]));
        editTextBelly.setHint(convertDataToText(data[2]));
        editTextButt.setHint(convertDataToText(data[3]));
        editTextWaist.setHint(convertDataToText(data[4]));
        editTextArmRight.setHint(convertDataToText(data[5]));
        editTextArmLeft.setHint(convertDataToText(data[6]));
        editTextLegRight.setHint(convertDataToText(data[7]));
        editTextLegLeft.setHint(convertDataToText(data[8]));

        // -----------------------------------------------------------------------------------------

        // Set up toolbar
        Toolbar toolbarActivityAddStats = (Toolbar) findViewById(R.id.toolbarActivityAddStats);
        toolbarActivityAddStats.setTitle("Edit todays stats");
        setSupportActionBar(toolbarActivityAddStats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set up date-text
        TextView textViewDate = findViewById(R.id.textViewEditStatsDate);
        textViewDate.setText(date);

        // Set up buttons
        saveButton = findViewById(R.id.buttonSaveBodyStats);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;

                    // Save data to database
                    DatabaseHelper databaseHelper = new DatabaseHelper(Activity_EditBodyStats.this);
                    databaseHelper.addDataBody(date, data[0] , data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
                    databaseHelper.close();

                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                }


            }
        });

        Button cancelButton = findViewById(R.id.buttonCancelSaveBodyStats);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Main.class);
                intent.putExtra("date", date);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = getIntent();
//        intent.putExtra("date", date);
//
//        super.onBackPressed();
//    }

}