package janbiko.urgaining;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Jannik on 13.09.2017.
 */

public class WorkoutsActivity extends Activity
{

    private FloatingActionButton addRoutineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        initUI();
    }

    private void initUI() {
        initButtons();
    }

    private void initButtons() {
        addRoutineButton = (FloatingActionButton) findViewById(R.id.add_routine_button);
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add Routine", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
