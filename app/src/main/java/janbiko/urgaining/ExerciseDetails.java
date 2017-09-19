package janbiko.urgaining;

import android.app.Activity;
import android.os.Bundle;
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

    private String exerciseName;
    private int sets;
    private TextView exercise;
    private Button addValuesButton;
    private LinearLayout exerciseDetails;
    private WorkoutsDatabase workoutsDB;

    private LinearLayout setCounter1;
    private LinearLayout kg1;
    private LinearLayout reps1;
    private LinearLayout kg2;
    private LinearLayout reps2;

    //private HashMap<String, Integer> idsMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise_details);
        setPopupWindowSize();
        getExerciseName();
        initDatabase();
        getSets();
        initUI();

        //TEST();
    }
    /*
    private void TEST() {
        ArrayList<ArrayList<Float>> test = workoutsDB.getAllExerciseValuesItems(exerciseName);
        for (int i = 0; i < test.size(); i++) {
            for (int j = 0; j < test.get(i).size(); j++) {
                Log.i("Liste "+i+", Wert "+j+":", test.get(i).get(j).toString());
            }
        }
    }*/


    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open();
    }

    private void initUI() {
        //exerciseDetails = (LinearLayout) findViewById(R.id.exercise_details);
        initTextViews();
        //createEditTextsAndTextViews();
        //createStoredValuesTextViews();
        initButtons();
        deleteUnusedViews();
        fillInLatestExerciseValues();
    }

    private void fillInLatestExerciseValues() {
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
        setCounter1 = (LinearLayout) findViewById(R.id.set_counter_1);
        LinearLayout setCounter2 = (LinearLayout) findViewById(R.id.set_counter_2);
        kg1 = (LinearLayout) findViewById(R.id.kg1);
        kg2 = (LinearLayout) findViewById(R.id.kg2);
        reps1 = (LinearLayout) findViewById(R.id.reps1);
        reps2 = (LinearLayout) findViewById(R.id.reps2);
        //int i = setCounter1.getChildCount() - 1;
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
        addValuesButton = (Button) findViewById(R.id.add_values_button);
        addValuesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValuesToDatabase();
            }
        });
    }

    private void addValuesToDatabase(){
        ArrayList<Float> exerciseValues = getExerciseValues();
        long timeStamp = System.currentTimeMillis() / 100;
        if (exerciseValues.size() != sets * 2) {
            Toast.makeText(getApplicationContext(), "Please enter valid numbers.",
                    Toast.LENGTH_SHORT).show();
        } else if (exerciseValues.size() == sets * 2 && exerciseValues.size() <= exerciseValuesSize) {
            while (exerciseValues.size() < exerciseValuesSize) {
                exerciseValues.add(-1f);
            }
            Toast.makeText(getApplicationContext(), "Added succesfully.", Toast.LENGTH_SHORT).show();
            workoutsDB.insertExerciseValuesItem(exerciseName, exerciseValues, timeStamp);
            finish();
        }

        ArrayList<Float> test = workoutsDB.getLatestExerciseValuesItem(exerciseName);
        for (int i = 0; i < test.size(); i++) {
            Log.i("Wert " + i + ":", test.get(i).toString());
        }

        /* for Testing
        for (int i = 0; i < exerciseValues.size(); i++) {
            Log.i("Wert " + i +":", exerciseValues.get(i).toString());
        } */
    }

    private ArrayList<Float> getExerciseValues() {
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
    /*
    private void createStoredValuesTextViews() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        int layoutMarginTopPX = 15;
        int layoutMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                layoutMarginTopPX, getResources().getDisplayMetrics());
        layoutParams.setMargins(0, layoutMarginTop, 0, 0);

        for (int i = 0; i < pseudoWert; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            TextView weightTextView = createWeightTextView();
            linearLayout.addView(weightTextView, createWeightLayoutParams());
            for (int j = 0; j < sets; j++) {
                TextView repsTextView = createValuesTextView();
                linearLayout.addView(repsTextView, createRepsLayoutParams());
            }
            exerciseDetails.addView(linearLayout, layoutParams);
        }
    }

    private LinearLayout.LayoutParams createWeightLayoutParams() {
        int textViewWidthPX = 25;
        int textViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textViewWidthPX, getResources().getDisplayMetrics());
        int textViewMarginEndPX = 14;
        int textViewMarginEnd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textViewMarginEndPX, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                textViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginEnd(textViewMarginEnd);

        return layoutParams;
    }

    private LinearLayout.LayoutParams createRepsLayoutParams() {
        int textViewWidthPX = 25;
        int textViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textViewWidthPX, getResources().getDisplayMetrics());
        int textViewMarginStartPX = 8;
        int textViewMarginStart= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textViewMarginStartPX, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                textViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(textViewMarginStart);

        return layoutParams;
    }

    private TextView createValuesTextView() {
        TextView textView = new TextView(this);
        //TODO: remove later
        textView.setText("5");

        return textView;
    }

    private TextView createWeightTextView() {
        TextView textView =  new TextView(this);
        //TODO: remove later
        textView.setText("100");

        return textView;
    }

    private void createEditTextsAndTextViews() {
        LinearLayout titles = (LinearLayout) findViewById(R.id.titles);
        LinearLayout valuesInput = (LinearLayout) findViewById(R.id.values_input);
        int marginLeftTextViewsPX = 5;
        int layoutWidthEditTextsPX = 23;
        int marginLeftEditTextsPX = 10;
        int editTextTextSize = 12;

        LinearLayout.LayoutParams layoutParamsTextViews = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // converting from px to dp
        int marginLeftTextViews = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                marginLeftTextViewsPX, getResources().getDisplayMetrics());
        layoutParamsTextViews.setMargins(marginLeftTextViews, 0, 0, 0);

        // converting from px to dp
        int layoutWidthEditTexts = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                layoutWidthEditTextsPX, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParamsEditTexts = new LinearLayout.LayoutParams(
                layoutWidthEditTexts, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // converting from px to dp
        int marginLeftEditTexts = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                marginLeftEditTextsPX, getResources().getDisplayMetrics());
        layoutParamsEditTexts.setMargins(marginLeftEditTexts, 0, 0, 0);

        for (int i = 1; i <= sets; i++) {
            TextView textView = new TextView(this);
            textView.setText("Set" + i);
            titles.addView(textView, layoutParamsTextViews);

            EditText editText = new EditText(this);
            editText.setHint(R.string.values_hint);
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setTextSize(editTextTextSize);
            editText.setSingleLine(true);

            editText.setId(assignId(i));

            valuesInput.addView(editText, layoutParamsEditTexts);
        }
    }

    private int assignId(int i) {
        if (i == 1) return R.id.id1;
        else if (i == 2) return R.id.id2;
        else if (i == 3) return R.id.id3;
        else if (i == 4) return R.id.id4;
        else if (i == 5) return R.id.id5;
        else if (i == 6) return R.id.id6;
        else if (i == 7) return R.id.id7;
        else return R.id.id8;
    }   */

    private void initTextViews() {
        exercise = (TextView) findViewById(R.id.name_exercise);
        exercise.setText(exerciseName);
    }

    private void getExerciseName() {
        exerciseName = getIntent().getStringExtra("ExerciseName");
    }

    private void getSets() {
        for (int i = 0; i < workoutsDB.getAllExerciseItems().size(); i++) {
            if (workoutsDB.getAllExerciseItems().get(i).getName().equals(exerciseName)) {
                sets = workoutsDB.getAllExerciseItems().get(i).getSets();
                break;
            }
        }
        //Toast.makeText(this, "" + sets, Toast.LENGTH_SHORT).show();
    }

    private void setPopupWindowSize() {
        // getting screen size of used device and setting popup window size in relation to given size
        float popupWindowWidth = 0.95f;
        float popupWindowHeight = 0.95f;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * popupWindowWidth),
                (int) (height * popupWindowHeight));
    }

}
