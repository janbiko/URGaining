package janbiko.urgaining;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button workoutsButton;
    private Button progressButton;
    private Button settingsButton;
    private Button quitButton;
    private TextView totalTextView;

    public WorkoutsDatabase workoutsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatabase();
        initUI();
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
        workoutsDB.open(/*"workouts"*/);
    }

    private void initUI() {
        initButtons();
        initTextViews();
    }

    private void initTextViews() {
        totalTextView = (TextView) findViewById(R.id.total_textview);
    }

    private void initButtons() {
        workoutsButton = (Button) findViewById(R.id.workouts_button);
        workoutsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WorkoutsActivity.class);
                startActivity(i);
            }
        });

        progressButton = (Button) findViewById(R.id.progress_button);
        progressButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Progress", Toast.LENGTH_SHORT).show();
            }
        });

        settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
            }
        });

        quitButton = (Button) findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });
    }
}
