package com.falyrion.gymtonicapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;
import com.falyrion.gymtonicapp.data.DatabaseHelper;
import com.falyrion.gymtonicapp.recyclerview.Adapter_Workout_Plans;
import com.falyrion.gymtonicapp.recyclerview.Adapter_Workout_Routine;
import com.falyrion.gymtonicapp.recyclerview.Item_Workout_Plan;
import com.falyrion.gymtonicapp.recyclerview.Item_Workout_Routine;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Activity_Workout_CreateEditExercise extends AppCompatActivity implements Adapter_Workout_Routine.Interface_Workout_Routine, Adapter_Workout_Plans.Interface_Workout_Plans {

    /**
     * This activity lets the user create or edit exercises. Changes will be saved to the database.
     */

    private String mode = "create";
    private String date;

    private String oldPlanName;
    private String oldRoutineName;
    private String oldExerciseName;
    private String exerciseName;
    private int exerciseSets = 0;
    private int exerciseRepetitions = 0;
    private double exerciseWeight = 0;
    private boolean savePossible = false;

    private Button saveButton;
    private Button cancelButton;
    private Toolbar toolbarActivityCreateExercise;
    private RecyclerView recyclerViewRoutines;

    private ArrayList<Item_Workout_Routine> routinesList;
    private ArrayList<Item_Workout_Plan> plansList;
    private Adapter_Workout_Routine adapterRoutines;
    private Adapter_Workout_Plans adapterPlans;
    private int currentSelectedRoutineIdx;
    private int currentSelectedPlanIdx;

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
            switch (id) {
                case 0:
                    if (editable.toString().equals("")) {
                        exerciseName = "No name";
                    } else {
                        exerciseName = editable.toString();
                    }
                    break;

                case 1:
                    if (editable.toString().equals("")) {
                        exerciseSets = 0;
                    } else {
                        exerciseSets = Integer.parseInt(editable.toString());
                    }
                    break;

                case 2:
                    if (editable.toString().equals("")) {
                        exerciseRepetitions = 0;
                    } else {
                        exerciseRepetitions = Integer.parseInt(editable.toString());
                    }
                    break;

                case 3:
                    if (editable.toString().equals("")) {
                        exerciseWeight = 0;
                    } else {
                        exerciseWeight= Double.parseDouble(editable.toString());
                    }
                    break;
            }

            // Update background resource of save
            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            saveButton.setTextColor(getColor(R.color.text_high));
            savePossible = true;
        }
    }

    private void getPlansFromDatabase() {
        // Initiate list
        plansList = new ArrayList<Item_Workout_Plan>();

        // Get routines by plan
        Cursor cursor = databaseHelper.getWorkoutPlans();

        // Populate routines-list
        if (cursor.getCount() > 0) {
            // Add first item to list as "selected"
            cursor.moveToFirst();
            plansList.add(
                    cursor.getPosition(),
                    new Item_Workout_Plan(cursor.getString(0), true)
            );

            // Add remaining items to list as "not selected"
            while (cursor.moveToNext()) {
                plansList.add(
                        cursor.getPosition(),
                        new Item_Workout_Plan(cursor.getString(0), false)
                );
            }

            // Set up adapter
            adapterPlans = new Adapter_Workout_Plans(plansList, this);
            currentSelectedPlanIdx = 0;
        }
        cursor.close();
    }

    private void getRoutinesFromDatabase(String workoutPlanName) {
        // Initiate list
        routinesList = new ArrayList<Item_Workout_Routine>();

        // Get routines by plan
        Cursor cursor = databaseHelper.getWorkoutRoutines(workoutPlanName);

        // Populate routines-list
        if (cursor.getCount() > 0) {
            // Add first item to list as "selected"
            cursor.moveToFirst();
            routinesList.add(
                    cursor.getPosition(),
                    new Item_Workout_Routine(cursor.getString(0), true)
            );

            // Add remaining items to list as "not selected"
            while (cursor.moveToNext()) {
                routinesList.add(
                        cursor.getPosition(),
                        new Item_Workout_Routine(cursor.getString(0), false)
                );
            }

            // Set up adapter
            adapterRoutines = new Adapter_Workout_Routine(routinesList, this);
            currentSelectedRoutineIdx = 0;
        }
        cursor.close();
    }

    public void updateSelectedPlan(int newSelectedIndex) {
        // Update new selected item
        plansList.get(newSelectedIndex).setIsSelected(true);
        adapterPlans.notifyItemChanged(newSelectedIndex);

        // Update previous selected item
        plansList.get(currentSelectedPlanIdx).setIsSelected(false);
        adapterPlans.notifyItemChanged(currentSelectedPlanIdx);

        // Update variable
        currentSelectedPlanIdx = newSelectedIndex;
    }

    public void updateSelectedRoutine(int newSelectedIndex) {
        // Update new selected item
        routinesList.get(newSelectedIndex).setIsSelected(true);
        adapterRoutines.notifyItemChanged(newSelectedIndex);

        // Update previous selected item
        routinesList.get(currentSelectedRoutineIdx).setIsSelected(false);
        adapterRoutines.notifyItemChanged(currentSelectedRoutineIdx);

        // Update variable
        currentSelectedRoutineIdx = newSelectedIndex;
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


    // Class overwrite methods ---------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_createeditexercise);

        // -----------------------------------------------------------------------------------------
        Intent intent = getIntent();

        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        // Database --------------------------------------------------------------------------------
        databaseHelper = new DatabaseHelper(Activity_Workout_CreateEditExercise.this);

        // Get plans from database
        getPlansFromDatabase();

        // Get routines from database
        getRoutinesFromDatabase(plansList.get(0).getTitle());

        // Set up recycler-view
        recyclerViewRoutines = findViewById(R.id.recyclerViewRoutines);
        recyclerViewRoutines.setAdapter(adapterRoutines);
        recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        RecyclerView recyclerViewPlans = findViewById(R.id.recyclerViewPlans);
        recyclerViewPlans.setAdapter(adapterPlans);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        // Check mode the activity was started in --------------------------------------------------

        // -> Overwrite default mode if available
        if (getIntent().hasExtra("mode")) {
            if (getIntent().getStringExtra("mode").equals("edit")) {
                mode = "edit";

                // Overwrite variables
                oldPlanName = getIntent().getStringExtra("planName");
                oldRoutineName = getIntent().getStringExtra("routineName");
                exerciseName = getIntent().getStringExtra("exerciseName");
                oldExerciseName = exerciseName;
                exerciseSets = getIntent().getIntExtra("exerciseSets", 0);
                exerciseRepetitions = getIntent().getIntExtra("exerciseReps", 0);
                exerciseWeight = getIntent().getDoubleExtra("exerciseWeight", 0);

                // Get index of plan of current exercise
                int newSelectedPlanIndex = -1;
                for (Item_Workout_Plan plan : plansList) {
                    newSelectedPlanIndex++;
                    if (plan.getTitle().equals(oldPlanName)) {
                        break;
                    }
                }

                // Get index of routine of current exercise
                int newSelectedRoutineIndex = -1;
                for (Item_Workout_Routine routine : routinesList) {
                    newSelectedRoutineIndex++;
                    if (routine.getTitle().equals(oldRoutineName)) {
                        break;
                    }
                }

                // Update recycler-views
                if (currentSelectedRoutineIdx != newSelectedRoutineIndex) {
                    updateSelectedRoutine(newSelectedRoutineIndex);
                }
                if (currentSelectedPlanIdx != newSelectedPlanIndex) {
                    updateSelectedPlan(newSelectedPlanIndex);
                }

            }
        }

        // -----------------------------------------------------------------------------------------
        // Set up edit-texts

        EditText editTextExerciseName = findViewById(R.id.editTextExerciseName);
        EditText editTextExerciseSets = findViewById(R.id.editTextCreateExerciseSets);
        EditText editTextExerciseRepetitions = findViewById(R.id.editTextCreateExerciseRepetitions);
        EditText editTextExerciseWeight = findViewById(R.id.editTextCreateExerciseWeight);

        if (mode.equals("edit")) {
            editTextExerciseName.setText(exerciseName);
            editTextExerciseSets.setText(String.valueOf(exerciseSets));
            editTextExerciseRepetitions.setText(String.valueOf(exerciseRepetitions));
            editTextExerciseWeight.setText(convertDataToText(exerciseWeight));
        }

        editTextExerciseName.addTextChangedListener(new textWatcher(0));
        editTextExerciseSets.addTextChangedListener(new textWatcher(1));
        editTextExerciseRepetitions.addTextChangedListener(new textWatcher(2));
        editTextExerciseWeight.addTextChangedListener(new textWatcher(3));

        // -----------------------------------------------------------------------------------------
        // Set up toolbar

        toolbarActivityCreateExercise = findViewById(R.id.toolbarActivityCreateExercise);
        if (mode.equals("edit")) {
            toolbarActivityCreateExercise.setTitle(getResources().getString(R.string.button_edit_exercises));
        } else {
            toolbarActivityCreateExercise.setTitle(getResources().getString(R.string.button_create_exercises));
        }
        setSupportActionBar(toolbarActivityCreateExercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // -----------------------------------------------------------------------------------------
        // Set up buttons

        saveButton = findViewById(R.id.buttonSaveNewExercise);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    if (exerciseName == null) {
                        // If name was not set up yet remind user to add one
                        Toast.makeText(getApplicationContext(), "Please enter a name first!", Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, "Please enter a name first!", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    savePossible = false;

                    if (mode.equals("edit")) {
                        // Delete old exercise
                        databaseHelper.deleteWorkoutExercise(oldPlanName, oldRoutineName, oldExerciseName);
                    }

                    // Save data to database
                    databaseHelper.addWorkoutExercise(
                            plansList.get(currentSelectedPlanIdx).getTitle(),  // Get plan name
                            routinesList.get(currentSelectedRoutineIdx).getTitle(),  // Get routine name
                            exerciseName,
                            exerciseSets,
                            exerciseRepetitions,
                            exerciseWeight
                    );
                    databaseHelper.close();


                    // Change to edit-mode
                    mode = "edit";
                    oldPlanName = plansList.get(currentSelectedPlanIdx).getTitle();
                    oldRoutineName = routinesList.get(currentSelectedRoutineIdx).getTitle();
                    oldExerciseName = exerciseName;

                    // Change button color
                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                    saveButton.setTextColor(getColor(R.color.text_middle));
                    cancelButton.setText(R.string.button_text_back);
                    toolbarActivityCreateExercise.setTitle(getResources().getString(R.string.button_create_exercises));

                    Snackbar.make(view, "Exercise saved!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton = findViewById(R.id.buttonCancelNewExercise);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            // -> This method starts Activity_Main
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Main.class);
                if (date != null) { intent.putExtra("date", date); }
                intent.putExtra("fragmentID", 1);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }


    // Interface methods ---------------------------------------------------------------------------

    @Override
    public void onPlanItemClick(int itemPosition) {
        // -> This method updates the selected plan visually
        if (itemPosition != currentSelectedPlanIdx) {
            updateSelectedPlan(itemPosition);

            // Update routines list
            getRoutinesFromDatabase(plansList.get(itemPosition).getTitle());
            recyclerViewRoutines.setAdapter(adapterRoutines);
            recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public void onRoutineItemClick(int itemPosition) {
        // -> This method updates the selected routine visually
        if (itemPosition != currentSelectedRoutineIdx) {
            updateSelectedRoutine(itemPosition);
        }
    }
}