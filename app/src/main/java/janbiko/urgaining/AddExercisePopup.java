package janbiko.urgaining;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by Jannik on 15.09.2017.
 */

public class AddExercisePopup extends Activity{

    private EditText nameInput;
    private Spinner setCountSpinner;

    private String workoutName;
    private WorkoutsDatabase workoutsDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_add_exercise);
        setPopupWindowSize();
        getWorkoutName();
        initDatabase();
        initUI();
    }

    private void getWorkoutName(){
        workoutName = getIntent().getStringExtra("WorkoutName");
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
    }

    private void initUI() {
        initEditTexts();
        initSpinner();
        initButtons();
    }

    private void initEditTexts() {
        nameInput = (EditText) findViewById(R.id.exercise_name);
    }

    private void initButtons() {
        Button createExerciseButton = (Button) findViewById(R.id.create_exercise_button);
        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExerciseToDatabase();
                finish();
            }
        });
    }

    private void saveExerciseToDatabase() {
        // creates new Exercise object from "nameInput" and "setCountSpinner" values and saves it
        // to the database, if the the exercise name is not an empty string and doesn't already
        // exist

        String exerciseName = nameInput.getText().toString();
        int sets = (int) setCountSpinner.getSelectedItem();
        Exercise exercise = new Exercise(exerciseName, sets, workoutName);

        if (exerciseName.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid name.",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!exerciseAlreadyExisting(exerciseName)) {
            workoutsDB.open();
            workoutsDB.insertExerciseItem(exercise);
            workoutsDB.close();
            Toast.makeText(getApplicationContext(), exerciseName + " created.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Exercise name is already taken.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean exerciseAlreadyExisting(String name) {
        // checks if exercise name already exists in the database

        workoutsDB.open();
        for (int i = 0; i < workoutsDB.getAllExerciseItems().size(); i++) {
            if (name.equals(workoutsDB.getAllExerciseItems().get(i).getName())) return true;
        }
        workoutsDB.close();
        return false;
    }

    private void initSpinner() {
        // initializes setCountSpinner and feeds it with values via an adapter

        setCountSpinner = (Spinner) findViewById(R.id.set_count_spinner);
        Integer[] values = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8};
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(AddExercisePopup.this,
                R.layout.support_simple_spinner_dropdown_item, values);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        setCountSpinner.setAdapter(spinnerAdapter);
    }

    private void setPopupWindowSize() {
        // getting screen size of used device and setting popup window size in relation to given
        // size

        float popupWindowWidth = 0.75f;
        float popupWindowHeight = 0.35f;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * popupWindowWidth),
                (int) (height * popupWindowHeight));
    }
}
