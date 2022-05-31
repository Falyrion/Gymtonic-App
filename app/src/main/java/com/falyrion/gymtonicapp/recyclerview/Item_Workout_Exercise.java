package com.falyrion.gymtonicapp.recyclerview;

public class Item_Workout_Exercise {

    /*
    Item for Adapter_Workout_Exercise.
     */

    private String title;
    private int sets;
    private int repetitions;
    private double weight;

    public Item_Workout_Exercise(String title, int sets, int repetitions, double weight) {
        this.title = title;
        this.sets = sets;
        this.repetitions = repetitions;
        this.weight = weight;
    }

    public String getTitle() {
        return this.title;
    }

    public int getSets() {
        return this.sets;
    }

    public int getRepetitions() {
        return this.repetitions;
    }

    public double getWeight() {
        return this.weight;
    }

}
