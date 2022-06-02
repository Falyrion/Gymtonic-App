package com.falyrion.gymtonicapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.falyrion.gymtonicapp.R;
import com.falyrion.gymtonicapp.data.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class Activity_Meals_CreateEditPreset extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * This activity lets the user create new meal-presets or edit already existing ones.
     */

    private String mealName;
    private String mealUUID;
    private String[] mealCategories;
    private String selectedMealCategory;
    private double[] mealData = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private boolean savePossible = false;
    private String date;
    private String mode = "create";

    private Button saveButton;
    private Button cancelButton;
    private EditText editTextMealName;

    private DatabaseHelper databaseHelper;


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
            if (id == 0) {
                if (editable.toString().length() > 28) {
                    editTextMealName.setText(mealName);
                    Toast.makeText(getApplicationContext(), "Text limit reached!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mealName = editable.toString();
            } else {
                mealData[id - 1] = Double.parseDouble(editable.toString());
            }

            // Update background resource of save button
            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            saveButton.setTextColor(getColor(R.color.text_high));
            savePossible = true;
            cancelButton.setText(R.string.button_text_cancel);
        }
    }

    private String[] loadMealCategoriesFromDatabase() {
        Cursor cursorCat = databaseHelper.getPresetMealCategories();
        String[] loadedCategories = new String[0];

        if (cursorCat.getCount() > 0) {
            loadedCategories = new String[cursorCat.getCount()];
            int i = 0;
            while (cursorCat.moveToNext()) {
                loadedCategories[i] = cursorCat.getString(0);
                i++;
            }
        }
        cursorCat.close();

        return loadedCategories;
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
        setContentView(R.layout.activity_meals_createeditpreset);

        // Connect to database
        databaseHelper = new DatabaseHelper(Activity_Meals_CreateEditPreset.this);

        // Set up spinner for categories -----------------------------------------------------------
        mealCategories = loadMealCategoriesFromDatabase();
        selectedMealCategory = mealCategories[0];

        Spinner spinner = findViewById(R.id.spinnerMealCategory);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterCategories = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item_purple_middle, mealCategories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategories);

        // -----------------------------------------------------------------------------------------
        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        }

        if (getIntent().hasExtra("mode")) {
            if (intent.getStringExtra("mode").equals("edit")) {
                mode = "edit";

                String uuid = intent.getStringExtra("uuid");
                Cursor cursorData = databaseHelper.getPresetMealDetails(uuid);
                if (cursorData.getCount() > 0) {
                    cursorData.moveToFirst();
                    mealUUID = cursorData.getString(0);
                    mealName = cursorData.getString(1);
                    selectedMealCategory = cursorData.getString(2);
                    mealData = new double[]{
                            cursorData.getDouble(3), cursorData.getDouble(4),
                            cursorData.getDouble(5), cursorData.getDouble(6), cursorData.getDouble(7), cursorData.getDouble(8),
                            cursorData.getDouble(9), cursorData.getDouble(10), cursorData.getDouble(11), cursorData.getDouble(12),
                            cursorData.getDouble(13), cursorData.getDouble(14), cursorData.getDouble(15), cursorData.getDouble(16),
                            cursorData.getDouble(17), cursorData.getDouble(18), cursorData.getDouble(19), cursorData.getDouble(20),
                            cursorData.getDouble(21), cursorData.getDouble(22), cursorData.getDouble(23), cursorData.getDouble(24),
                            cursorData.getDouble(25), cursorData.getDouble(26), cursorData.getDouble(27), cursorData.getDouble(28),
                            cursorData.getDouble(29), cursorData.getDouble(30), cursorData.getDouble(31), cursorData.getDouble(32),
                            cursorData.getDouble(33)
                    };
                }
                cursorData.close();

                // Set spinner item
                List<String> mealsList = Arrays.asList(mealCategories);
                spinner.setSelection(mealsList.indexOf(selectedMealCategory));
            }
        }

        // -----------------------------------------------------------------------------------------
        // Set up edit-texts
        editTextMealName = findViewById(R.id.editTextMealName);
        if (mealName != null) {
            editTextMealName.setText(mealName);
        }
        editTextMealName.addTextChangedListener(new textWatcher(0));

        // TODO: Add new edit texts for new items
        EditText editTextCalories = findViewById(R.id.editTextCalories);
        editTextCalories.setHint(convertDataToText(mealData[0]));
        editTextCalories.addTextChangedListener(new textWatcher(1));

        EditText editTextFat = findViewById(R.id.editTextFat);
        editTextFat.setHint(convertDataToText(mealData[1]));
        editTextFat.addTextChangedListener(new textWatcher(2));

        EditText editTexFatSat = findViewById(R.id.editTextFatSat);
        editTexFatSat.setHint(convertDataToText(mealData[2]));
        editTexFatSat.addTextChangedListener(new textWatcher(3));

        EditText editTextCarbs = findViewById(R.id.editTextCarbs);
        editTextCarbs.setHint(convertDataToText(mealData[3]));
        editTextCarbs.addTextChangedListener(new textWatcher(4));

        EditText editTextSugar = findViewById(R.id.editTextSugar);
        editTextSugar.setHint(convertDataToText(mealData[4]));
        editTextSugar.addTextChangedListener(new textWatcher(5));

        EditText editTextProtein = findViewById(R.id.editTextProtein);
        editTextProtein.setHint(convertDataToText(mealData[5]));
        editTextProtein.addTextChangedListener(new textWatcher(6));

        EditText editTextSalt = findViewById(R.id.editTextSalt);
        editTextSalt.setHint(convertDataToText(mealData[6]));
        editTextSalt.addTextChangedListener(new textWatcher(7));

        EditText editTextFiber = findViewById(R.id.editTextFiber);
        editTextFiber.setHint(convertDataToText(mealData[7]));
        editTextFiber.addTextChangedListener(new textWatcher(8));

        EditText editTextChol = findViewById(R.id.editTextCholesterin);
        editTextChol.setHint(convertDataToText(mealData[8]));
        editTextChol.addTextChangedListener(new textWatcher(9));

        EditText editTextCreatine = findViewById(R.id.editTextCreatine);
        editTextCreatine.setHint(convertDataToText(mealData[9]));
        editTextCreatine.addTextChangedListener(new textWatcher(10));

        EditText editTextCa = findViewById(R.id.editTextCa);
        editTextCa.setHint(convertDataToText(mealData[10]));
        editTextCa.addTextChangedListener(new textWatcher(11));

        EditText editTextFe = findViewById(R.id.editTextFe);
        editTextFe.setHint(convertDataToText(mealData[11]));
        editTextFe.addTextChangedListener(new textWatcher(12));

        EditText editTextK = findViewById(R.id.editTextKa);
        editTextK.setHint(convertDataToText(mealData[12]));
        editTextK.addTextChangedListener(new textWatcher(13));

        EditText editTextMg = findViewById(R.id.editTextMg);
        editTextMg.setHint(convertDataToText(mealData[13]));
        editTextMg.addTextChangedListener(new textWatcher(14));

        EditText editTextMn = findViewById(R.id.editTextMn);
        editTextMn.setHint(convertDataToText(mealData[14]));
        editTextMn.addTextChangedListener(new textWatcher(15));

        EditText editTextNa = findViewById(R.id.editTextNa);
        editTextNa.setHint(convertDataToText(mealData[15]));
        editTextNa.addTextChangedListener(new textWatcher(16));

        EditText editTextP = findViewById(R.id.editTextP);
        editTextP.setHint(convertDataToText(mealData[16]));
        editTextP.addTextChangedListener(new textWatcher(17));

        EditText editTextZn = findViewById(R.id.editTextZn);
        editTextZn.setHint(convertDataToText(mealData[17]));
        editTextZn.addTextChangedListener(new textWatcher(18));

        EditText editTextVitA = findViewById(R.id.editTextVitA);
        editTextVitA.setHint(convertDataToText(mealData[18]));
        editTextVitA.addTextChangedListener(new textWatcher(19));

        EditText editTextVitB1 = findViewById(R.id.editTextVitB1);
        editTextVitB1.setHint(convertDataToText(mealData[19]));
        editTextVitB1.addTextChangedListener(new textWatcher(20));

        EditText editTextVitB2 = findViewById(R.id.editTextVitB2);
        editTextVitB2.setHint(convertDataToText(mealData[20]));
        editTextVitB2.addTextChangedListener(new textWatcher(21));

        EditText editTextVitB3 = findViewById(R.id.editTextVitB3);
        editTextVitB3.setHint(convertDataToText(mealData[21]));
        editTextVitB3.addTextChangedListener(new textWatcher(22));

        EditText editTextVitB5 = findViewById(R.id.editTextVitB5);
        editTextVitB5.setHint(convertDataToText(mealData[22]));
        editTextVitB5.addTextChangedListener(new textWatcher(23));

        EditText editTextVitB6 = findViewById(R.id.editTextVitB6);
        editTextVitB6.setHint(convertDataToText(mealData[23]));
        editTextVitB6.addTextChangedListener(new textWatcher(24));

        EditText editTextVitB7 = findViewById(R.id.editTextVitB7);
        editTextVitB7.setHint(convertDataToText(mealData[24]));
        editTextVitB7.addTextChangedListener(new textWatcher(25));

        EditText editTextVitB11 = findViewById(R.id.editTextVitB11);
        editTextVitB11.setHint(convertDataToText(mealData[25]));
        editTextVitB11.addTextChangedListener(new textWatcher(26));

        EditText editTextVitB12 = findViewById(R.id.editTextVitB12);
        editTextVitB12.setHint(convertDataToText(mealData[26]));
        editTextVitB12.addTextChangedListener(new textWatcher(27));

        EditText editTextVitC = findViewById(R.id.editTextVitC);
        editTextVitC.setHint(convertDataToText(mealData[27]));
        editTextVitC.addTextChangedListener(new textWatcher(28));

        EditText editTextVitE = findViewById(R.id.editTextVitE);
        editTextVitE.setHint(convertDataToText(mealData[28]));
        editTextVitE.addTextChangedListener(new textWatcher(29));

        EditText editTextVitK = findViewById(R.id.editTextVitK);
        editTextVitK.setHint(convertDataToText(mealData[29]));
        editTextVitK.addTextChangedListener(new textWatcher(30));

        EditText editTextVitH = findViewById(R.id.editTextVitH);
        editTextVitH.setHint(convertDataToText(mealData[30]));
        editTextVitH.addTextChangedListener(new textWatcher(31));

        // -----------------------------------------------------------------------------------------
        // Set up toolbar
        Toolbar toolbarActivityCreateMeal = (Toolbar) findViewById(R.id.toolbarActivityCreateMeal);
        if (mode.equals("create")) {
            toolbarActivityCreateMeal.setTitle(getResources().getString(R.string.dn_button_add));
        } else if (mode.equals("edit")) {
            toolbarActivityCreateMeal.setTitle(getResources().getString(R.string.dn_button_edit));
        }

        setSupportActionBar(toolbarActivityCreateMeal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // -----------------------------------------------------------------------------------------
        // Set up buttons
        saveButton = findViewById(R.id.buttonSaveNewMeal);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    if (mealName == null) {
                        // If meal name was not set up yet remind user to add one
                        Toast.makeText(getApplicationContext(), "Please enter a name first!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    savePossible = false;

                    // If UUID does not exist, create one
                    if (mealUUID == null) {
                        // Get only first 8 digits from UUID (no need for long "123e4567-e89b-42d3-a456-556642440000")
                        mealUUID = UUID.randomUUID().toString().substring(0, 8);
                    }


                    // Save data to database
                    databaseHelper.addOrReplacePresetMeal(mealUUID, mealName, selectedMealCategory, mealData);
                    databaseHelper.close();

                    // Change button color
                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                    saveButton.setTextColor(getColor(R.color.text_middle));
                    cancelButton.setText(R.string.button_text_back);
                }
            }
        });

        cancelButton = findViewById(R.id.buttonCancelNewMeal);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Meals_AddDailyEntry.class);
                if (date != null) {
                    intent.putExtra("date", date);
                }
                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    // Methods from imported spinner interface -----------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedMealCategory = mealCategories[position];
        savePossible = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
    }
}