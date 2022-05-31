package com.falyrion.gymtonicapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;


public class Fragment_Settings extends Fragment {

    private double[] dataGoals;
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
            dataGoals[id] = Double.parseDouble(editable.toString());

            // Update background resource of save button
            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            savePossible = true;
        }
    }

    private String convertDataToText(double value) {
        // Convert given double to string.
        // Check if double value has ".0" decimals. If yes cut it out.
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load data from database
        Cursor cursor = ((Activity_Main) requireContext()).databaseHelper.getSettingsGoals();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            dataGoals = new double[] {
                    cursor.getDouble(0),
                    cursor.getDouble(1),
                    cursor.getDouble(2),
                    cursor.getDouble(3)
            };
        } else {
            dataGoals = new double[] {0, 0, 0, 0};
        }
        cursor.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        EditText editTextCalories = getView().findViewById(R.id.editTextSettingsGoalsCal);
        editTextCalories.setText(convertDataToText(dataGoals[0]));
        editTextCalories.addTextChangedListener(new textWatcher(0));

        EditText editTextFat = getView().findViewById(R.id.editTextSettingsGoalsFat);
        editTextFat.setText(convertDataToText(dataGoals[1]));
        editTextFat.addTextChangedListener(new textWatcher(1));

        EditText editTextCarbs = getView().findViewById(R.id.editTextSettingsGoalsCarbs);
        editTextCarbs.setText(convertDataToText(dataGoals[2]));
        editTextCarbs.addTextChangedListener(new textWatcher(2));

        EditText editTextProtein = getView().findViewById(R.id.editTextSettingsGoalsProtein);
        editTextProtein.setText(convertDataToText(dataGoals[3]));
        editTextProtein.addTextChangedListener(new textWatcher(3));

        saveButton = getView().findViewById(R.id.buttonSaveSettings);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;
                    saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);

                    ((Activity_Main) requireContext()).databaseHelper.setSettingsGoals(dataGoals[0], dataGoals[1], dataGoals[2], dataGoals[3]);
                }
            }
        });
    }
}