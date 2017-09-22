package janbiko.urgaining;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jannik on 15.09.2017.
 */

public class ExercisesActivity extends AppCompatActivity {

    private static final String ALERT_MESSAGE = "Do you want to delete this exercise?";
    private static final String ALERT_POSITIVE_BUTTON = "Yes";
    private static final String ALERT_NEGATIVE_BUTTON = "No";

    private String workoutName;

    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    private WorkoutsDatabase workoutsDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        getWorkoutName();

        initDatabase();
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshArrayList();
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open();
    }

    private void initUI() {
        initButtons();
        initListViews();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.navigation_progress:
                        Intent iP = new Intent(ExercisesActivity.this, ProgressActivity.class);
                        startActivity(iP);
                        break;
                    case R.id.navigation_workout:
                        Intent iW = new Intent(ExercisesActivity.this, WorkoutsActivity.class);
                        startActivity(iW);
                        break;
                    case R.id.navigation_settings:
                        Intent iS = new Intent(ExercisesActivity.this, SettingsActivity.class);
                        startActivity(iS);
                        break;
                }
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(workoutName);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initListViews() {
        // fills the ListView with the corresponding exercise names and sets Listeners

        ListView exerciseNamesList = (ListView) findViewById(R.id.names_list);
        exerciseNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                // creates an alert dialog onLongClick

                createAlertDialog(position);
                return true;
            }
        });

        exerciseNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // launch "ExerciseDetails" activity with the exercise's name as extra

                Intent i = new Intent(ExercisesActivity.this, ExerciseDetails.class);
                i.putExtra("ExerciseName", listItems.get(position));
                startActivity(i);
            }
        });

        listAdapter = new ArrayAdapter<>(this, R.layout.workout_list_item,
                listItems);
        exerciseNamesList.setAdapter(listAdapter);
        fillListView();
    }

    private void createAlertDialog(final int position) {
        // creates an alert dialog to ask the user, if he wants to delete the selected exercise

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(ALERT_MESSAGE);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(ALERT_POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeExerciseAtPosition(position);
            }
        });
        alertBuilder.setNegativeButton(ALERT_NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void removeExerciseAtPosition(int position) {
        // deletes the delivered exercise and its values from the database

        if (listItems.get(position) != null) {
            workoutsDB.removeExerciseItem(listItems.get(position));
            workoutsDB.removeExerciseValues(listItems.get(position));
            refreshArrayList();
        }
    }

    private void refreshArrayList() {
        ArrayList<String> tempList = new ArrayList<>();
        for (int i = 0; i < workoutsDB.getAllExerciseItems().size(); i++) {
            if (workoutsDB.getAllExerciseItems().get(i).getWorkoutName().equals(workoutName)) {
                tempList.add(workoutsDB.getAllExerciseItems().get(i).getName());
            }
        }
        listItems.clear();
        listItems.addAll(tempList);
        listAdapter.notifyDataSetChanged();
    }

    private void fillListView() {
        // gets all exercise names from current workout from the database and adds them to the
        // ListView

        ArrayList<String> exerciseNames = new ArrayList<>();
        for (int i = 0; i < workoutsDB.getAllExerciseItems().size(); i++) {
            if (workoutsDB.getAllExerciseItems().get(i).getWorkoutName().equals(workoutName)) {
                exerciseNames.add(workoutsDB.getAllExerciseItems().get(i).getName());
            }
        }
        listItems.addAll(exerciseNames);
        listAdapter.notifyDataSetChanged();
    }

    private void initButtons() {
        FloatingActionButton addExerciseButton = (FloatingActionButton) findViewById(R.id.add_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launching "AddExercisePopup" activity with the workout name as extra

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
