package com.falyrion.gymtonicapp.activities;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Activity_Workout_EditPlans extends AppCompatActivity implements Adapter_Item_General_001.Interface_Item_Edit {

    /**
     * This activity lets the user edit workout plans. Changes will be saved to the database.
     *
     * - This activity is for changing values of an existing plan only. Creating new plans happens
     *   in the Fragment_Workout.
     */

    private boolean savePossible = false;
    private int currentEditedPlanIndex;

    private ArrayList<Item_General_001> plansList;
    private Adapter_Item_General_001 adapterWorkoutPlans;
    private DatabaseHelper databaseHelper;


    private ArrayList<Item_General_001> loadPlansFromDatabase() {
        Cursor cursor = databaseHelper.getWorkoutPlans();
        ArrayList<Item_General_001> loadedPlans = new ArrayList<Item_General_001>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loadedPlans.add(cursor.getPosition(), new Item_General_001(cursor.getString(0)));
            }
        }

        return loadedPlans;
    }

    private void showDialogEditPlanName(String currentPlanName, int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextPlanName = view.findViewById(R.id.dialogEditText);
        editTextPlanName.setText(currentPlanName);

        builder.setTitle("Edit plan name");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newPlanName = editTextPlanName.getText().toString();

                if (newPlanName.length() <= 0) {
                    return;
                }

                // Update data in database
                databaseHelper.updateWorkoutPlanName(currentPlanName, newPlanName);

                // Update recycler view
                plansList.get(itemPosition).setTitle(newPlanName);
                adapterWorkoutPlans.notifyItemChanged(itemPosition);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // alertDialog.getWindow().setLayout(600, 400);
    }

    private void showDialogCreatePlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextPlanName = view.findViewById(R.id.dialogEditText);
        editTextPlanName.setHint("Enter plan name");

        builder.setTitle("Create New Workout Plan");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Create plan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = editTextPlanName.getText().toString();

                if (text.length() <= 0) {
                    return;
                }

                // Add item into list
                int insertIndex = plansList.size();
                plansList.add(insertIndex, new Item_General_001(text));
                adapterWorkoutPlans.notifyItemInserted(insertIndex);

                // Save to database
                databaseHelper.addWorkoutPlan(text);
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
        setContentView(R.layout.activity_workout_editplans);

        databaseHelper = new DatabaseHelper(Activity_Workout_EditPlans.this);
        plansList = loadPlansFromDatabase();

        adapterWorkoutPlans = new Adapter_Item_General_001(plansList, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlans);
        recyclerView.setAdapter(adapterWorkoutPlans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        // Toolbar ---------------------------------------------------------------------------------

        Toolbar toolbar = findViewById(R.id.toolbarActivityEditPlans);
        toolbar.setTitle(getResources().getString(R.string.button_edit_plans));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Buttons ---------------------------------------------------------------------------------

        Button buttonCreatePlan = findViewById(R.id.buttonCreatePlan);
        buttonCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCreatePlan();
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
    public void onItemClicked(int itemPosition) {
        // Open dialog edit plan
        showDialogEditPlanName(plansList.get(itemPosition).getTitle(), itemPosition);
        currentEditedPlanIndex = itemPosition;
    }

    @Override
    public void onButtonRemoveClicked(int itemPosition) {
        if (plansList.size() == 1) {
            // Toast.makeText(getApplicationContext(), "You must have at least 1 workout plan", Toast.LENGTH_SHORT).show();
            showDialogError();
            return;
        }

        // Delete plan from database
        databaseHelper.deleteWorkoutPlan(plansList.get(itemPosition).getTitle());

        // Update recycler view
        plansList.remove(itemPosition);
        adapterWorkoutPlans.notifyItemRemoved(itemPosition);
    }

}