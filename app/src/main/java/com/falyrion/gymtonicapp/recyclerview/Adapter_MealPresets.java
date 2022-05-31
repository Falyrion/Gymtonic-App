package com.falyrion.gymtonicapp.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.falyrion.gymtonicapp.R;

import java.util.List;

public class Adapter_MealPresets extends RecyclerView.Adapter<Adapter_MealPresets.Viewholder> {

    /*
    Adapter for RecyclerView in Activity_AddMeal. Used to display Item_MealPreset.
     */

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView textViewMealTitle;
        TextView textViewCalories;
        TextView textViewAmount;
        ImageButton buttonAdd;
        ImageButton buttonRemove;
        LinearLayout mealPresetFrame;
        mealPresetItemInterface itemInterface;

        public Viewholder(@NonNull View itemView, mealPresetItemInterface itemInterface) {
            super(itemView);

            textViewMealTitle = (TextView) itemView.findViewById(R.id.textRVAddMealTitle);
            textViewCalories = (TextView) itemView.findViewById(R.id.textViewRVAddMealCalories);
            textViewAmount = (TextView) itemView.findViewById(R.id.mealAmount);
            buttonAdd = (ImageButton) itemView.findViewById(R.id.buttonRVAddMealAdd);
            buttonRemove = (ImageButton) itemView.findViewById(R.id.buttonRVAddMealSubtract);
            mealPresetFrame = (LinearLayout) itemView.findViewById(R.id.linearLayoutMealPreset);

            this.itemInterface = itemInterface;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Public interface for methods called by meal-preset items
    // -> Implemented in Activity_AddMeal

    public interface mealPresetItemInterface {
        void onItemClick(String mealUUID);
        void onAmountClick(int itemPosition);
        void updateItemAmount(int itemPosition, String mealUUID, double newAmount);
    }

    // ---------------------------------------------------------------------------------------------
    // Constructor of this class

    private List<Item_MealPreset> mealPresetsList;
    private mealPresetItemInterface itemInterface;
    private Context context;
    public Adapter_MealPresets(List<Item_MealPreset> mealPresetsList, mealPresetItemInterface itemInterface, Context context) {
        this.mealPresetsList = mealPresetsList;
        this.itemInterface = itemInterface;
        this.context = context;
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
        View viewMealPreset = inflater.inflate(R.layout.recyclerview_item_mealpreset, parent, false);

        // Return new holder instance
        return new Viewholder(viewMealPreset, itemInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Get the data-class based on position
        Item_MealPreset mealPreset = mealPresetsList.get(position);

        // Set item views based on views and data-class
        holder.textViewMealTitle.setText(mealPreset.getMealTitle());  // Title
        holder.textViewCalories.setText(String.valueOf(mealPreset.getCalories()));  // Calories
        holder.textViewAmount.setText(convertDataToText(mealPreset.getAmount()));

        if (mealPreset.getAmount() > 0) {
            holder.buttonAdd.setColorFilter(ContextCompat.getColor(context, R.color.text_middle));
            holder.buttonRemove.setColorFilter(ContextCompat.getColor(context, R.color.text_middle));
            holder.textViewAmount.setTextColor(ContextCompat.getColor(context, R.color.text_middle));
        } else {
            holder.buttonAdd.setColorFilter(ContextCompat.getColor(context, R.color.text_low));
            holder.buttonRemove.setColorFilter(ContextCompat.getColor(context, R.color.text_low));
            holder.textViewAmount.setTextColor(ContextCompat.getColor(context, R.color.text_low));
        }


        // OnClick Text View
        holder.mealPresetFrame.setOnClickListener(view -> {
            holder.itemInterface.onItemClick(mealPreset.getMealUUID());

        });

        // OnClick Button Add
        holder.buttonAdd.setOnClickListener(view -> {
            // Increase amount
            mealPreset.setAmount(mealPreset.getAmount() + 1);
            // holder.editTextAmount.setText(convertDataToText(mealPreset.getAmount()));

            holder.itemInterface.updateItemAmount(holder.getAdapterPosition(), mealPreset.getMealUUID(), mealPreset.getAmount());
        });

        // OnClick Button Remove
        holder.buttonRemove.setOnClickListener(view -> {
            double currentAmount = mealPreset.getAmount();

            // Make sure new amount is minimum 0
            double newAmount = currentAmount - 1;
            if (newAmount < 0) { newAmount = 0; }
            mealPreset.setAmount(newAmount);

            holder.itemInterface.updateItemAmount(holder.getAdapterPosition(), mealPreset.getMealUUID(), newAmount);
        });

        // On Edit Text
        holder.textViewAmount.setOnClickListener(view -> {
            holder.itemInterface.onAmountClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return mealPresetsList.size();
    }
}
