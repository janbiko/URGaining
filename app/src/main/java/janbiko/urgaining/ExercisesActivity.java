package janbiko.urgaining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

public class ExercisesActivity extends Activity {

    private String workoutName;
    private FloatingActionButton addExerciseButton;

    private ListView exerciseNamesList;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    private WorkoutsDatabase workoutsDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        getWorkoutName();
        //Toast.makeText(this, workoutName, Toast.LENGTH_LONG).show();

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
    }

    private void initListViews() {
        exerciseNamesList = (ListView) findViewById(R.id.names_list);
        exerciseNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                removeExerciseAtPosition(position);
                return true;
            }
        });

        exerciseNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Item clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        listAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                listItems);
        exerciseNamesList.setAdapter(listAdapter);
        fillListView();
    }

    private void removeExerciseAtPosition(int position) {
        if (listItems.get(position) != null) {
            workoutsDB.removeExerciseItem(listItems.get(position));
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
