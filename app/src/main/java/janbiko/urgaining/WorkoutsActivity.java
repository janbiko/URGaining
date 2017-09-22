package janbiko.urgaining;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Jannik on 13.09.2017.
 */

public class WorkoutsActivity extends AppCompatActivity
{

    private static final String ALERT_POSITIVE_BUTTON = "Yes";
    private static final String ALERT_NEGATIVE_BUTTON = "No";

    private WorkoutsDatabase workoutsDB;

    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

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
                        Intent iP = new Intent(WorkoutsActivity.this, ProgressActivity.class);
                        startActivity(iP);
                        break;
                    case R.id.navigation_workout:
                        Intent iW = new Intent(WorkoutsActivity.this, WorkoutsActivity.class);
                        startActivity(iW);
                        break;
                    case R.id.navigation_settings:
                        Intent iS = new Intent(WorkoutsActivity.this, SettingsActivity.class);
                        startActivity(iS);
                        break;
                }
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initListViews() {
        // fills the ListView with the workout names and sets Listeners

        ListView workoutNamesList = (ListView) findViewById(R.id.names_list);
        workoutNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // creates an alert dialog onLongClick

                createAlertDialog(position, listItems.get(position));
                return true;
            }
        });
        workoutNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // launch "ExerciseActivity" workout's name as extra

                Intent i = new Intent(WorkoutsActivity.this, ExercisesActivity.class);
                i.putExtra("WorkoutName", listItems.get(position));
                startActivity(i);
            }
        });


        listAdapter = new ArrayAdapter<>(this, R.layout.workout_list_item,
                listItems);
        workoutNamesList.setAdapter(listAdapter);
        fillListView();
    }

    private void createAlertDialog(final int position, String workoutName) {
        // creates an alert dialog to ask the user, if he wants to delete the selected workout

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Do you want to delete " + workoutName + " workout?");
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(ALERT_POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeWorkoutAtPosition(position);
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

    private void removeWorkoutAtPosition(int position) {
        // deletes the delivered workout from the database and its corresponding exercises

        if (listItems.get(position) != null) {
            removeExercises(listItems.get(position));
            workoutsDB.open();
            workoutsDB.removeWorkoutItem(listItems.get(position));
            workoutsDB.close();
            refreshArrayList();
        }
    }

    private void removeExercises(String workoutName) {
        // gets all exercises associated with the selected workout and deletes them and its values
        // from the database

        ArrayList<String> exerciseNames = new ArrayList<>();
        workoutsDB.open();
        for (int i = 0; i < workoutsDB.getAllExerciseItems().size(); i++) {
            if (workoutsDB.getAllExerciseItems().get(i).getWorkoutName().equals(workoutName)) {
                exerciseNames.add(workoutsDB.getAllExerciseItems().get(i).getName());
            }
        }

        for (int i = 0; i < exerciseNames.size(); i++) {
            workoutsDB.removeExerciseValues(exerciseNames.get(i));
            workoutsDB.removeExerciseItem(exerciseNames.get(i));
        }
        workoutsDB.close();
    }

    private void refreshArrayList(){
        workoutsDB.open();
        ArrayList tempList = workoutsDB.getAllWorkoutItems();
        workoutsDB.close();
        listItems.clear();
        listItems.addAll(tempList);
        listAdapter.notifyDataSetChanged();
    }

    private void fillListView() {
        // gets all workout names from the database and adds them to the ListView
        workoutsDB.open();
        listItems.addAll(workoutsDB.getAllWorkoutItems());
        workoutsDB.close();
        listAdapter.notifyDataSetChanged();
    }

    private void initButtons() {
        FloatingActionButton addRoutineButton = (FloatingActionButton) findViewById(R.id.add_button);
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launching "AddWorkoutPopup" activity

                Intent i = new Intent(WorkoutsActivity.this, AddWorkoutPopup.class);
                startActivity(i);

            }
        });
    }

}
