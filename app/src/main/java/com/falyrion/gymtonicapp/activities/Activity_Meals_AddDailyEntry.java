package com.falyrion.gymtonicapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;
import com.falyrion.gymtonicapp.data.DatabaseHelper;
import com.falyrion.gymtonicapp.recyclerview.Adapter_MealPresets;
import com.falyrion.gymtonicapp.recyclerview.Item_MealPreset;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Activity_Meals_AddDailyEntry extends AppCompatActivity implements Adapter_MealPresets.mealPresetItemInterface, AdapterView.OnItemSelectedListener {

    /**
     * This activity displays all meal-presets and lets the user add them to the database
     * for the current day.
     */

    private String date;
    private boolean savePossible = false;

    private Button saveButton;
    private Button cancelButton;
    TextView noEntries;
    RecyclerView recyclerViewMeals;

    private String[] mealCategories;
    private int currentCategoryIndex = 0;
    private static final String allCategories = "All";
    private ArrayList<Item_MealPreset> mealsPresetList;
    private Adapter_MealPresets adapterPresets;

    private DatabaseHelper databaseHelper;

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

    private String[] loadMealCategoriesFromDatabase() {
        Cursor cursorCat = databaseHelper.getPresetMealCategories();
        String[] loadedCategories = new String[0];

        if (cursorCat.getCount() > 0) {
            loadedCategories = new String[cursorCat.getCount() + 1];
            loadedCategories[0] = allCategories;

            int i = 1;
            while (cursorCat.moveToNext()) {
                loadedCategories[i] = cursorCat.getString(0);
                i++;
            }
        }
        cursorCat.close();

        return loadedCategories;
    }

    private ArrayList<Item_MealPreset> loadPresetMealsFromDatabase(String category) {
        Cursor cursor;

        if (category.equals(allCategories)) {
            cursor = databaseHelper.getPresetMealsSimpleAllCategories();
        } else {
            cursor = databaseHelper.getPresetMealsSimpleFromCategory(category);
        }

        ArrayList<Item_MealPreset> loadedPresets = new ArrayList<Item_MealPreset>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // Create new Item_MealPreset and add it to meals-list
                loadedPresets.add(cursor.getPosition(), new Item_MealPreset(
                        cursor.getString(1),  // Title
                        cursor.getString(0),  // UUID
                        (int) cursor.getDouble(2),  // Calories
                        0  // Amount
                ));
            }
        }
        cursor.close();

        return loadedPresets;
    }


    // Overwrite class default methods -------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_adddailyentry);

        // Set loading-layout ----------------------------------------------------------------------
        FrameLayout loadingLayout = findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);

        // Get data from intent --------------------------------------------------------------------
        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        // Set up toolbar --------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbarActivityAddMeal);
        toolbar.setTitle(getResources().getString(R.string.dn_button_add));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set up categories spinner ---------------------------------------------------------------

        databaseHelper = new DatabaseHelper(Activity_Meals_AddDailyEntry.this);

        // Load categories from database
        mealCategories = loadMealCategoriesFromDatabase();

        // Set up spinner
        Spinner spinner = findViewById(R.id.spinnerMealPresetCategory);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterCategories = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item_purple_dark, mealCategories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategories);

        // Create meals for recycler view ----------------------------------------------------------

        // Load preset meals from database
        mealsPresetList = loadPresetMealsFromDatabase(mealCategories[currentCategoryIndex]);

        // Set adapters and recycler views
        adapterPresets = new Adapter_MealPresets(mealsPresetList, this, getApplicationContext());
        recyclerViewMeals = findViewById(R.id.recyclerViewMealsPreset);
        recyclerViewMeals.setAdapter(adapterPresets);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        // Update loading-layout -------------------------------------------------------------------
        noEntries = findViewById(R.id.textViewNoEntries);
        if (mealsPresetList.isEmpty()) {
            noEntries.setVisibility(View.VISIBLE);
        }

        loadingLayout.setVisibility(View.INVISIBLE);

        TextView textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(date);

        // Set up buttons --------------------------------------------------------------------------
        Button buttonAddMeal = findViewById(R.id.buttonAddMeal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start new activity Activity_CreateMeal
                Intent intent = new Intent(view.getContext(), Activity_Meals_CreateEditPreset.class);
                if (date != null) {
                    intent.putExtra("date", date);
                }
                startActivity(intent);
            }
        });

        saveButton = findViewById(R.id.buttonAddMealSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;

                    for (Item_MealPreset currentMeal : mealsPresetList) {
                        if (currentMeal.getAmount() > 0) {
                            databaseHelper.addOrReplaceConsumedMeal(date, currentMeal.getMealUUID(), currentMeal.getAmount());
                        }
                    }

                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                    cancelButton.setText("Back");
                    Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton = findViewById(R.id.buttonAddMealCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start new activity Activity_Main (= go back to main screen)
                Intent intent = new Intent(view.getContext(), Activity_Main.class);
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


    // Methods from imported interface -------------------------------------------------------------

    @Override
    public void updateItemAmount(int itemPosition, String mealUUID, double newAmount) {
        if (!savePossible) {
            savePossible = true;
            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            saveButton.setTextColor(getColor(R.color.text_high));
        }
        adapterPresets.notifyItemChanged(itemPosition);  // Update view
    }

    @Override
    public void onItemClick(String mealUUID) {
        // Start new activity Activity_CreateMeal
        Intent intent = new Intent(getApplicationContext(), Activity_Meals_CreateEditPreset.class);
        if (date != null) {
            intent.putExtra("date", date);
        }
        intent.putExtra("mode", "edit");
        intent.putExtra("uuid", mealUUID);
        startActivity(intent);
    }

    @Override
    public void onAmountClick(int itemPosition) {
        // Show dialog to edit amount
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextPlanName = view.findViewById(R.id.dialogEditText);
        if (mealsPresetList.get(itemPosition).getAmount() != 0) {
            editTextPlanName.setText(convertDataToText(mealsPresetList.get(itemPosition).getAmount()));
        }
        editTextPlanName.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        builder.setTitle("Change amount");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textInput = editTextPlanName.getText().toString();

                if (textInput.length() <= 0) {
                    return;
                }

                mealsPresetList.get(itemPosition).setAmount(Double.parseDouble(textInput));
                adapterPresets.notifyItemChanged(itemPosition);

                if (!savePossible) {
                    savePossible = true;
                    saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
                    saveButton.setTextColor(getColor(R.color.text_high));
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // Methods from imported spinner interface -----------------------------------------------------

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == currentCategoryIndex) {
            return;
        }

        currentCategoryIndex = position;

        // Clear data
        mealsPresetList.clear();
        adapterPresets.notifyDataSetChanged();

        // Load new data
        mealsPresetList = loadPresetMealsFromDatabase(mealCategories[position]);

        if (mealsPresetList.isEmpty()) {
            noEntries.setVisibility(View.VISIBLE);
            return;
        }
        noEntries.setVisibility(View.INVISIBLE);

        // Update recycler view
        adapterPresets = new Adapter_MealPresets(mealsPresetList, this, getApplicationContext());
        recyclerViewMeals = findViewById(R.id.recyclerViewMealsPreset);
        recyclerViewMeals.setAdapter(adapterPresets);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
    }
}