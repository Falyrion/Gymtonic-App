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

public class Adapter_Workout_Plans extends RecyclerView.Adapter<Adapter_Workout_Plans.Viewholder> {

    /*
    Adapter for RecyclerView in Fragment_Exercises. Used to display Item_Routine.
     */

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        LinearLayout linearLayout;
        Interface_Workout_Plans interfaceWorkoutPlans;

        public Viewholder(@NonNull View itemView, Interface_Workout_Plans interfaceWorkoutPlans) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.routineTitle);
            linearLayout = itemView.findViewById(R.id.routineLayoutBackground);
            this.interfaceWorkoutPlans = interfaceWorkoutPlans;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Interface

    public interface Interface_Workout_Plans {
        void onPlanItemClick(int itemPosition);
    }

    // ---------------------------------------------------------------------------------------------
    // Constructor of this class

    private List<Item_Workout_Plan> plansList;
    private Interface_Workout_Plans interfaceWorkoutPlans;

    public Adapter_Workout_Plans(List<Item_Workout_Plan> plansList, Interface_Workout_Plans interfaceWorkoutPlans) {
        this.plansList = plansList;
        this.interfaceWorkoutPlans = interfaceWorkoutPlans;
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
        View view = inflater.inflate(R.layout.recyclerview_item_workout_routine, parent, false);

        // Return new holder instance
        return new Viewholder(view, interfaceWorkoutPlans);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Get the data-class based on position
        Item_Workout_Plan planItem = plansList.get(position);

        // Set item views based on views and data-class
        String planTitle = "  " + planItem.getTitle().toUpperCase() + "  ";
        holder.textViewTitle.setText(planTitle);  // Title

        // Set background color
        if (planItem.getIsSelected()) {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_light);
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_middle);
        }

        // Set onClick-Events
        holder.linearLayout.setOnClickListener(view -> {
            holder.interfaceWorkoutPlans.onPlanItemClick(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }
}
