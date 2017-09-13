package janbiko.urgaining;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Jannik on 13.09.2017.
 */

public class AddWorkoutPopup extends Activity{

    private float popupWindowWidth = 0.75f;
    private float popupWindowsHeight = 0.25f;
    private Button createWorkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_add_workout);
        setPopupWindowSize();
        initUI();
    }

    private void initUI() {
        initButtons();
    }

    private void initButtons() {
        createWorkoutButton = (Button) findViewById(R.id.create_workout_button);
        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store workout name in database to display it in "WorkoutsActivity"
                Toast.makeText(getApplicationContext(), "Workout created.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setPopupWindowSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * popupWindowWidth), (int) (height * popupWindowsHeight));
    }
}
