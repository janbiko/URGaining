package janbiko.urgaining;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
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

    private static final String ALERT_POSITIVE_BUTTON = "Yes";
    private static final String ALERT_NEGATIVE_BUTTON = "No";

    private int total = 0;
    private TextView totalTextView;
    private WorkoutsDatabase workoutsDB;
    private ExpandableListView workoutsList;
    private android.widget.ExpandableListAdapter listAdapter;
    private ArrayList<String> workouts;
    private HashMap<String, List<String>> exercises;
    private LineChart lineChart;
    private String currentExercise = "";

    private ShareButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAndSetTheme()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_progress);
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

        lineChart = (LineChart) findViewById(R.id.progress_chart);
        lineChart.setVisibility(View.INVISIBLE);



        initDatabase();
        initExpandableList();
        calculateTotal();
        initTotalTextView();

        initShareButton();
    }

    private void initShareButton() {
        shareButton = (ShareButton) findViewById(R.id.share_button);
        shareButton.setVisibility(View.INVISIBLE);
    }

    private void initTotalTextView() {
        totalTextView = (TextView) findViewById(R.id.total_text);
        updateTotalTextView();
    }

    private void updateTotalTextView() {
        totalTextView.setText("" + total + "kg total.");
    }

    private void initExpandableList() {
        workoutsList = (ExpandableListView) findViewById(R.id.workouts_list);

        workoutsDB.open();
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
        workoutsDB.close();

        listAdapter = new ExpandableListAdapter(this, workouts, exercises);

        workoutsList.setAdapter(listAdapter);
        workoutsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                feedGraph(exercises.get(workouts.get(groupPosition)).get(childPosition));
                return false;
            }
        });

        workoutsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
                                           long id) {
                if (ExpandableListView.getPackedPositionType(id) ==
                        ExpandableListView.PACKED_POSITION_TYPE_CHILD){
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    String exerciseName = exercises.get(workouts.get(groupPosition)).
                            get(childPosition);

                    createAlertDialog(exerciseName);

                    return true;
                }

                return false;
            }
        });
    }

    private void createAlertDialog(final String exerciseName) {

        if (!checkIfExerciseAlreadyInTotal(exerciseName)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage("Do you want to add " + exerciseName + " to your total?");
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton(ALERT_POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    addExerciseToTotal(exerciseName);
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
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage("Do you want to remove " + exerciseName + " from your total?");
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton(ALERT_POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    removeExerciseFromTotal(exerciseName);
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
    }

    private void removeExerciseFromTotal(String exerciseName) {
        workoutsDB.open();
        workoutsDB.removeTotalItem(exerciseName);
        workoutsDB.close();
        calculateTotal();
        updateTotalTextView();
    }

    private boolean checkIfExerciseAlreadyInTotal(String exerciseName) {

        workoutsDB.open();
        ArrayList<String> exercisesTotal = workoutsDB.getAllTotalItems();
        workoutsDB.close();
        for (int i = 0; i < exercisesTotal.size(); i++) {
            if (exercisesTotal.get(i).equals(exerciseName)) return true;
        }
        return false;
    }

    private void addExerciseToTotal(String exercise) {
        workoutsDB.open();

        if (workoutsDB.getAllExerciseValuesItems(exercise).size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "There are no values stored for this exercise.", Toast.LENGTH_LONG).show();
        } else {
            workoutsDB.insertTotalItem(exercise);
            calculateTotal();
            updateTotalTextView();
        }

        workoutsDB.close();
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
    }

    private void calculateTotal() {
        total = 0;
        workoutsDB.open();
        ArrayList<String> totalExercises = workoutsDB.getAllTotalItems();
        ArrayList<ArrayList<Float>> latestExValues = new ArrayList<>();
        for (int i = 0; i < totalExercises.size(); i++) {
            latestExValues.add(workoutsDB.getLatestExerciseValuesItem(totalExercises.get(i)));
        }
        for (int i = 0; i < latestExValues.size(); i++) {
            total += epleyFormula(latestExValues.get(i).get(0), latestExValues.get(i).get(1));
        }

        workoutsDB.close();
    }

    private void feedGraph(String exercise){
        if (exercise.equals(currentExercise)) {
            lineChart.setVisibility(View.INVISIBLE);
            shareButton.setVisibility(View.INVISIBLE);
            currentExercise = "";
        } else {
            lineChart.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);
            currentExercise = exercise;
        }


        List<Entry> oneRMEntries = new ArrayList<>();
        workoutsDB.open();
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

            setGraphDescription(lineChart, percIncrease, exercise);
            LineDataSet dataSetLast = new LineDataSet(oneRMEntries, "1RM");

            LineData lineData = new LineData();
            lineData.addDataSet(dataSetLast);
            lineChart.setData(lineData);

            setGraphAxisStyle(lineChart, oneRMEntries.size());
            setDataSetLastStyle(dataSetLast);

            lineChart.invalidate();     //refresh chart
            setFacebookShareContent();
        }
        else {
            lineChart.setVisibility(View.INVISIBLE);
            shareButton.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "No data available.", Toast.LENGTH_LONG).show();
        }
        workoutsDB.close();
    }

    private void setFacebookShareContent() {
        Bitmap image = lineChart.getChartBitmap();

        SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();

        shareButton.setShareContent(content);
    }

    private void setGraphDescription(LineChart lineChart, float percIncrease, String exerciseName) {
        Description description = new Description();
        if (percIncrease >= 0) {
            description.setText("" + exerciseName + ": " + percIncrease +
                    "% 1RM increase since first record");
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

        YAxis right = lineChart.getAxisRight();
        right.setDrawGridLines(false);
        right.setDrawLabels(false);



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
      public void checkAndSetTheme(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        final boolean usePinkTheme = preferences.getBoolean(PREF_PINK_THEME, false);
        final boolean useNormalTheme = preferences.getBoolean(PREF_NORMAL_THEME, false);
        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }
        if (usePinkTheme){
            setTheme(R.style.AppTheme_Pink);
        }
        if (useNormalTheme){
            setTheme(R.style.AppTheme);
        }

    }

}
