package com.falyrion.gymtonicapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;


public class Fragment_Settings extends Fragment implements AdapterView.OnItemSelectedListener {

    private double[] dataGoals;
    private String[] languages;
    private String currentLanguage;
    private boolean savePossible = false;
    private boolean firstSelect = true;

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
            enableSaveButton();
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

    private void enableSaveButton() {
        saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
        saveButton.setTextColor(getContext().getColor(R.color.text_high));
        saveButton.setVisibility(View.VISIBLE);
        savePossible = true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load data from database
        Cursor cursorGoals = ((Activity_Main) requireContext()).databaseHelper.getSettingsGoals();
        if (cursorGoals.getCount() > 0) {
            cursorGoals.moveToFirst();
            dataGoals = new double[] {
                    cursorGoals.getDouble(0),
                    cursorGoals.getDouble(1),
                    cursorGoals.getDouble(2),
                    cursorGoals.getDouble(3)
            };
        } else {
            dataGoals = new double[] {0, 0, 0, 0};
        }
        cursorGoals.close();

        // Languages
        languages = new String[] {
                getResources().getString(R.string.lang_de),
                getResources().getString(R.string.lang_en)
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Nutrition goal settings
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


        // Language settings spinner
        Spinner spinner = getView().findViewById(R.id.spinnerLanguages);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterCategories = new ArrayAdapter(getContext(), R.layout.spinner_item_purple_middle, languages);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategories);

        // Button
        saveButton = getView().findViewById(R.id.buttonSaveSettings);
        saveButton.setVisibility(View.INVISIBLE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;
                    saveButton.setBackgroundResource(R.drawable.shape_box_round_middle);
                    saveButton.setTextColor(getContext().getColor(R.color.text_middle));
                    saveButton.setVisibility(View.INVISIBLE);

                    ((Activity_Main) requireContext()).databaseHelper.setSettingsGoals(dataGoals[0], dataGoals[1], dataGoals[2], dataGoals[3]);
                    ((Activity_Main) requireContext()).databaseHelper.setSettingsLanguage(currentLanguage);

                }
            }
        });
    }

    // Methods from imported spinner interface -----------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // Guard clause against the automatic item select on creation
        if (firstSelect) {
            firstSelect = false;
            return;
        }

        // Set value
        switch (position) {
            case 0: currentLanguage = "de"; break;
            case 1: currentLanguage = "en"; break;
        }

        // Update button
        enableSaveButton();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
    }
}