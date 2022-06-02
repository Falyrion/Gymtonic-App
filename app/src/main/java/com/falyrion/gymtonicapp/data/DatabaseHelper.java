package com.falyrion.gymtonicapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * This class contains all methods to access the database.
     */

    private Context context;
    private static final String DATABASE_NAME = "gymtonicapp.db";
    private static final int DATABASE_VERSION = 43;

    // Table foods
    private static final String TABLE_PM = "preset_meals";
    private static final String COL_PM_INDEX = "meal_index";
    private static final String COL_PM_NAME = "name";
    private static final String COL_PM_CATEGORY = "category";
    private static final String COL_PM_CALORIES = "calories";
    private static final String COL_PM_FAT = "fat";
    private static final String COL_PM_FAT_SAT = "fat_sat";
    private static final String COL_PM_CARBS = "carbs";
    private static final String COL_PM_SUGAR = "sugar";
    private static final String COL_PM_PROTEIN = "protein";
    private static final String COL_PM_SALT = "salt";
    private static final String COL_PM_FIBER = "fiber";
    private static final String COL_PM_CHOL = "cholesterin";  // in mg
    private static final String COL_PM_CREATINE = "creatine";
    private static final String COL_PM_CA = "calcium";  // in mg
    private static final String COL_PM_FE = "iron";  // in mg
    private static final String COL_PM_K = "kalium";  // in mg
    private static final String COL_PM_MG = "magnesium";  // in mg
    private static final String COL_PM_MN = "mangan";  // in mg
    private static final String COL_PM_NA = "natrium";  // in mg
    private static final String COL_PM_P = "phosphor";  // in mg
    private static final String COL_PM_ZN = "zinc";  // in mg
    private static final String COL_PM_VIT_A = "vit_a"; // in mg
    private static final String COL_PM_VIT_B1 = "vit_b1"; // in mg
    private static final String COL_PM_VIT_B2 = "vit_b2"; // in mg
    private static final String COL_PM_VIT_B3 = "vit_b3";  // (Niacin) in mg
    private static final String COL_PM_VIT_B5 = "vit_b5";  // (Pantothensäure) in mg
    private static final String COL_PM_VIT_B6 = "vit_b6";  // in mg
    private static final String COL_PM_VIT_B7 = "vit_b7";  // (Biotin) in mg
    private static final String COL_PM_VIT_B11 = "vit_b11";  // (Folsäure, B9) in mg
    private static final String COL_PM_VIT_B12 = "vit_b12";  // in mg
    private static final String COL_PM_VIT_C = "vit_c";  // in mg
    private static final String COL_PM_VIT_E = "vit_e";  // in mg
    private static final String COL_PM_VIT_K = "vit_k";  // in mg
    private static final String COL_PM_VIT_H = "vit_h";  // (Biotin) in mg

    private static final String TABLE_PMC = "meal_categories";
    private static final String COL_PMC_NAME = "name";

    // Table meals per day
    private static final String TABLE_CM = "consumed_meals";
    private static final String COL_CM_DATE = "date";
    private static final String COL_CM_INDEX = "meal_index";  // Refers to uuid-index of a food from foods-table
    private static final String COL_CM_AMOUNT = "amount";

    // Table body-data
    private static final String TABLE_BD = "bodydata";
    private static final String COLUMN_BD_DATE = "date";
    private static final String COLUMN_BD_WEIGHT = "weight";
    private static final String COLUMN_BD_CHEST = "chest";
    private static final String COLUMN_BD_BELLY = "belly";
    private static final String COLUMN_BD_BUTT = "butt";
    private static final String COLUMN_BD_WAIST = "waist";
    private static final String COLUMN_BD_ARM_R = "arm_right";
    private static final String COLUMN_BD_ARM_L = "arm_left";
    private static final String COLUMN_BD_LEG_R = "leg_right";
    private static final String COLUMN_BD_LEG_L = "leg_left";

    // Table exercises
    private static final String TABLE_WP = "workout_plans";
    private static final String COL_WP_NAME = "plan_name";

    private static final String TABLE_WR = "workout_routines";
    private static final String COL_WR_PLAN_NAME = "plan_name";
    private static final String COL_WR_ROUTINE_NAME = "routine_name";

    private static final String TABLE_WE = "exercises";
    private static final String COL_WE_PLAN_NAME = "plan_name";
    private static final String COL_WE_ROUTINE_NAME = "routine";
    private static final String COL_WE_EXERCISE_NAME = "exercise_name";
    private static final String COL_WE_SETS = "sets";
    private static final String COL_WE_REPETITIONS = "repetitions";
    private static final String COL_WE_WEIGHT = "weight";

    // Table settings
    private static final String TABLE_S_GOAL = "settings_goals";
    private static final String COL_S_INDEX = "settings_index";
    private static final String COL_S_GOAL_CALORIES = "goal_calories";
    private static final String COL_S_GOAL_FAT = "goal_fat";
    private static final String COL_S_GOAL_CARBS = "goal_carbs";
    private static final String COL_S_GOAL_PROTEIN = "goal_protein";

    private static final String TABLE_S_LANG = "settings_lang";
    private static final String COL_S_LANG = "language";


    // Constructor ---------------------------------------------------------------------------------
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Class default overwrite methods -------------------------------------------------------------

    /** This method will be called upon creation of the database. This method will create all the
     * necessary tables inside the database and prepopulate some tables.
     *
     * @param sqLiteDatabase: SQLiteDatabase that is created
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create table body-data
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_BD + " ("
                + COLUMN_BD_DATE + " TEXT PRIMARY KEY, "
                + COLUMN_BD_WEIGHT + " REAL, "
                + COLUMN_BD_CHEST + " REAL, "
                + COLUMN_BD_BELLY + " REAL, "
                + COLUMN_BD_BUTT + " REAL, "
                + COLUMN_BD_WAIST + " REAL, "
                + COLUMN_BD_ARM_R + " REAL, "
                + COLUMN_BD_ARM_L + " REAL, "
                + COLUMN_BD_LEG_R + " REAL, "
                + COLUMN_BD_LEG_L + " REAL);"
        );

        // Create table food-data
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_PM + " ("
                 + COL_PM_INDEX + " TEXT PRIMARY KEY, "
                 + COL_PM_NAME + " TEXT, "
                 + COL_PM_CATEGORY + " TEXT, "
                 + COL_PM_CALORIES + " REAL, "
                 + COL_PM_FAT + " REAL, "
                 + COL_PM_FAT_SAT + " REAL, "
                 + COL_PM_CARBS + " REAL, "
                 + COL_PM_SUGAR + " REAL, "
                 + COL_PM_PROTEIN + " REAL, "
                 + COL_PM_SALT + " REAL, "
                 + COL_PM_FIBER + " REAL, "
                 + COL_PM_CHOL + " REAL, "
                 + COL_PM_CREATINE + " REAL, "
                 + COL_PM_CA + " REAL, "
                 + COL_PM_FE + " REAL, "
                 + COL_PM_K + " REAL, "
                 + COL_PM_MG + " REAL, "
                 + COL_PM_MN + " REAL, "
                 + COL_PM_NA + " REAL, "
                 + COL_PM_P + " REAL, "
                 + COL_PM_ZN + " REAL, "
                 + COL_PM_VIT_A + " REAL, "
                 + COL_PM_VIT_B1 + " REAL, "
                 + COL_PM_VIT_B2 + " REAL, "
                 + COL_PM_VIT_B3 + " REAL, "
                 + COL_PM_VIT_B5 + " REAL, "
                 + COL_PM_VIT_B6 + " REAL, "
                 + COL_PM_VIT_B7 + " REAL, "
                 + COL_PM_VIT_B11 + " REAL, "
                 + COL_PM_VIT_B12 + " REAL, "
                 + COL_PM_VIT_C + " REAL, "
                 + COL_PM_VIT_E + " REAL, "
                 + COL_PM_VIT_K + " REAL, "
                 + COL_PM_VIT_H + " REAL);"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PMC + " (" + COL_PMC_NAME + " TEXT PRIMARY KEY);");

        // Create table dailymeals
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CM + " ("
                + COL_CM_DATE + " TEXT, "
                + COL_CM_INDEX + " TEXT, "
                + COL_CM_AMOUNT + " REAL, " +
                "PRIMARY KEY (" + COL_CM_INDEX + ", " + COL_CM_AMOUNT + "));"
        );

        // Create tables exercises
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WE + " ("
                + COL_WE_PLAN_NAME + " TEXT, "
                + COL_WE_ROUTINE_NAME + " TEXT, "
                + COL_WE_EXERCISE_NAME + " TEXT, "
                + COL_WE_SETS + " INTEGER, "
                + COL_WE_REPETITIONS + " INTEGER, "
                + COL_WE_WEIGHT + " REAL"
                + ");");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WP + " ("+ COL_WP_NAME + " TEXT PRIMARY KEY);");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WR + " ("+ COL_WR_PLAN_NAME + " TEXT, " + COL_WR_ROUTINE_NAME + " TEXT, PRIMARY KEY (" + COL_WR_PLAN_NAME + ", " + COL_WR_ROUTINE_NAME + "));");

        // Create table settings
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_S_GOAL + " ("
                + COL_S_INDEX + " INTEGER PRIMARY KEY, "
                + COL_S_GOAL_CALORIES + " REAL, "
                + COL_S_GOAL_FAT + " REAL, "
                + COL_S_GOAL_CARBS + " REAL, "
                + COL_S_GOAL_PROTEIN + " REAL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_S_LANG + " ("
                + COL_S_INDEX + " INTEGER PRIMARY KEY, "
                + COL_S_LANG + " TEXT);");

        // Add preset data to tables ---------------------------------------------------------------

        // Preset meals
        // -> Format: index + name + 6 main values + 22 optional values
        // -> ('index', 'name', 'category', cal, fat, fatsat, carbs, sugar, protein, salt, fiber, cholesterin, creatine, ca, fe, k_potassium, mg, mn, na_sodium, phosphor, zn, vita, vitb1, b2, b3, b5, b6, b7, b11, b12, c, e, k, h)
        // -> Preset meals indices have always 1 digit more (7 digits in total) than user created meals to prevent overlaps
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000000', 'Apple (100 g)', 'Fruits and Vegetables', 52, 0.17, 0, 13.81, 10.39, 0.26, 0, 2.4, 0, 0, 6, 0.12, 107, 5, 0.035, 1, 11, 0.04, 0.003, 0.017, 0.026, 0.091, 0.061, 0.041, 0, 0, 0, 4.6, 0.18, 0.022, 0)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000001', 'Banana (100 g)', 'Fruits and Vegetables', 95.0, 0.33, 0.0, 22.84, 12.23, 1.0, 0.0, 2.6, 0.0, 0.0, 5.0, 0.26, 358.0, 27.0, 0.0, 0.0, 22.0, 0.15, 0.003, 0.031, 0.073, 0.665, 0.334, 0.367, 0.0, 0.0, 0.0, 8.7, 0.0, 0.0, 0.0)");

        // Add meal categories
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Fruits and Vegetables');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Drinks');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Grains and Cereals');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Spices, Sauces, Oils');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Veggie Products');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Sweets and Spread');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Animal Products');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Convenience Foods');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Supplements');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Custom');");

        // Workout plan names
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WP + " VALUES('Example Workout Plan')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WP + " VALUES('My Workout Plan')");

        // Workout routine names
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WR + " VALUES('Example Workout Plan', 'Push Day')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WR + " VALUES('Example Workout Plan', 'Pull Day')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WR + " VALUES('Example Workout Plan', 'Leg Day')");

        // Workout exercises
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Example Workout Plan', 'Push Day', 'Chest Press', 3, 6, 20)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Example Workout Plan', 'Push Day', 'Shoulder Press', 3, 6, 20)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Example Workout Plan', 'Pull Day', 'Lat Pull Down', 3, 8, 20)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Example Workout Plan', 'Leg Day', 'Leg Press', 3, 6, 20)");

        // Settings
        // -> (index, calories, carbs, fat, protein). First value is index. Must always be 0.
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_S_GOAL + " VALUES(0, 2500, 200, 100, 160)");

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_S_LANG + " VALUES(0, 'system')");
    }

    /** This method will be called upon upgrading the database from one version to a higher one.
     *
     * @param sqLiteDatabase: SQLiteDatabase; The SQLiteDatabase to upgrade.
     * @param oldVersion: Integer; The old version number of the database to upgrade from.
     * @param newVersion: Integer; The new version number of the database to upgrade to.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Delete old tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PMC);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_S_GOAL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_S_LANG);

        // Create new database
        onCreate(sqLiteDatabase);
    }


    // Meals Query's -------------------------------------------------------------------------------

    public Cursor getPresetMealsSimpleAllCategories() {
        // -> Return index, name, calories from table "foods" in ascending order by name

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_PM_INDEX + ", " + COL_PM_NAME + ", " + COL_PM_CALORIES + " FROM " + TABLE_PM + " ORDER BY " + COL_PM_NAME +  " ASC LIMIT 100;",
                    null
            );
        }

        return cursor;
    }

    public Cursor getPresetMealsSimpleFromCategory(String category) {
        // -> Return index, name, calories from specified category table "foods" in ascending order by name

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_PM_INDEX + ", " + COL_PM_NAME + ", " + COL_PM_CALORIES + " FROM " + TABLE_PM + " WHERE " + COL_PM_CATEGORY + "='" + category + "'" + " ORDER BY " + COL_PM_NAME +  " ASC;",
                    null
            );
        }

        return cursor;
    }

    public Cursor getPresetMealDetails(String foodUUID) {
        // -> Returns all Details for a preset meal with given UUID

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_PM + " WHERE " + COL_PM_INDEX + "='" + foodUUID + "';", null);
        }

        return cursor;
    }

    public Cursor getPresetMealCategories() {
        // -> Returns all Details for a preset meal with given UUID

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + COL_PMC_NAME + " FROM " + TABLE_PMC + " ORDER BY " + COL_PMC_NAME + " ASC;", null);
        }

        return cursor;
    }

    public Cursor getConsumedMeals(String date) {
        // -> Returns index, name, calories, amount for all consumed meals for a given date

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            // SELECT food.food_index, food.name, food.calories, dailymeals.amount FROM dailymeals LEFT JOIN food ON dailymeals.food_index=food.food_index WHERE dailymeals.date=date;
            // Returns -> | food.food_index | food.name | food.cal | dailymeals.amount |
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + TABLE_PM + "." + COL_PM_INDEX + ", " + TABLE_PM + "." + COL_PM_NAME + ", " + TABLE_PM + "." + COL_PM_CALORIES + ", " + TABLE_CM + "." + COL_CM_AMOUNT + " " +
                            "FROM " + TABLE_CM + " " +
                            "LEFT JOIN " + TABLE_PM + " ON " + TABLE_CM + "." + COL_CM_INDEX + "=" + TABLE_PM + "." + COL_PM_INDEX + " " +
                            "WHERE " + TABLE_CM + "." + COL_CM_DATE + "='" + date + "';",
                    null);
        }

        return cursor;
    }

    public Cursor getConsumedMealsSums(String date) {
        // -> Returns sum of all details of all consumed meals for a given date

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {

            // SELECT (food.calories * dailymeals.amount) [other columns here] FROM dailymeals LEFT JOIN food ON dailymeals.food_index=food.food_index WHERE dailymeals.date=date;
            // Returns -> | food.food_index | food.name | food.cal | food.fat | food.fat_sat | food.carbs | food.sugar | food.protein | dailymeals.amount |
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " +
                            "SUM (" + TABLE_PM + "." + COL_PM_CALORIES + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_FAT + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_FAT_SAT + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_CARBS + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_SUGAR + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_PROTEIN + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +

                            "SUM (" + TABLE_PM + "." + COL_PM_SALT + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_FIBER + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_CHOL + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_CREATINE + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +

                            "SUM (" + TABLE_PM + "." + COL_PM_CA + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_FE + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_K + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_MG + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_MN + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_NA + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_P + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_ZN + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +

                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_A + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B1 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B2 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B3 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B5 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B6 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B7 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B11 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_B12 + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_C + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_E + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_K + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_VIT_H + " * " + TABLE_CM + "." + COL_CM_AMOUNT + ") " +

                            "FROM " + TABLE_CM + " " +
                            "LEFT JOIN " + TABLE_PM + " ON " + TABLE_CM + "." + COL_CM_INDEX + "=" + TABLE_PM + "." + COL_PM_INDEX + " " +
                            "WHERE " + TABLE_CM + "." + COL_CM_DATE + "='" + date + "';",
                    null);
        }

        return cursor;
    }

    public void addOrReplacePresetMeal(String uuid, String name, String category, double[] data) {
        // -> Inserts new preset meal to database

        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create content values to put into the database
        ContentValues cv = new ContentValues();

        cv.put(COL_PM_INDEX, uuid);
        cv.put(COL_PM_NAME, name);
        cv.put(COL_PM_CATEGORY, category);
        cv.put(COL_PM_CALORIES, data[0]);
        cv.put(COL_PM_FAT, data[1]);
        cv.put(COL_PM_FAT_SAT, data[2]);
        cv.put(COL_PM_CARBS, data[3]);
        cv.put(COL_PM_SUGAR, data[4]);
        cv.put(COL_PM_PROTEIN, data[5]);
        cv.put(COL_PM_SALT, data[6]);
        cv.put(COL_PM_FIBER, data[7]);
        cv.put(COL_PM_CHOL, data[8]);
        cv.put(COL_PM_CREATINE, data[9]);
        cv.put(COL_PM_CA, data[10]);
        cv.put(COL_PM_FE, data[11]);
        cv.put(COL_PM_K, data[12]);
        cv.put(COL_PM_MG, data[13]);
        cv.put(COL_PM_MN, data[14]);
        cv.put(COL_PM_NA, data[15]);
        cv.put(COL_PM_P, data[16]);
        cv.put(COL_PM_ZN, data[17]);
        cv.put(COL_PM_VIT_A, data[18]);
        cv.put(COL_PM_VIT_B1, data[19]);
        cv.put(COL_PM_VIT_B2, data[20]);
        cv.put(COL_PM_VIT_B3, data[21]);
        cv.put(COL_PM_VIT_B5, data[22]);
        cv.put(COL_PM_VIT_B6, data[23]);
        cv.put(COL_PM_VIT_B7, data[24]);
        cv.put(COL_PM_VIT_B11, data[25]);
        cv.put(COL_PM_VIT_B12, data[26]);
        cv.put(COL_PM_VIT_C, data[27]);
        cv.put(COL_PM_VIT_E, data[28]);
        cv.put(COL_PM_VIT_K, data[29]);
        cv.put(COL_PM_VIT_H, data[30]);

        // Insert data into database
        long result = sqLiteDatabase.replaceOrThrow(TABLE_PM, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        }

    }

    public void addOrReplaceConsumedMeal(String date, String mealUUID, double amount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_CM_INDEX, mealUUID);
        cv.put(COL_CM_DATE, date);
        cv.put(COL_CM_AMOUNT, amount);

        sqLiteDatabase.replaceOrThrow(TABLE_CM, null, cv);
    }

    public void removeConsumedMeal(String date, String mealUUID) {
        // Remove entry from DailyMealsTable with date and UUID
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.delete(TABLE_CM, COL_CM_INDEX + "= ? AND " + COL_CM_DATE + "= ?", new String[] {mealUUID, date});
            // sqLiteDatabase.delete(TABLE_NAME_M, COLUMN_M_INDEX + "='" + mealUUID + "' AND " + COLUMN_M_DATE + "='" + date + "'", null);
            // sqLiteDatabase.rawQuery("DELETE FROM " + TABLE_NAME_M + " WHERE " + COLUMN_M_INDEX + "='" + mealUUID + "' AND " + COLUMN_M_DATE + "='" + date + "';", null);
        }
    }

    // Workout Query's -----------------------------------------------------------------------------

    public Cursor getWorkoutPlans() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT * FROM " + TABLE_WP + ";", null);
        }

        return cursor;
    }

    public Cursor getWorkoutRoutines(String planName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            // cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + COL_WE_ROUTINE_NAME + " FROM " + TABLE_WE + " WHERE " + COL_WE_PLAN_NAME + "='" + plan +"';", null);
            cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + COL_WR_ROUTINE_NAME + " FROM " + TABLE_WR + " WHERE " + COL_WR_PLAN_NAME + "='" + planName + "';", null);
        }

        return cursor;
    }

    public Cursor getWorkoutExercises(String plan, String routine) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_WE_EXERCISE_NAME + ", " + COL_WE_SETS + ", " + COL_WE_REPETITIONS + ", " + COL_WE_WEIGHT +
                            " FROM " + TABLE_WE +
                            " WHERE " + COL_WE_PLAN_NAME + "='" + plan + "' AND " + COL_WE_ROUTINE_NAME + "='" + routine +"';",
                    null);
        }

        return cursor;
    }

    public void addWorkoutPlan(String planName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("INSERT OR REPLACE INTO " + TABLE_WP + " VALUES('" + planName + "')");
        }
    }

    public void addWorkoutRoutine(String planName, String routineName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_WR_PLAN_NAME, planName);
            cv.put(COL_WR_ROUTINE_NAME, routineName);

            sqLiteDatabase.replaceOrThrow(TABLE_WR, null, cv);
        }
    }

    public void addWorkoutExercise(String planName, String routineName, String exerciseName, int sets, int repetitions, double weight) {
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create content values to put into the database
        ContentValues cv = new ContentValues();
        cv.put(COL_WE_PLAN_NAME, planName);
        cv.put(COL_WE_ROUTINE_NAME, routineName);
        cv.put(COL_WE_EXERCISE_NAME, exerciseName);
        cv.put(COL_WE_SETS, sets);
        cv.put(COL_WE_REPETITIONS, repetitions);
        cv.put(COL_WE_WEIGHT, weight);

        long result = sqLiteDatabase.replaceOrThrow(TABLE_WE, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateWorkoutPlanName(String oldPlanName, String newPlanName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (sqLiteDatabase != null) {
            // Remove old plan name from table "plans"
            sqLiteDatabase.delete(TABLE_WP, COL_WP_NAME + "= ?", new String[] {oldPlanName});

            // Add new plan name to table "plans"
            ContentValues cv = new ContentValues();
            cv.put(COL_WP_NAME, newPlanName);
            sqLiteDatabase.insertOrThrow(TABLE_WP, null, cv);

            // Update plan names in table "exercises"
            cv = new ContentValues();
            cv.put(COL_WE_PLAN_NAME, newPlanName);
            sqLiteDatabase.update(TABLE_WE, cv, COL_WE_PLAN_NAME + "= ?", new String[]{oldPlanName});

            // Update plan names in table "routines"
            cv = new ContentValues();
            cv.put(COL_WR_PLAN_NAME, newPlanName);
            sqLiteDatabase.update(TABLE_WR, cv, COL_WR_PLAN_NAME + "= ?", new String[]{oldPlanName});
        }
    }

    public void updateWorkoutRoutineName(String planName, String oldRoutineName, String newRoutineName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (sqLiteDatabase != null) {
            // Update routine name in table "routines"
            ContentValues cv = new ContentValues();
            cv.put(COL_WR_ROUTINE_NAME, newRoutineName);
            sqLiteDatabase.update(TABLE_WR, cv, COL_WR_PLAN_NAME + "= ? AND " + COL_WR_ROUTINE_NAME + "= ?", new String[] {planName, oldRoutineName});

            // Update routine names in table "exercises"
            cv = new ContentValues();
            cv.put(COL_WE_ROUTINE_NAME, newRoutineName);
            sqLiteDatabase.update(TABLE_WE, cv, COL_WE_ROUTINE_NAME + "= ?", new String[]{oldRoutineName});
        }
    }

    public void deleteWorkoutPlan(String planName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Remove plan name from table "plans"
        sqLiteDatabase.delete(TABLE_WP, COL_WP_NAME + "= ?", new String[] {planName});

        // Remove all entries for this plan in table "exercises"
        sqLiteDatabase.delete(TABLE_WE, COL_WE_PLAN_NAME + "= ?", new String[] {planName});
    }

    public void deleteWorkoutRoutine(String planName, String routineName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Remove routine name from table "routines"
        sqLiteDatabase.delete(TABLE_WR, COL_WR_PLAN_NAME + "= ? AND " + COL_WR_ROUTINE_NAME + "= ?", new String[] {planName, routineName});

        // Remove all entries for this routine in table "exercises"
        sqLiteDatabase.delete(TABLE_WE, COL_WE_PLAN_NAME + "= ? AND " + COL_WE_ROUTINE_NAME + "= ?", new String[] {planName, routineName});
    }

    public void deleteWorkoutExercise(String planName, String routineName, String exerciseName) {
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.delete(TABLE_WE, COL_WE_PLAN_NAME + "= ? AND " + COL_WE_ROUTINE_NAME + "= ? AND " + COL_WE_EXERCISE_NAME + "= ?", new String[] {planName, routineName, exerciseName});
            // sqLiteDatabase.rawQuery("DELETE FROM " + TABLE_NAME_WE + " WHERE " + COL_E_PLAN_NAME + "='" + planName + "' AND " + COL_E_ROUTINE_NAME + "='" + routineName + "' AND " + COL_E_EXERCISE_NAME + "='" + exerciseName +"';", null);
        }

    }

    // Settings Query's ----------------------------------------------------------------------------

    public Cursor getSettingsGoals() {
        // -> Read settings from table "settings_goals"

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_S_GOAL_CALORIES + ", " + COL_S_GOAL_FAT + ", " + COL_S_GOAL_CARBS + ", " + COL_S_GOAL_PROTEIN +
                    " FROM " + TABLE_S_GOAL +
                    " WHERE " + COL_S_INDEX + "=0;",
                    null);
        }

        return cursor;
    }

    public Cursor getSettingsLanguage() {
        // -> Read settings from table "settings_lang"

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_S_LANG + " FROM " + TABLE_S_LANG + " WHERE " + COL_S_INDEX + "=0;",
                    null);
        }

        return cursor;
    }

    public void setSettingsGoals(double goalCalories, double goalFat, double goalCarbs, double goalProtein) {
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create content values to put into the database
        ContentValues cv = new ContentValues();

        cv.put(COL_S_INDEX, 0);
        cv.put(COL_S_GOAL_CALORIES, goalCalories);
        cv.put(COL_S_GOAL_FAT, goalFat);
        cv.put(COL_S_GOAL_CARBS, goalCarbs);
        cv.put(COL_S_GOAL_PROTEIN, goalProtein);

        long result = sqLiteDatabase.replaceOrThrow(TABLE_S_GOAL, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save settings", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved settings", Toast.LENGTH_SHORT).show();
        }
    }

    public void setSettingsLanguage(String language) {
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create content values to put into the database
        ContentValues cv = new ContentValues();

        cv.put(COL_S_INDEX, 0);
        cv.put(COL_S_LANG, language);

        long result = sqLiteDatabase.replaceOrThrow(TABLE_S_LANG, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save settings", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved settings. Close App to apply changes.", Toast.LENGTH_SHORT).show();
        }
    }

    // Body-Data Query's ---------------------------------------------------------------------------

    public void addDataBody(String date, double weight, double chest, double belly, double butt,
                            double waist, double arm_r, double arm_l, double leg_r, double leg_l){
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create content values to put into the database
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BD_DATE, date);
        cv.put(COLUMN_BD_WEIGHT, weight);
        cv.put(COLUMN_BD_CHEST, chest);
        cv.put(COLUMN_BD_BELLY, belly);
        cv.put(COLUMN_BD_BUTT, butt);
        cv.put(COLUMN_BD_WAIST, waist);
        cv.put(COLUMN_BD_ARM_R, arm_r);
        cv.put(COLUMN_BD_ARM_L, arm_l);
        cv.put(COLUMN_BD_LEG_R, leg_r);
        cv.put(COLUMN_BD_LEG_L, leg_l);

        // Insert data into dadabase
        long result = sqLiteDatabase.replaceOrThrow(TABLE_BD, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        }
    }

}
