package com.falyrion.gymtonicapp.recyclerview;

public class Item_Workout_Plan {

    /*
    Item for Adapter_Workout_Plans
     */

    private String title;
    private boolean isSelected;

    public Item_Workout_Plan(String title, boolean isSelected) {
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getTitle() { return this.title; }

    public boolean getIsSelected() { return this.isSelected; }

    public void setIsSelected(boolean newStatus) { this.isSelected = newStatus; }

}
