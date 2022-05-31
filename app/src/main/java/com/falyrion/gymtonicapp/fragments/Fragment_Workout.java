package com.falyrion.gymtonicapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;
import com.falyrion.gymtonicapp.activities.Activity_Workout_CreateEditExercise;
import com.falyrion.gymtonicapp.activities.Activity_Workout_EditPlans;
import com.falyrion.gymtonicapp.activities.Activity_Workout_EditRoutines;
import com.falyrion.gymtonicapp.recyclerview.Adapter_Workout_Exercise;
import com.falyrion.gymtonicapp.recyclerview.Adapter_Workout_Routine;
import com.falyrion.gymtonicapp.recyclerview.Item_Workout_Exercise;
import com.falyrion.gymtonicapp.recyclerview.Item_Workout_Routine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class Fragment_Workout extends Fragment implements Adapter_Workout_Routine.Interface_Workout_Routine, Adapter_Workout_Exercise.Interface_Workout_Exercises, AdapterView.OnItemSelectedListener {

    private String[] workoutPlans;
    private ArrayList<Item_Workout_Routine> workoutRoutines;
    private ArrayList<Item_Workout_Exercise> workoutExercises;
    private int selectedPlanIdx = -1;
    private int selectedRoutineIdx = -1;
    private RecyclerView recyclerViewRoutines;
    private RecyclerView recyclerViewExercises;
    private Adapter_Workout_Routine adapterRoutines;
    private Adapter_Workout_Exercise adapterExercises;

    private boolean fabOpen = false;
    private FloatingActionButton fabMain;
    private LinearLayout linearLayoutFABSub01;
    private LinearLayout linearLayoutFABSub02;
    private LinearLayout linearLayoutFABSub03;
    private View backgroundBlur;

    TextView sectionTitleRoutines;
    TextView sectionTitleExercises;


    private String[] loadPlansFromDatabase() {
        Cursor cursor = ((Activity_Main) requireContext()).databaseHelper.getWorkoutPlans();
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

    private ArrayList<Item_Workout_Routine> loadRoutinesFromDatabase(String workoutPlan) {
        Cursor cursor = ((Activity_Main) requireContext()).databaseHelper.getWorkoutRoutines(workoutPlan);
        ArrayList<Item_Workout_Routine> loadedRoutines = new ArrayList<Item_Workout_Routine>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            loadedRoutines.add(cursor.getPosition(), new Item_Workout_Routine(cursor.getString(0), true));

            while (cursor.moveToNext()) {
                loadedRoutines.add(cursor.getPosition(), new Item_Workout_Routine(cursor.getString(0), false));
            }
        }

        return loadedRoutines;
    }

    private ArrayList<Item_Workout_Exercise> loadExercisesFromDatabase(String workoutPlan, String workoutRoutine) {
        Cursor cursor = ((Activity_Main) requireContext()).databaseHelper.getWorkoutExercises(workoutPlan, workoutRoutine);
        ArrayList<Item_Workout_Exercise> loadedExercises = new ArrayList<Item_Workout_Exercise>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loadedExercises.add(
                        cursor.getPosition(),
                        new Item_Workout_Exercise(
                                cursor.getString(0),  // Title
                                cursor.getInt(1),  // Sets
                                cursor.getInt(2),  // Reps
                                cursor.getDouble(3)  // Weight
                        )
                );
            }
        }

        return loadedExercises;
    }

    private void toggleFABMenu() {
        if (fabOpen) {
            // Close FAB-menu
            fabOpen = false;
            fabMain.setImageResource(R.drawable.ic_baseline_add_circle_24);
            linearLayoutFABSub01.setTranslationY(0);
            linearLayoutFABSub02.setTranslationY(0);
            linearLayoutFABSub03.setTranslationY(0);
            linearLayoutFABSub01.setVisibility(View.INVISIBLE);
            linearLayoutFABSub02.setVisibility(View.INVISIBLE);
            linearLayoutFABSub03.setVisibility(View.INVISIBLE);
            backgroundBlur.setVisibility(View.GONE);
        } else {
            // Oben FAB-menu
            fabOpen = true;
            backgroundBlur.setVisibility(View.VISIBLE);
            fabMain.setImageResource(R.drawable.ic_baseline_remove_circle_24);
            linearLayoutFABSub01.setVisibility(View.VISIBLE);
            linearLayoutFABSub02.setVisibility(View.VISIBLE);
            linearLayoutFABSub03.setVisibility(View.VISIBLE);
            linearLayoutFABSub01.animate().translationY(-170);
            linearLayoutFABSub02.animate().translationY(-340);
            linearLayoutFABSub03.animate().translationY(-510);
        }
    }


    // Override class default methods --------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load data from database and fill lists and create adapters

        workoutPlans = loadPlansFromDatabase();
        selectedPlanIdx = 0;

        workoutRoutines = loadRoutinesFromDatabase(workoutPlans[0]);

        if (!workoutRoutines.isEmpty()) {
            workoutExercises = loadExercisesFromDatabase(workoutPlans[0], workoutRoutines.get(0).getTitle());
            Log.d("uwu", "routines size: " + workoutRoutines.size());
            Log.d("uwu", "exerzises size: " + workoutExercises.size());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Set up recycler-views

        sectionTitleRoutines = view.findViewById(R.id.sectionTitleRoutines);
        sectionTitleExercises = view.findViewById(R.id.sectionTitleExercises);

        Spinner spinnerPlans = view.findViewById(R.id.spinnerWorkoutPlans);
        spinnerPlans.setOnItemSelectedListener(this);
        ArrayAdapter adapterPlans = new ArrayAdapter(getContext(), R.layout.spinner_item_purple_middle, workoutPlans);
        adapterPlans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlans.setAdapter(adapterPlans);

        recyclerViewRoutines = view.findViewById(R.id.recyclerViewRoutines);
        if (!workoutRoutines.isEmpty()) {
            adapterRoutines = new Adapter_Workout_Routine(workoutRoutines, this);
            recyclerViewRoutines.setAdapter(adapterRoutines);
            recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            selectedRoutineIdx = 0;
        } else {
            sectionTitleRoutines.setVisibility(View.INVISIBLE);
        }

        recyclerViewExercises = view.findViewById(R.id.recyclerViewExercises);
        if (!workoutExercises.isEmpty()) {
            adapterExercises = new Adapter_Workout_Exercise(workoutExercises, this);
            recyclerViewExercises.setAdapter(adapterExercises);
            recyclerViewExercises.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            sectionTitleExercises.setVisibility(View.INVISIBLE);
        }

        // FAB-Views -------------------------------------------------------------------------------

        linearLayoutFABSub01 = view.findViewById(R.id.linearLayoutFAB01);
        linearLayoutFABSub02 = view.findViewById(R.id.linearLayoutFAB02);
        linearLayoutFABSub03 = view.findViewById(R.id.linearLayoutFAB03);
        backgroundBlur = view.findViewById(R.id.fragmentExercisesBlur);
        fabMain = view.findViewById(R.id.fabExercisesMain);
        FloatingActionButton fabSub01 = view.findViewById(R.id.fabExercises01);
        FloatingActionButton fabSub02 = view.findViewById(R.id.fabExercises02);
        FloatingActionButton fabSub03 = view.findViewById(R.id.fabExercises03);

        // Main Button
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFABMenu();
            }
        });

        // Background blur
        backgroundBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFABMenu();
            }
        });

        // Button "Add Exercise"
        fabSub01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workoutRoutines.isEmpty()) {
                    Snackbar.make(view, "Please create at least one workout routine first!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(view.getContext(), Activity_Workout_CreateEditExercise.class);
                intent.putExtra("date", ((Activity_Main) requireContext()).date);
                startActivity(intent);
            }
        });

        // Button "Add Routine"
        fabSub02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if at least 1 plan exists, else do not open new activity
                if (workoutPlans.length <= 0) {
                    Toast.makeText(getContext(), "You must create at least 1 workout plan first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Start activity
                Intent intent = new Intent(view.getContext(), Activity_Workout_EditRoutines.class);
                startActivity(intent);
            }
        });

        // Button "Add Plan"
        fabSub03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Workout_EditPlans.class);
                startActivity(intent);
            }
        });

    }


    // Interface methods ---------------------------------------------------------------------------

    // Spinner Plan Methods
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == selectedPlanIdx) {
            return;
        }

        selectedPlanIdx = position;

        // Update routines
        workoutRoutines.clear();
        if (adapterRoutines != null) {
            adapterRoutines.notifyDataSetChanged();
        }

        workoutExercises.clear();
        if (adapterExercises != null) {
            adapterExercises.notifyDataSetChanged();
        }

        workoutRoutines = loadRoutinesFromDatabase(workoutPlans[selectedPlanIdx]);

        if (workoutRoutines.isEmpty()) {
            sectionTitleRoutines.setVisibility(View.INVISIBLE);
            sectionTitleExercises.setVisibility(View.INVISIBLE);
            return;
        }

        adapterRoutines = new Adapter_Workout_Routine(workoutRoutines, this);
        recyclerViewRoutines.setAdapter(adapterRoutines);
        recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedRoutineIdx = 0;
        sectionTitleRoutines.setVisibility(View.VISIBLE);

        // Updates exercises
        workoutExercises = loadExercisesFromDatabase(workoutPlans[selectedPlanIdx], workoutRoutines.get(selectedRoutineIdx).getTitle());

        if (!workoutExercises.isEmpty()) {
            adapterExercises = new Adapter_Workout_Exercise(workoutExercises, this);
            recyclerViewExercises.setAdapter(adapterExercises);
            recyclerViewExercises.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            sectionTitleExercises.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
    }

    // Routine adapter methods
    @Override
    public void onRoutineItemClick(int itemPosition) {
        if (selectedRoutineIdx == -1) {
            return;
        }

        if (itemPosition == selectedRoutineIdx) {
            return;
        }

        // Update routine-items visually
        workoutRoutines.get(itemPosition).setIsSelected(true);
        workoutRoutines.get(selectedRoutineIdx).setIsSelected(false);
        adapterRoutines.notifyItemChanged(itemPosition);
        adapterRoutines.notifyItemChanged(selectedRoutineIdx);

        selectedRoutineIdx = itemPosition;

        // Update exercises
        workoutExercises.clear();
        if (adapterExercises != null) {
            adapterExercises.notifyDataSetChanged();
        }


        workoutExercises = loadExercisesFromDatabase(workoutPlans[selectedPlanIdx], workoutRoutines.get(selectedRoutineIdx).getTitle());

        if (!workoutExercises.isEmpty()) {
            adapterExercises = new Adapter_Workout_Exercise(workoutExercises, this);
            recyclerViewExercises.setAdapter(adapterExercises);
            recyclerViewExercises.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }


        // adapterExercises.notifyDataSetChanged();
    }

    // Exercise adapter methods
    @Override
    public void onExerciseItemClick(int itemPosition) {
        Intent intent = new Intent(getContext(), Activity_Workout_CreateEditExercise.class);
        intent.putExtra("date", ((Activity_Main) requireContext()).date);
        intent.putExtra("mode", "edit");
        intent.putExtra("planName", workoutPlans[selectedPlanIdx]);
        intent.putExtra("routineName", workoutRoutines.get(selectedRoutineIdx).getTitle());
        intent.putExtra("exerciseName", workoutExercises.get(itemPosition).getTitle());
        intent.putExtra("exerciseSets", workoutExercises.get(itemPosition).getSets());
        intent.putExtra("exerciseReps", workoutExercises.get(itemPosition).getRepetitions());
        intent.putExtra("exerciseWeight", workoutExercises.get(itemPosition).getWeight());
        startActivity(intent);
    }

}