package janbiko.urgaining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

/**
 * Created by Jannik on 13.09.2017.
 */

public class WorkoutsActivity extends Activity
{

    private FloatingActionButton addRoutineButton;
    private WorkoutsDatabase workoutsDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        initDatabase();
        initUI();
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open();
    }

    private void initUI() {
        initButtons();
    }

    private void initButtons() {
        addRoutineButton = (FloatingActionButton) findViewById(R.id.add_routine_button);
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WorkoutsActivity.this, AddWorkoutPopup.class);
                startActivity(i);

            }
        });
    }

}
