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

public class Adapter_Workout_Routine extends RecyclerView.Adapter<Adapter_Workout_Routine.Viewholder> {

    /*
    Adapter for RecyclerView in Fragment_Exercises. Used to display Item_Routine.
     */

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        LinearLayout linearLayout;
        Interface_Workout_Routine interfaceWorkoutRoutine;

        public Viewholder(@NonNull View itemView, Interface_Workout_Routine interfaceWorkoutRoutine) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.routineTitle);
            linearLayout = itemView.findViewById(R.id.routineLayoutBackground);
            this.interfaceWorkoutRoutine = interfaceWorkoutRoutine;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Interface

    public interface Interface_Workout_Routine {
        void onRoutineItemClick(int itemPosition);
    }

    // ---------------------------------------------------------------------------------------------
    // Constructor of this class

    private List<Item_Workout_Routine> routineList;
    private Interface_Workout_Routine interfaceWorkoutRoutine;

    public Adapter_Workout_Routine(List<Item_Workout_Routine> routineList, Interface_Workout_Routine interfaceWorkoutRoutine) {
        this.routineList = routineList;
        this.interfaceWorkoutRoutine = interfaceWorkoutRoutine;
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
        View viewRoutine = inflater.inflate(R.layout.recyclerview_item_workout_routine, parent, false);

        // Return new holder instance
        return new Viewholder(viewRoutine, interfaceWorkoutRoutine);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Get the data-class based on position
        Item_Workout_Routine routineItem = routineList.get(position);

        // Set item views based on views and data-class
        String routineTitle = "  " + routineItem.getTitle().toUpperCase() + "  ";
        holder.textViewTitle.setText(routineTitle);  // Title

        // Set background color
        if (routineItem.getIsSelected()) {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_light);
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_middle);
        }

        // Set onClick-Events
        holder.linearLayout.setOnClickListener(view -> {
            holder.interfaceWorkoutRoutine.onRoutineItemClick(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }
}
