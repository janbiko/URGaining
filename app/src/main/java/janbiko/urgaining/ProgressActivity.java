package janbiko.urgaining;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    private WorkoutsDatabase workoutsDB;

    private ExpandableListView workoutsList;
    private android.widget.ExpandableListAdapter listAdapter;
    private ArrayList<String> workouts;
    private HashMap<String, List<String>> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.navigation_progress:
                        break;
                    case R.id.navigation_workout:
                        Intent iW = new Intent(ProgressActivity.this, WorkoutsActivity.class);
                        startActivity(iW);
                        break;
                    case R.id.navigation_settings:
                        Intent iS = new Intent(ProgressActivity.this, SettingsActivity.class);
                        startActivity(iS);
                        break;
                }
                return true;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDatabase();
        initUI();
    }

    private void initUI() {
        workoutsList = (ExpandableListView) findViewById(R.id.workouts_list);

        workouts = workoutsDB.getAllWorkoutItems();
        exercises = new HashMap<>();

        for (int i = 0; i < workouts.size(); i++) {
            List<String> tempExsList = new ArrayList<>();
            for (int j = 0; j < workoutsDB.getAllExerciseItems().size(); j++) {
                if (workoutsDB.getAllExerciseItems().get(j).getWorkoutName()
                        .equals(workouts.get(i))){
                    tempExsList.add(workoutsDB.getAllExerciseItems().get(j).getName());
                }
            }
            exercises.put(workouts.get(i), tempExsList);

        }

        listAdapter = new ExpandableListAdapter(this, workouts, exercises);

        workoutsList.setAdapter(listAdapter);
        workoutsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //Toast.makeText(getApplicationContext(),exercises.get(workouts.get(groupPosition))
                // .get(childPosition), Toast.LENGTH_SHORT).show();
                feedGraph(exercises.get(workouts.get(groupPosition)).get( childPosition));
                return false;
            }
        });
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open();
    }

    private void feedGraph(String exercise){
        List<Entry> oneRMEntries = new ArrayList<>();
        if(workoutsDB.getAllExerciseValuesItems(exercise).size() > 0){
            ArrayList<ArrayList<Float>> exercises = workoutsDB.getAllExerciseValuesItems(exercise);

            int oneRMFirstEntry = 0;
            int oneRMLastEntry = 0;
            int oneRM;
            int graphIndex = 0;
            for (int i = 0; i < exercises.size(); i++) {
                oneRM = epleyFormula(exercises.get(i).get(0), exercises.get(i).get(1));
                graphIndex++;
                oneRMEntries.add(new Entry(graphIndex, oneRM));
                if (i == 0) oneRMFirstEntry = oneRM;
                if (i == exercises.size() - 1) oneRMLastEntry = oneRM;
            }

            float percIncrease = calculatePercentageIncrease(oneRMFirstEntry, oneRMLastEntry);

            LineChart lineChart = (LineChart) findViewById(R.id.progress_chart);
            setGraphDescription(lineChart, percIncrease);
            LineDataSet dataSetLast = new LineDataSet(oneRMEntries, "1RM");

            LineData lineData = new LineData();
            lineData.addDataSet(dataSetLast);
            lineChart.setData(lineData);

            setGraphAxisStyle(lineChart, oneRMEntries.size());
            setDataSetLastStyle(dataSetLast);

            lineChart.invalidate();     //refresh chart
        }
        else {
            Toast.makeText(getApplicationContext(), "No data available.", Toast.LENGTH_LONG).show();
        }
    }

    private void setGraphDescription(LineChart lineChart, float percIncrease) {
        Description description = new Description();
        if (percIncrease >= 0) {
            description.setText(""+ percIncrease + "% 1RM increase since first record");
        }
        else {
            description.setText(""+ Math.abs(percIncrease) + "% 1RM decrease since first record");
        }
        lineChart.setDescription(description);
    }

    private float calculatePercentageIncrease (float start, float end) {
        float value = (end / start - 1) * 10000;
        // round to 2 decimals
        int val = (int) value;

        return (float) val / 100;
    }

    private int epleyFormula (float weight, float repetitions) {
        return Math.round(weight * (1 + (repetitions / 30)));
    }

    private void setGraphAxisStyle(LineChart lineChart, int dataSize){
        float minYValue = 80f;
        float maxYValue = 180f;

        YAxis right = lineChart.getAxisRight();
        right.setDrawGridLines(false);
        right.setDrawLabels(false);

        YAxis left = lineChart.getAxisLeft();
        //left.setAxisMinimum(minYValue);
        //left.setAxisMaximum(maxYValue);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(dataSize, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(false);
    }

    private void setDataSetLastStyle(LineDataSet dataSet){
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setValueTextSize(10f);
        dataSet.setColor(Color.GREEN);
        dataSet.setCircleRadius(5f);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setLineWidth(2.5f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }


}
