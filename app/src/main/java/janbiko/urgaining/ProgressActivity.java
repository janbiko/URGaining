package janbiko.urgaining;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    public WorkoutsDatabase workoutsDB;

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
        feedGraph();
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open(/*"workouts"*/);
    }

    private void feedGraph(){
        //fill sample data
        List<Integer> weightList = new ArrayList<>();
        weightList.add(80);
        weightList.add(80);
        weightList.add(70);
        weightList.add(60);
        weightList.add(65);
        weightList.add(75);

        List<Entry> entriesWeight = new ArrayList<>();
        for(int i=0; i<weightList.size(); i++)
        {
            entriesWeight.add(new Entry(i+1, weightList.get(i)));
        }

        LineChart lineChart = (LineChart) findViewById(R.id.progress_chart);
        LineDataSet dataSet = new LineDataSet(entriesWeight, "Weight");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        setGraphAxisStyle(lineChart, weightList.size());
        setDataSetStyle(dataSet);
        lineChart.invalidate();     //refresh chart
    }

    private void setGraphAxisStyle(LineChart lineChart, int dataSize){
        float minYValue = 50f;
        float maxYValue = 150f;

        YAxis right = lineChart.getAxisRight();
        right.setDrawGridLines(false);
        right.setDrawLabels(false);

        YAxis left = lineChart.getAxisLeft();
        left.setAxisMinimum(minYValue);
        left.setAxisMaximum(maxYValue);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(dataSize, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(false);
        lineChart.getDescription().setEnabled(false);
    }

    private void setDataSetStyle(LineDataSet dataSet){
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(Color.GREEN);
        dataSet.setCircleRadius(5f);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setLineWidth(2.5f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }

    public void goToSettings(MenuItem item) {
        Intent i = new Intent(ProgressActivity.this, SettingsActivity.class);
        startActivity(i);
    }
}
