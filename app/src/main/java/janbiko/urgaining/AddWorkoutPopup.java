package janbiko.urgaining;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Jannik on 13.09.2017.
 */

public class AddWorkoutPopup extends Activity{

    private Button createWorkoutButton;
    private EditText nameInput;

    private WorkoutsDatabase workoutsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_add_workout);
        setPopupWindowSize();

        initDatabase();
        initUI();
    }

    private void initDatabase() {
        workoutsDatabase = new WorkoutsDatabase(this);
        workoutsDatabase.open("workouts");
    }

    private void initUI() {
        initButtons();
        initEditTexts();
    }

    private void initEditTexts() {
        nameInput = (EditText) findViewById(R.id.workout_name);
    }

    private void initButtons() {
        createWorkoutButton = (Button) findViewById(R.id.create_workout_button);
        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store workout name in database to display it in "WorkoutsActivity"
                saveNameToDatabase();
                finish();
            }
        });
    }

    private void saveNameToDatabase() {
        // if the entered workout name is not empty and doesn't already exist in the database, it
        // is stored in the database
        String workoutName = nameInput.getText().toString();
        if (workoutName.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid name.",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!workoutAlreadyExisting(workoutName)) {
            workoutsDatabase.insertWorkoutItem(workoutName);
            Toast.makeText(getApplicationContext(), workoutName + " created.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Workout name is already taken.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean workoutAlreadyExisting(String name) {
        // checks if workoutname already exists in the database
        for (int i = 0; i < workoutsDatabase.getAllWorkoutItems().size(); i++) {
            if (name.equals(workoutsDatabase.getAllWorkoutItems().get(i))) return true;
        }
        return false;
    }

    private void setPopupWindowSize() {
        // getting screen size of used device and setting popup window size in relation to given size
        float popupWindowWidth = 0.75f;
        float popupWindowHeight = 0.25f;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * popupWindowWidth),
                (int) (height * popupWindowHeight));
    }
}
