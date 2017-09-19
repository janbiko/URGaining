package janbiko.urgaining;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Jannik on 14.09.2017.
 */

public class WorkoutsDatabase {
    private static final String DATABASE_NAME = "workouts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE_WORKOUTS = "workouts";
    private static final String DATABASE_TABLE_EXERCISES = "exercises";
    private static final String DATABASE_TABLE_EXERCISE_VALUES = "exercisevalues";

    private static final String KEY_ID = "_id";
    private static final String KEY_WORKOUT = "workout";
    private static final String KEY_EXERCISE = "exercise";
    private static final String KEY_SETS = "sets";
    private static final String KEY_WORKOUT_NAME = "workoutname";

    private static final String KEY_TIME = "time";
    private static final String KEY_WEIGHT_1 = "weight1";
    private static final String KEY_SET_1 = "set1";
    private static final String KEY_WEIGHT_2 = "weight2";
    private static final String KEY_SET_2 = "set2";
    private static final String KEY_WEIGHT_3 = "weight3";
    private static final String KEY_SET_3 = "set3";
    private static final String KEY_WEIGHT_4 = "weight4";
    private static final String KEY_SET_4 = "set4";
    private static final String KEY_WEIGHT_5 = "weight5";
    private static final String KEY_SET_5 = "set5";
    private static final String KEY_WEIGHT_6 = "weight6";
    private static final String KEY_SET_6 = "set6";
    private static final String KEY_WEIGHT_7 = "weight7";
    private static final String KEY_SET_7 = "set7";
    private static final String KEY_WEIGHT_8 = "weight8";
    private static final String KEY_SET_8 = "set8";



    // Columns
    private static final int COLUMN_WORKOUT_INDEX = 1;

    private static final int COLUMN_EXERCISE_INDEX = 1;
    private static final int COLUMN_SETS_INDEX = 2;
    private static final int COLUMN_WORKOUT_NAME_INDEX = 3;

    private static final int COLUMN_EXERCISE_NAME_INDEX = 1;
    private static final int COLUMN_TIMESTAMP = 2;
    private static final int COLUMN_WEIGHT_1 = 3;
    private static final int COLUMN_SET_1 = 4;
    private static final int COLUMN_WEIGHT_2 = 5;
    private static final int COLUMN_SET_2 = 6;
    private static final int COLUMN_WEIGHT_3 = 7;
    private static final int COLUMN_SET_3 = 8;
    private static final int COLUMN_WEIGHT_4 = 9;
    private static final int COLUMN_SET_4 = 10;
    private static final int COLUMN_WEIGHT_5 = 11;
    private static final int COLUMN_SET_5 = 12;
    private static final int COLUMN_WEIGHT_6 = 13;
    private static final int COLUMN_SET_6 = 14;
    private static final int COLUMN_WEIGHT_7 = 15;
    private static final int COLUMN_SET_7 = 16;
    private static final int COLUMN_WEIGHT_8 = 17;
    private static final int COLUMN_SET_8 = 18;



    private WorkoutDBOpenHelper workoutDBHelper;
    private SQLiteDatabase db;


    public WorkoutsDatabase(Context context) {
        workoutDBHelper = new WorkoutDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        try {
            db = workoutDBHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = workoutDBHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public long insertWorkoutItem(String item) {
        ContentValues itemValues = new ContentValues();
        itemValues.put(KEY_WORKOUT, item);
        return db.insert(DATABASE_TABLE_WORKOUTS, null, itemValues);
    }

    public long insertExerciseItem(Exercise item) {
        ContentValues itemValues = new ContentValues();
        itemValues.put(KEY_EXERCISE, item.getName());
        itemValues.put(KEY_SETS, item.getSets());
        itemValues.put(KEY_WORKOUT_NAME, item.getWorkoutName());
        return db.insert(DATABASE_TABLE_EXERCISES, null, itemValues);
    }

    public long insertExerciseValuesItem(String exerciseName, ArrayList<Float> exerciseValues,
                                         long timeStamp) {
        ContentValues itemValues = new ContentValues();
        itemValues.put(KEY_EXERCISE, exerciseName);
        itemValues.put(KEY_TIME, timeStamp);
        itemValues.put(KEY_WEIGHT_1, exerciseValues.get(0));
        itemValues.put(KEY_SET_1, exerciseValues.get(1));
        itemValues.put(KEY_WEIGHT_2, exerciseValues.get(2));
        itemValues.put(KEY_SET_2, exerciseValues.get(3));
        itemValues.put(KEY_WEIGHT_3, exerciseValues.get(4));
        itemValues.put(KEY_SET_3, exerciseValues.get(5));
        itemValues.put(KEY_WEIGHT_4, exerciseValues.get(6));
        itemValues.put(KEY_SET_4, exerciseValues.get(7));
        itemValues.put(KEY_WEIGHT_5, exerciseValues.get(8));
        itemValues.put(KEY_SET_5, exerciseValues.get(9));
        itemValues.put(KEY_WEIGHT_6, exerciseValues.get(10));
        itemValues.put(KEY_SET_6, exerciseValues.get(11));
        itemValues.put(KEY_WEIGHT_7, exerciseValues.get(12));
        itemValues.put(KEY_SET_7, exerciseValues.get(13));
        itemValues.put(KEY_WEIGHT_8, exerciseValues.get(14));
        itemValues.put(KEY_SET_8, exerciseValues.get(15));

        return db.insert(DATABASE_TABLE_EXERCISE_VALUES, null, itemValues);
    }

    public void removeWorkoutItem(String item) {
        String toDelete = KEY_WORKOUT + "=?";
        String[] deleteArguments = new String[]{item};
        db.delete(DATABASE_TABLE_WORKOUTS, toDelete, deleteArguments);
    }

    public void removeExerciseItem(String item) {
        String toDelete = KEY_EXERCISE + "=?";
        String[] deleteArguments = new String[]{item};
        db.delete(DATABASE_TABLE_EXERCISES, toDelete, deleteArguments);
    }

    public void removeExerciseValues(String exerciseName) {
        String toDelete = KEY_EXERCISE + "=?";
        String[] deleteArguments = new String[]{exerciseName};
        db.delete(DATABASE_TABLE_EXERCISE_VALUES, toDelete, deleteArguments);
    }

    public ArrayList<String> getAllWorkoutItems() {
        ArrayList<String> items = new ArrayList<>();
        Cursor cursor = db.query(DATABASE_TABLE_WORKOUTS, new String[] {
                KEY_ID, KEY_WORKOUT}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String workout = cursor.getString(COLUMN_WORKOUT_INDEX);

                items.add(workout);
            } while (cursor.moveToNext());

        }

        return items;
    }

    public ArrayList<Exercise> getAllExerciseItems() {
        ArrayList<Exercise> items = new ArrayList<Exercise>();
        Cursor cursor = db.query(DATABASE_TABLE_EXERCISES, new String[] { KEY_ID, KEY_EXERCISE,
        KEY_SETS, KEY_WORKOUT_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String exercise = cursor.getString(COLUMN_EXERCISE_INDEX);
                int sets = cursor.getInt(COLUMN_SETS_INDEX);
                String workoutName = cursor.getString(COLUMN_WORKOUT_NAME_INDEX);

                items.add(new Exercise(exercise, sets, workoutName));
            } while (cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<ArrayList<Float>> getAllExerciseValuesItems(String exerciseName) {
        ArrayList<ArrayList<Float>> exerciseValues = new ArrayList<>();
        Cursor cursor = db.query(DATABASE_TABLE_EXERCISE_VALUES, new String[] {
                KEY_ID, KEY_EXERCISE, KEY_TIME, KEY_WEIGHT_1, KEY_SET_1, KEY_WEIGHT_2, KEY_SET_2,
                KEY_WEIGHT_3, KEY_SET_3, KEY_WEIGHT_4, KEY_SET_4, KEY_WEIGHT_5, KEY_SET_5,
                KEY_WEIGHT_6, KEY_SET_6, KEY_WEIGHT_7, KEY_SET_7, KEY_WEIGHT_8, KEY_SET_8}, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String exercise = cursor.getString(COLUMN_EXERCISE_NAME_INDEX);
                if (exercise.equals(exerciseName)) {
                    ArrayList<Float> tempList = new ArrayList<>();
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_1));
                    tempList.add(cursor.getFloat(COLUMN_SET_1));
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_2));
                    tempList.add(cursor.getFloat(COLUMN_SET_2));
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_3));
                    tempList.add(cursor.getFloat(COLUMN_SET_3));
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_4));
                    tempList.add(cursor.getFloat(COLUMN_SET_4));
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_5));
                    tempList.add(cursor.getFloat(COLUMN_SET_5));
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_6));
                    tempList.add(cursor.getFloat(COLUMN_SET_6));
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_7));
                    tempList.add(cursor.getFloat(COLUMN_SET_7));
                    tempList.add(cursor.getFloat(COLUMN_WEIGHT_8));
                    tempList.add(cursor.getFloat(COLUMN_SET_8));
                    exerciseValues.add(tempList);
                }

            } while (cursor.moveToNext());
        }

        return exerciseValues;
    }

    public ArrayList<Float> getLatestExerciseValuesItem(String exerciseName) {
        ArrayList<Float> latestExerciseValues = new ArrayList<>();
        ArrayList<Long> timestamps = new ArrayList<>();
        long latestTimestamp = 0;

        Cursor cursor = db.query(DATABASE_TABLE_EXERCISE_VALUES, new String[] {
                KEY_ID, KEY_EXERCISE, KEY_TIME, KEY_WEIGHT_1, KEY_SET_1, KEY_WEIGHT_2, KEY_SET_2,
                KEY_WEIGHT_3, KEY_SET_3, KEY_WEIGHT_4, KEY_SET_4, KEY_WEIGHT_5, KEY_SET_5,
                KEY_WEIGHT_6, KEY_SET_6, KEY_WEIGHT_7, KEY_SET_7, KEY_WEIGHT_8, KEY_SET_8}, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String exercise = cursor.getString(COLUMN_EXERCISE_NAME_INDEX);
                if (exercise.equals(exerciseName)) {
                    long timestamp = cursor.getLong(COLUMN_TIMESTAMP);
                    timestamps.add(timestamp);
                }
            } while (cursor.moveToNext());
        }

        for (int i = 0; i < timestamps.size(); i++) {
            if (timestamps.get(i) > latestTimestamp) latestTimestamp = timestamps.get(i);
        }

        if (cursor.moveToFirst()) {
            do {
                long timestamp = cursor.getLong(COLUMN_TIMESTAMP);
                if (timestamp == latestTimestamp) {
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_1));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_1));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_2));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_2));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_3));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_3));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_4));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_4));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_5));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_5));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_6));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_6));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_7));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_7));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_WEIGHT_8));
                    latestExerciseValues.add(cursor.getFloat(COLUMN_SET_8));
                    break;
                }
            } while (cursor.moveToNext());
        }

        return latestExerciseValues;
    }

    private class WorkoutDBOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE_WORKOUTS + " (" + KEY_ID +
                " integer primary key autoincrement, " + KEY_WORKOUT +
                " text not null);";

        private static final String DATABASE_CREATE_EXERCISES = "create table " +
                DATABASE_TABLE_EXERCISES + " (" + KEY_ID +
                " integer primary key autoincrement, " + KEY_EXERCISE +
                " text not null, " + KEY_SETS + " not null, " + KEY_WORKOUT_NAME +
                " text not null);";

        private static final String  DATABASE_CREATE_EXERCISE_VALUES = "create table " +
                DATABASE_TABLE_EXERCISE_VALUES + " (" + KEY_ID +
                " integer primary key autoincrement, " + KEY_EXERCISE +
                " text not null, " + KEY_TIME + " integer not null, " + KEY_WEIGHT_1 +
                " float, " + KEY_SET_1 + " integer, " + KEY_WEIGHT_2 + " float, " + KEY_SET_2 +
                " integer, " + KEY_WEIGHT_3 + " float, " + KEY_SET_3 + " integer, " + KEY_WEIGHT_4 +
                " float, " + KEY_SET_4 + " integer, " + KEY_WEIGHT_5 + " float, " + KEY_SET_5 +
                " integer, " + KEY_WEIGHT_6 + " float, " + KEY_SET_6 + " integer, " + KEY_WEIGHT_7 +
                " float, " + KEY_SET_7 + " integer, " + KEY_WEIGHT_8 + " float, " + KEY_SET_8 +
                " integer);";

        public WorkoutDBOpenHelper(Context c, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbName, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_EXERCISES);
            db.execSQL(DATABASE_CREATE_EXERCISE_VALUES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    /*
    private class ExerciseDBOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE_EXERCISES + " (" + KEY_ID +
                " integer primary key autoincrement, " + KEY_EXERCISE +
                " text not null, " + KEY_SETS + " not null);";

        public ExerciseDBOpenHelper(Context c, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbName, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }*/
}


