package janbiko.urgaining;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jannik on 17.09.2017.
 */

public class ExerciseDetails extends Activity{

    private String exerciseName;
    private int sets;
    private TextView exercise;

    private WorkoutsDatabase workoutsDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise_details);
        setPopupWindowSize();
        getExerciseName();
        initDatabase();
        getSets();
        initUI();
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open();
    }

    private void initUI() {
        initTextViews();
        createEditTextsAndTextViews();
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
            valuesInput.addView(editText, layoutParamsEditTexts);
        }
    }

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
        Toast.makeText(this, "" + sets, Toast.LENGTH_SHORT).show();
    }

    private void setPopupWindowSize() {
        // getting screen size of used device and setting popup window size in relation to given size
        float popupWindowWidth = 0.9f;
        float popupWindowHeight = 0.9f;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * popupWindowWidth),
                (int) (height * popupWindowHeight));
    }
}
