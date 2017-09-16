package janbiko.urgaining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jannik on 13.09.2017.
 */

public class WorkoutsActivity extends Activity
{

    private FloatingActionButton addRoutineButton;
    private WorkoutsDatabase workoutsDB;
    private ListView workoutNamesList;

    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        initDatabase();
        initUI();
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open(/*"workouts"*/);
    }

    private void initUI() {
        initButtons();
        initListViews();
    }

    private void initListViews() {
        workoutNamesList = (ListView) findViewById(R.id.names_list);
        workoutNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                removeWorkoutAtPosition(position);
                return true;
            }
        });
        workoutNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(), "Show " + listItems.get(position) +
                //        " workout.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(WorkoutsActivity.this, ExercisesActivity.class);
                i.putExtra("WorkoutName", listItems.get(position));
                startActivity(i);
            }
        });


        listAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                listItems);
        workoutNamesList.setAdapter(listAdapter);
        fillListView();
    }

    private void removeWorkoutAtPosition(int position) {
        if (listItems.get(position) != null) {
            workoutsDB.removeWorkoutItem(listItems.get(position));
            refreshArrayList();
        }
    }

    private void refreshArrayList(){
        ArrayList tempList = workoutsDB.getAllWorkoutItems();
        listItems.clear();
        listItems.addAll(tempList);
        listAdapter.notifyDataSetChanged();
    }

    private void fillListView() {
        listItems.addAll(workoutsDB.getAllWorkoutItems());
        listAdapter.notifyDataSetChanged();
    }

    private void initButtons() {
        addRoutineButton = (FloatingActionButton) findViewById(R.id.add_button);
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WorkoutsActivity.this, AddWorkoutPopup.class);
                startActivity(i);

            }
        });
    }

}
