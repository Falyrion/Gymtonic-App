package com.falyrion.gymtonicapp.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.falyrion.gymtonicapp.Activity_Main;
import com.falyrion.gymtonicapp.R;
import com.falyrion.gymtonicapp.activities.Activity_Calendar;
import com.falyrion.gymtonicapp.activities.Activity_Meals_AddDailyEntry;
import com.falyrion.gymtonicapp.activities.Activity_Meals_MealsOfDay;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Fragment_Nutrition extends Fragment {

    // TODO: - Add more nutrition details (spurenelemente, metalle, vitamine, etc)
    // - in Add_Meal_Actitvity funktion hinzufügen das man auf ein preset clicken kann und es bearbeiten und löschen
    // - Noch mal schauen ob datum zwischen add_meal_activity und createmealactitvity richtig hin und her gegeben wird
    // - Funktion zum löschen von bereits zum tag hinzugefügten meals
    // - Progress bars schöner machen


    private String date;
    private double[] dataFood;
    private double[] dataGoals;

    private int percentOf(double current, double max) {
        return (int) ((current / max) * 100);
    }

    private String convertDataToDoubleText(double value) {
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

    private String convertDataToIntText(double value) {
        return String.valueOf((int) value);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get date from arguments
        if (getArguments().containsKey("date")) {
            date = getArguments().getString("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        // Load data from database
        Cursor cursorDataFood = ((Activity_Main) requireContext()).databaseHelper.getConsumedMealsSums(date);
        if (cursorDataFood.getCount() > 0) {
            cursorDataFood.moveToFirst();
            dataFood = new double[31];
            for (int i = 0; i <= 30; i++) {
                dataFood[i] = cursorDataFood.getDouble(i);
            }

        } else {
            dataFood = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
        cursorDataFood.close();

        Cursor cursorSettings = ((Activity_Main) requireContext()).databaseHelper.getSettingsGoals();
        if (cursorSettings.getCount() > 0) {
            cursorSettings.moveToFirst();
            dataGoals = new double[] {
                    cursorSettings.getDouble(0),  // Goal Calories
                    cursorSettings.getDouble(1),  // Goal Fat
                    cursorSettings.getDouble(2),  // Goal Carbohydrates
                    cursorSettings.getDouble(3)  // Goal Protein
            };
        } else {
            dataGoals = new double[] {2000, 2000, 2000, 2000};
        }
        cursorSettings.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrition, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Date
        TextView textDate = view.findViewById(R.id.textViewDBNDate);
        textDate.setText(date);

        // Set values for main-dashboard
        ProgressBar progressBarMain = getView().findViewById(R.id.progressBarDBNCaloriesMain);
        TextView textProgressMain = getView().findViewById(R.id.textViewDNCaloriesMain);
        ObjectAnimator.ofInt(progressBarMain, "progress", percentOf(dataFood[0], dataGoals[0])).start();
        textProgressMain.setText(convertDataToIntText(dataFood[0]));

        ProgressBar progressBarFat = getView().findViewById(R.id.progressBarDBNFatMain);
        TextView textProgressFat = getView().findViewById(R.id.textViewProgressDBNFatMain);
        ObjectAnimator.ofInt(progressBarFat, "progress", percentOf(dataFood[1], dataGoals[1])).start();
        String textFat = convertDataToIntText(dataFood[1]) + " g";
        textProgressFat.setText(textFat);

        ProgressBar progressBarCarbohydrates = getView().findViewById(R.id.progressBarDBNCarbsMain);
        TextView textProgressCarbohydrates = getView().findViewById(R.id.textViewProgressCarbohydrates);
        ObjectAnimator.ofInt(progressBarCarbohydrates, "progress", percentOf(dataFood[3], dataGoals[2])).start();
        String textCarbohydrates = convertDataToIntText(dataFood[3]) + " g";
        textProgressCarbohydrates.setText(textCarbohydrates);

        ProgressBar progressBarProtein = getView().findViewById(R.id.progressBarDBNProteinMain);
        TextView textProgressProtein = getView().findViewById(R.id.textViewProgressDBNProteinMain);
        ObjectAnimator.ofInt(progressBarProtein, "progress", percentOf(dataFood[5], dataGoals[3])).start();
        String textProtein = convertDataToIntText(dataFood[5]) + " g";
        textProgressProtein.setText(textProtein);

        // Set values for details-dashboard
        TextView textViewDetailsCal = getView().findViewById(R.id.textViewDBNDetailsCalories);
        textViewDetailsCal.setText(convertDataToDoubleText(dataFood[0]));

        TextView textViewDetailsFat = getView().findViewById(R.id.textViewDBNDetailsFat);
        textViewDetailsFat.setText(convertDataToDoubleText(dataFood[1]));

        TextView textViewDetailsFatSat = getView().findViewById(R.id.textViewDBNDetailsFatSat);
        textViewDetailsFatSat.setText(convertDataToDoubleText(dataFood[2]));

        TextView textViewDetailsCarbs = getView().findViewById(R.id.textViewDBNDetailsCarbs);
        textViewDetailsCarbs.setText(convertDataToDoubleText(dataFood[3]));

        TextView textViewDetailsSugar = getView().findViewById(R.id.textViewDBNDetailsSugar);
        textViewDetailsSugar.setText(convertDataToDoubleText(dataFood[4]));

        TextView textViewDetailsProtein = getView().findViewById(R.id.textViewDBNDetailsProtein);
        textViewDetailsProtein.setText(convertDataToDoubleText(dataFood[5]));

        TextView textViewDetailsSalt = getView().findViewById(R.id.textViewDBNDetailsSalt);
        textViewDetailsSalt.setText(convertDataToDoubleText(dataFood[6]));

        TextView textViewDetailsFiber = getView().findViewById(R.id.textViewDBNDetailsFiber);
        textViewDetailsFiber.setText(convertDataToDoubleText(dataFood[7]));

        TextView textViewDetailsChol = getView().findViewById(R.id.textViewDBNDetailsChol);
        textViewDetailsChol.setText(convertDataToDoubleText(dataFood[8]));

        TextView textViewDetailsCreatine = getView().findViewById(R.id.textViewDBNDetailsCreatine);
        textViewDetailsCreatine.setText(convertDataToDoubleText(dataFood[9]));

        TextView textViewDetailsCa = getView().findViewById(R.id.textViewDBNDetailsCa);
        textViewDetailsCa.setText(convertDataToDoubleText(dataFood[10]));

        TextView textViewDetailsFe = getView().findViewById(R.id.textViewDBNDetailsFe);
        textViewDetailsFe.setText(convertDataToDoubleText(dataFood[11]));

        TextView textViewDetailsKa = getView().findViewById(R.id.textViewDBNDetailsKa);
        textViewDetailsKa.setText(convertDataToDoubleText(dataFood[12]));

        TextView textViewDetailsMg = getView().findViewById(R.id.textViewDBNDetailsMg);
        textViewDetailsMg.setText(convertDataToDoubleText(dataFood[13]));

        TextView textViewDetailMn = getView().findViewById(R.id.textViewDBNDetailsMn);
        textViewDetailMn.setText(convertDataToDoubleText(dataFood[14]));

        TextView textViewDetailsNa = getView().findViewById(R.id.textViewDBNDetailsNa);
        textViewDetailsNa.setText(convertDataToDoubleText(dataFood[15]));

        TextView textViewDetailsP = getView().findViewById(R.id.textViewDBNDetailsP);
        textViewDetailsP.setText(convertDataToDoubleText(dataFood[16]));

        TextView textViewDetailsZn = getView().findViewById(R.id.textViewDBNDetailsZn);
        textViewDetailsZn.setText(convertDataToDoubleText(dataFood[17]));

        TextView textViewDetailsVitA = getView().findViewById(R.id.textViewDBNDetailsVitA);
        textViewDetailsVitA.setText(convertDataToDoubleText(dataFood[18]));

        TextView textViewDetailsVitB1 = getView().findViewById(R.id.textViewDBNDetailsVitB1);
        textViewDetailsVitB1.setText(convertDataToDoubleText(dataFood[19]));

        TextView textViewDetailsVitB2 = getView().findViewById(R.id.textViewDBNDetailsVitB2);
        textViewDetailsVitB2.setText(convertDataToDoubleText(dataFood[20]));

        TextView textViewDetailsVitB3 = getView().findViewById(R.id.textViewDBNDetailsVitB3);
        textViewDetailsVitB3.setText(convertDataToDoubleText(dataFood[21]));

        TextView textViewDetailsVitB5 = getView().findViewById(R.id.textViewDBNDetailsVitB5);
        textViewDetailsVitB5.setText(convertDataToDoubleText(dataFood[22]));

        TextView textViewDetailsVitB6 = getView().findViewById(R.id.textViewDBNDetailsVitB6);
        textViewDetailsVitB6.setText(convertDataToDoubleText(dataFood[23]));

        TextView textViewDetailsVitB7 = getView().findViewById(R.id.textViewDBNDetailsVitB7);
        textViewDetailsVitB7.setText(convertDataToDoubleText(dataFood[24]));

        TextView textViewDetailsVitB11 = getView().findViewById(R.id.textViewDBNDetailsVitB11);
        textViewDetailsVitB11.setText(convertDataToDoubleText(dataFood[25]));

        TextView textViewDetailsVitB12 = getView().findViewById(R.id.textViewDBNDetailsVitB12);
        textViewDetailsVitB12.setText(convertDataToDoubleText(dataFood[26]));

        TextView textViewDetailsVitC = getView().findViewById(R.id.textViewDBNDetailsVitC);
        textViewDetailsVitC.setText(convertDataToDoubleText(dataFood[27]));

        TextView textViewDetailsVitE = getView().findViewById(R.id.textViewDBNDetailsVitE);
        textViewDetailsVitE.setText(convertDataToDoubleText(dataFood[28]));

        TextView textViewDetailsVitK = getView().findViewById(R.id.textViewDBNDetailsVitK);
        textViewDetailsVitK.setText(convertDataToDoubleText(dataFood[29]));

        TextView textViewDetailsVitH = getView().findViewById(R.id.textViewDBNDetailsVitH);
        textViewDetailsVitH.setText(convertDataToDoubleText(dataFood[30]));

        // Set buttons
        Button buttonAddMeal = getView().findViewById(R.id.buttonDashboardNutritionAddMeal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Meals_AddDailyEntry.class);
                intent.putExtra("date", date);
                intent.putExtra("fragmentID", 0);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });

        Button buttonShowEatenMeals = getView().findViewById(R.id.buttonEatenMeals);
        buttonShowEatenMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Meals_MealsOfDay.class);
                intent.putExtra("date", date);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });

        ImageButton buttonCalendar = getView().findViewById(R.id.buttonDBNCalendar);
        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Activity_Calendar.class);
                intent.putExtra("date", date);
                intent.putExtra("fragmentID", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });

        TableLayout tableLayoutDetails = view.findViewById(R.id.tableLayoutDashboardNutritionDetails);

        ImageButton buttonToggleDetails = view.findViewById(R.id.buttonExpandNutritionDetails);
        buttonToggleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tableLayoutDetails.getVisibility() == View.GONE) {
                    tableLayoutDetails.setVisibility(View.VISIBLE);
                    buttonToggleDetails.setImageResource(R.drawable.ic_baseline_expand_less_24);
                    return;
                }
                tableLayoutDetails.setVisibility(View.GONE);
                buttonToggleDetails.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

//        ScrollView mainLayout = view.findViewById(R.id.scrollViewMainLayout);
//        mainLayout.setVisibility(View.VISIBLE);
//
//        FrameLayout loadingLayout = view.findViewById(R.id.loadingLayout);
//        loadingLayout.setVisibility(View.GONE);
    }

}