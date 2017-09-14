package janbiko.urgaining;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Jannik on 14.09.2017.
 */

public class WorkoutsDatabase {
    private static final String DATABASE_NAME = "workouts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE_WORKOUTS = "workouts";

    private static final String KEY_ID = "_id";
    private static final String KEY_WORKOUT = "workout";

    private static final int COLUMN_WORKOUT_INDEX = 1;

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

    public void removeWorkoutItem(String item) {
        String toDelete = KEY_WORKOUT + "=?";
        String[] deleteArguments = new String[]{item};
        db.delete(DATABASE_TABLE_WORKOUTS, toDelete, deleteArguments);
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



    private class WorkoutDBOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE_WORKOUTS + " (" + KEY_ID +
                " integer primary key autoincrement, " + KEY_WORKOUT +
                " text not null);";

        public WorkoutDBOpenHelper(Context c, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbName, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}


