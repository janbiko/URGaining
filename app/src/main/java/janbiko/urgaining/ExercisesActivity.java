package janbiko.urgaining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jannik on 15.09.2017.
 */

public class ExercisesActivity extends Activity {

    private String workoutName;
    private FloatingActionButton addExerciseButton;
    private ListView exerciseNamesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        getWorkoutName();
        //Toast.makeText(this, workoutName, Toast.LENGTH_LONG).show();
        
        initUI();
    }

    private void initUI() {
        initButtons();
        initListViews();
    }

    private void initListViews() {
        exerciseNamesList = (ListView) findViewById(R.id.names_list);
        exerciseNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                return false;
            }
        });

        exerciseNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Item clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButtons() {
        addExerciseButton = (FloatingActionButton) findViewById(R.id.add_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExercisesActivity.this, AddExercisePopup.class);
                i.putExtra("WorkoutName", workoutName);
                startActivity(i);
            }
        });
    }

    public void getWorkoutName() {
        workoutName = getIntent().getStringExtra("WorkoutName");
    }
}
