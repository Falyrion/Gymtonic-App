package com.falyrion.gymtonicapp.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falyrion.gymtonicapp.R;

import java.util.List;

public class Adapter_Workout_Exercise extends RecyclerView.Adapter<Adapter_Workout_Exercise.Viewholder> {

    /*
    Adapter for RecyclerView in Fragment_Exercises. Used to display Item_Exercise.
     */

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewSets;
        TextView textViewReps;
        TextView textViewWeight;
        LinearLayout linearLayoutMain;
        Interface_Workout_Exercises interface_rv_exercises;

        public Viewholder(@NonNull View itemView, Interface_Workout_Exercises interface_rv_exercises) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.rvItemExerciseTitle);
            textViewSets = itemView.findViewById(R.id.rvItemExerciseSets);
            textViewReps = itemView.findViewById(R.id.rvItemExerciseRepetitions);
            textViewWeight = itemView.findViewById(R.id.rvItemExerciseWeight);
            linearLayoutMain = itemView.findViewById(R.id.rvItemExerciseMain);

            this.interface_rv_exercises = interface_rv_exercises;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Interface

    public interface Interface_Workout_Exercises {
        void onExerciseItemClick(int itemPosition);
    }

    // ---------------------------------------------------------------------------------------------
    // Constructor of this class

    private List<Item_Workout_Exercise> exerciseList;
    private Interface_Workout_Exercises interfaceWorkoutExercises;

    public Adapter_Workout_Exercise(List<Item_Workout_Exercise> exerciseList, Interface_Workout_Exercises interfaceWorkoutExercises) {
        this.exerciseList = exerciseList;
        this.interfaceWorkoutExercises = interfaceWorkoutExercises;
    }

    // ---------------------------------------------------------------------------------------------
    // Functions for this class

    private String convertDataToText(double value) {
        // Convert given double to string.
        // Check if double value has ".0" decimals. If yes cut it out.
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate custom layout
        View viewExercise = inflater.inflate(R.layout.recylcerview_item_workout_exercise, parent, false);

        // Return new holder instance
        return new Viewholder(viewExercise, interfaceWorkoutExercises);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Get the data-class based on position
        Item_Workout_Exercise excerciseItem = exerciseList.get(position);

        // Set item views based on views and data-class
        holder.textViewTitle.setText(excerciseItem.getTitle());  // Title
        holder.textViewSets.setText(String.valueOf(excerciseItem.getSets()));  // Sets
        holder.textViewReps.setText(String.valueOf(excerciseItem.getRepetitions()));  // Repetitions
        holder.textViewWeight.setText(convertDataToText(excerciseItem.getWeight()));  // Weight

        // Set onClick-Events
        holder.linearLayoutMain.setOnClickListener(view -> {
            holder.interface_rv_exercises.onExerciseItemClick(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
