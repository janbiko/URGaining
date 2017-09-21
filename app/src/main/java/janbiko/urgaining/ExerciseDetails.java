package janbiko.urgaining;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
import android.text.InputType;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jannik on 17.09.2017.
 */

public class ExerciseDetails extends Activity{

    private int exerciseValuesSize = 16;
    private float deloadValue;

    private String exerciseName;
    private int sets;
    private WorkoutsDatabase workoutsDB;

    private LinearLayout setCounter1;
    private LinearLayout kg1;
    private LinearLayout reps1;
    private LinearLayout kg2;
    private LinearLayout reps2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise_details);
        setPopupWindowSize();
        getExerciseName();
        initDatabase();
        getSets();
        getDeloadValue();
        initUI();

    }

    private void getDeloadValue() {
        // gets deload value from settings

        SettingsActivity settings = new SettingsActivity();
        deloadValue = settings.getDeloadValue(this);
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open();
    }

    private void initUI() {
        initTextViews();
        initButtons();
        deleteUnusedViews();
        fillInLatestExerciseValues();
        setFocusChangedListeners();
    }

    private void setFocusChangedListeners() {
        // if user puts in a weight value in the first, second, ... EditText the value is
        // automatically transferred to all EditTexts after it

        for (int i = 1; i < kg1.getChildCount(); i++) {
            final EditText editText = (EditText) kg1.getChildAt(i);
            final int x = i;
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        for (int j = x +1; j < kg1.getChildCount(); j++) {
                            EditText sibling = (EditText) kg1.getChildAt(j);
                            sibling.setText(editText.getText().toString());
                        }
                    }
                }
            });
        }
    }

    private void fillInLatestExerciseValues() {
        // gets the latest exercise values from the database and puts them in the corresponding
        // TextViews

        ArrayList<Float> latestValues = workoutsDB.getLatestExerciseValuesItem(exerciseName);
        if (latestValues.size() == exerciseValuesSize) {
            int j = 0;
            for (int i = 1; i < kg2.getChildCount(); i++) {
                TextView kgTextView = (TextView) kg2.getChildAt(i);
                kgTextView.setText(latestValues.get(j).toString());

                TextView repsTextView = (TextView) reps2.getChildAt(i);
                int val = Math.round(latestValues.get(j + 1));
                repsTextView.setText("" + val);

                j += 2;
            }
        }
    }

    private void deleteUnusedViews() {
        // deletes all Views unused views, if the exercise has less than the maximum amount of sets

        setCounter1 = (LinearLayout) findViewById(R.id.set_counter_1);
        LinearLayout setCounter2 = (LinearLayout) findViewById(R.id.set_counter_2);
        kg1 = (LinearLayout) findViewById(R.id.kg1);
        kg2 = (LinearLayout) findViewById(R.id.kg2);
        reps1 = (LinearLayout) findViewById(R.id.reps1);
        reps2 = (LinearLayout) findViewById(R.id.reps2);

        for (int i = setCounter1.getChildCount() - 1; i > sets; i--) {
            setCounter1.removeViewAt(i);
            setCounter2.removeViewAt(i);
            kg1.removeViewAt(i);
            kg2.removeViewAt(i);
            reps1.removeViewAt(i);
            reps2.removeViewAt(i);
        }
    }

    private void initButtons() {
        Button addValuesButton = (Button) findViewById(R.id.add_values_button);
        addValuesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValuesToDatabase();
            }
        });

        Button deloadButton = (Button) findViewById(R.id.deload_button);
        deloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeloadValues();
            }
        });
    }

    private void setDeloadValues() {
        // calculates new weight based on the last used weight and the set deload value and puts
        // it in all weight EditTexts

        TextView textView = (TextView) kg2.getChildAt(1);
        float newValue = Math.round(Float.parseFloat(textView.getText().toString()) * deloadValue);

        for (int i = 1; i < kg1.getChildCount(); i++) {
            EditText editText = (EditText) kg1.getChildAt(i);
            editText.setText("" + newValue);
        }
    }

    private void addValuesToDatabase(){
        // saves exercise values to the database

        ArrayList<Float> exerciseValues = getExerciseValues();
        long timeStamp = System.currentTimeMillis() / 100;

        // checks if user put in a value for every set
        if (exerciseValues.size() != sets * 2) {
            Toast.makeText(getApplicationContext(), "Please enter valid numbers.",
                    Toast.LENGTH_SHORT).show();
        } else if (exerciseValues.size() == sets * 2 && exerciseValues.size() <=
                exerciseValuesSize) {

            // fills in default for unused sets, if the exercise consists of less than the
            // maximum amount of sets
            while (exerciseValues.size() < exerciseValuesSize) {
                exerciseValues.add(-1f);
            }

            Toast.makeText(getApplicationContext(), "Added successfully.", Toast.LENGTH_SHORT).show();
            workoutsDB.insertExerciseValuesItem(exerciseName, exerciseValues, timeStamp);
            finish();
        }
    }

    private ArrayList<Float> getExerciseValues() {
        // gets the exercise values from EditTexts and saves them in an ArrayList, then returns
        // the ArrayList
        // {weight1, reps1, weight2, reps2, ...}

        ArrayList<Float> values = new ArrayList<>();
        EditText editText;
        float value;

        for (int i = 1; i < setCounter1.getChildCount(); i++) {
            editText = (EditText) kg1.getChildAt(i);
            if (editText.getText().toString().equals("")) {
                return values;
            }
            value = Float.parseFloat(editText.getText().toString());
            values.add(value);

            editText = (EditText) reps1.getChildAt(i);
            if (editText.getText().toString().equals("")) {
                return values;
            }
            value = Float.parseFloat(editText.getText().toString());
            values.add(value);
        }

        return values;
    }

    private void initTextViews() {
        TextView exercise = (TextView) findViewById(R.id.name_exercise);
        exercise.setText(exerciseName);
    }

    private void getExerciseName() {
        // gets the exercise name from intent extra

        exerciseName = getIntent().getStringExtra("ExerciseName");
    }

    private void getSets() {
        // gets the amount of sets for the current exercise from the database

        for (int i = 0; i < workoutsDB.getAllExerciseItems().size(); i++) {
            if (workoutsDB.getAllExerciseItems().get(i).getName().equals(exerciseName)) {
                sets = workoutsDB.getAllExerciseItems().get(i).getSets();
                break;
            }
        }
    }

    private void setPopupWindowSize() {
        // getting screen size of used device and setting popup window size in relation to given
        // size

        float popupWindowWidth = 1f;
        float popupWindowHeight = 0.79f;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * popupWindowWidth),
                (int) (height * popupWindowHeight));
    }

}
