package com.falyrion.gymtonicapp.activities;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falyrion.gymtonicapp.R;
import com.falyrion.gymtonicapp.data.DatabaseHelper;
import com.falyrion.gymtonicapp.recyclerview.Adapter_Item_General_001;
import com.falyrion.gymtonicapp.recyclerview.Item_General_001;

import java.util.ArrayList;

public class Activity_Workout_EditRoutines extends AppCompatActivity implements Adapter_Item_General_001.Interface_Item_Edit, AdapterView.OnItemSelectedListener {

    /**
     * This activity lets the user edit workout routines. Changes will be saved to the database.
     *
     * - This activity is for changing values of an existing routines only. Creating new routines
     *   happens in the Fragment_Workout.
     */

    private boolean savePossible = false;

    private String[] workoutPlans;
    private int selectedPlanIdx = 0;
    private ArrayList<Item_General_001> routinesList;
    private Adapter_Item_General_001 adapterWorkoutRoutines;
    private RecyclerView recyclerViewRoutines;
    private DatabaseHelper databaseHelper;


    private String[] loadPlansFromDatabase() {
        Cursor cursor = databaseHelper.getWorkoutPlans();
        String[] loadedPlans = new String[0];

        if (cursor.getCount() > 0) {
            // Initiate array with length of cursor data
            loadedPlans = new String[cursor.getCount()];
            // Add plan-names to array
            int j = 0;
            while (cursor.moveToNext()) {
                loadedPlans[j] = cursor.getString(0);
                j++;
            }
        }
        cursor.close();

        return loadedPlans;
    }

    private ArrayList<Item_General_001> loadRoutinesFromDatabase(String plan) {
        Cursor cursor = databaseHelper.getWorkoutRoutines(plan);
        ArrayList<Item_General_001> loadedRoutines = new ArrayList<Item_General_001>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loadedRoutines.add(cursor.getPosition(), new Item_General_001(cursor.getString(0)));
            }
        }

        return loadedRoutines;
    }

    private void showDialogEditRoutineName(String currentRoutineName, int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextRoutineName = view.findViewById(R.id.dialogEditText);
        editTextRoutineName.setText(currentRoutineName);

        builder.setTitle(getResources().getString(R.string.button_edit_routines));
        builder.setNegativeButton(getResources().getString(R.string.button_text_cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.button_text_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newRoutineName = editTextRoutineName.getText().toString();

                if (newRoutineName.length() <= 0) {
                    return;
                }

                // Update data in database
                databaseHelper.updateWorkoutRoutineName(workoutPlans[selectedPlanIdx], currentRoutineName, newRoutineName);

                // Update recycler view
                routinesList.get(itemPosition).setTitle(newRoutineName);
                adapterWorkoutRoutines.notifyItemChanged(itemPosition);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogCreateRoutine() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextRoutineName = view.findViewById(R.id.dialogEditText);
        editTextRoutineName.setHint("Enter plan name");

        builder.setTitle(getResources().getString(R.string.button_create_routines));
        builder.setNegativeButton(getResources().getString(R.string.button_text_cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.button_create_routines), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = editTextRoutineName.getText().toString();

                if (text.length() <= 0) {
                    return;
                }

                // Add item into list
                int insertIndex = routinesList.size();
                routinesList.add(insertIndex, new Item_General_001(text));
                adapterWorkoutRoutines.notifyItemInserted(insertIndex);

                // Save to database
                databaseHelper.addWorkoutRoutine(workoutPlans[selectedPlanIdx], text);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        builder.setTitle("You must have at least 1 workout plan!");
        // builder.setMessage("uwu");
        builder.setPositiveButton("Okay", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Class overwrite methods ---------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_editroutines);

        // Load data from database
        databaseHelper = new DatabaseHelper(Activity_Workout_EditRoutines.this);
        workoutPlans = loadPlansFromDatabase();
        selectedPlanIdx = 0;
        routinesList = loadRoutinesFromDatabase(workoutPlans[selectedPlanIdx]);

        // Set up spinner for workout-plans
        Spinner spinnerPlans = findViewById(R.id.spinnerWorkoutRoutines);
        spinnerPlans.setOnItemSelectedListener(this);
        ArrayAdapter adapterPlans = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item_purple_middle, workoutPlans);
        adapterPlans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlans.setAdapter(adapterPlans);

        // Set up adapter for recycler-view for routines
        recyclerViewRoutines = findViewById(R.id.recyclerViewRoutines);
        if (!routinesList.isEmpty()) {
            adapterWorkoutRoutines = new Adapter_Item_General_001(routinesList, this);
            recyclerViewRoutines.setAdapter(adapterWorkoutRoutines);
            recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        }

        // Toolbar ---------------------------------------------------------------------------------

        Toolbar toolbar = findViewById(R.id.toolbarActivityEditRoutines);
        toolbar.setTitle("Edit workout routines");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Buttons ---------------------------------------------------------------------------------

//        Button buttonCancel = findViewById(R.id.buttonCancel);
//        buttonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), Activity_Main.class);
//                intent.putExtra("fragmentID", 2);
//                startActivity(intent);
//            }
//        });

        Button buttonCreatePlan = findViewById(R.id.buttonCreateRoutine);
        buttonCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCreateRoutine();
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }


    // Interface methods ---------------------------------------------------------------------------

    // Spinner "Plan" Methods
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == selectedPlanIdx) {
            return;
        }

        selectedPlanIdx = position;

        // Update routines
        routinesList.clear();
        adapterWorkoutRoutines.notifyDataSetChanged();

        routinesList = loadRoutinesFromDatabase(workoutPlans[selectedPlanIdx]);

        adapterWorkoutRoutines = new Adapter_Item_General_001(routinesList, this);
        recyclerViewRoutines.setAdapter(adapterWorkoutRoutines);
        recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
    }

    // "Routines" Recyclerview Adapter Methods
    @Override
    public void onItemClicked(int itemPosition) {
        // Open dialog edit plan
        showDialogEditRoutineName(routinesList.get(itemPosition).getTitle(), itemPosition);
    }

    @Override
    public void onButtonRemoveClicked(int itemPosition) {
        // Delete plan from database
        databaseHelper.deleteWorkoutRoutine(workoutPlans[selectedPlanIdx], routinesList.get(itemPosition).getTitle());

        // Update recycler view
        routinesList.remove(itemPosition);
        adapterWorkoutRoutines.notifyItemRemoved(itemPosition);
    }

}