package janbiko.urgaining;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jannik on 17.09.2017.
 */

public class ExerciseDetails extends Activity{

    private String exerciseName;
    private int sets;
    private TextView exercise;
    private Button addValuesButton;

    private WorkoutsDatabase workoutsDB;

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
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open();
    }

    private void initUI() {
        initTextViews();
        createEditTextsAndTextViews();
        initButtons();
    }

    private void initButtons() {
        addValuesButton = (Button) findViewById(R.id.add_values_button);
        addValuesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValuesToDatabase();
                //finish();
            }
        });
    }

    private void addValuesToDatabase(){

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
