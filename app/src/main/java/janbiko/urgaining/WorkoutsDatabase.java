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

    private static final String KEY_ID = "_id";
    private static final String KEY_WORKOUT = "workout";
    private static final String KEY_EXERCISE = "exercise";
    private static final String KEY_SETS = "sets";
    private static final String KEY_WORKOUT_NAME = "workoutname";

    private static final int COLUMN_WORKOUT_INDEX = 1;
    private static final int COLUMN_EXERCISE_INDEX = 1;
    private static final int COLUMN_SETS_INDEX = 2;
    private static final int COLUMN_WORKOUT_NAME_INDEX = 3;

    private WorkoutDBOpenHelper workoutDBHelper;
    //private ExerciseDBOpenHelper exerciseDBOpenHelper;
    private SQLiteDatabase db;


    public WorkoutsDatabase(Context context) {
        workoutDBHelper = new WorkoutDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        //exerciseDBOpenHelper = new ExerciseDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open(/*String database*/) throws SQLException {
        //if (database.equals(DATABASE_TABLE_WORKOUTS)) {
            try {
                db = workoutDBHelper.getWritableDatabase();
            } catch (SQLException e) {
                db = workoutDBHelper.getReadableDatabase();
            }
        /*} else if (database.equals(DATABASE_TABLE_EXERCISES)) {
            try {
                db = exerciseDBOpenHelper.getWritableDatabase();
            } catch (SQLException e) {
                db = exerciseDBOpenHelper.getReadableDatabase();
            }
        } else {
            throw new SQLException();
        }*/
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

    public void removeWorkoutItem(String item) {
        String toDelete = KEY_WORKOUT + "=?";
        String[] deleteArguments = new String[]{item};
        db.delete(DATABASE_TABLE_WORKOUTS, toDelete, deleteArguments);
    }

    public void removeExerciseItem(Exercise item) {
        String toDelete = KEY_EXERCISE + "=?";
        String[] deleteArguments = new String[]{item.getName()};
        db.delete(DATABASE_TABLE_EXERCISES, toDelete, deleteArguments);
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

        public WorkoutDBOpenHelper(Context c, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbName, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_EXERCISES);
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


