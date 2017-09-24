package janbiko.urgaining;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Jannik on 20.09.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String DELOAD_KEY = "DeloadValue";
    private static final String THEME_KEY = "ThemeValue";
    private static final String NOTIFICATION_KEY = "NotificationValue";
    private static final String MAX_TRAINING_SESSIONS_KEY = "MaxTrainingSessions";

    private Spinner deloadSpinner;
    private Switch themeSwitch;
    private Switch notificationSwitch;
    private Spinner maxTrainingSessionsSpinner;
    private SharedPreferences prefs;
    private WorkoutsDatabase workoutsDB;


    //Facebook login
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_settings);

        initLogin();
        initLogout();

        initDatabase();
        initPrefs();
        initUI();
        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "janbiko.urgaining",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
    }

    private void initDatabase() {
        workoutsDB = new WorkoutsDatabase(this);
    }

    private void initLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.settings_login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initLogout() {

        Button logOutButton = (Button) findViewById(R.id.settings_logout_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
            }
        });
    }

    private void initPrefs() {
        prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void initUI() {
        initSpinner();
        initSwitches();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.navigation_progress:
                        Intent iP = new Intent(SettingsActivity.this, ProgressActivity.class);
                        startActivity(iP);
                        break;
                    case R.id.navigation_workout:
                        Intent iW = new Intent(SettingsActivity.this, WorkoutsActivity.class);
                        startActivity(iW);
                        break;
                    case R.id.navigation_settings:
                        break;
                }
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initSwitches() {
        themeSwitch = (Switch) findViewById(R.id.dark_theme_switch);
        themeSwitch.setChecked(getThemeCheckerValue(this));

        notificationSwitch = (Switch) findViewById(R.id.notification_switch);
        notificationSwitch.setChecked(getNotificationCheckerValue(this));
    }

    private void initSpinner() {
        deloadSpinner = (Spinner) findViewById(R.id.deload_spinner);
        Float[] values = new Float[] {0.95f, 0.9f, 0.85f, 0.8f, 0.75f, 0.7f, 0.65f, 0.6f, 0.55f,
                0.5f};
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(SettingsActivity.this,
                R.layout.support_simple_spinner_dropdown_item, values);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        deloadSpinner.setAdapter(spinnerAdapter);
        deloadSpinner.setSelection(spinnerAdapter.getPosition(getDeloadValue(this)));

        maxTrainingSessionsSpinner = (Spinner) findViewById(R.id.max_training_sessions_spinner);
        String[] vals = new String[] {"5", "10", "15", "20", "25", "50", "100", "All"};
        ArrayAdapter spinnerAdapter2 = new ArrayAdapter<>(SettingsActivity.this,
                R.layout.support_simple_spinner_dropdown_item, vals);
        spinnerAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        maxTrainingSessionsSpinner.setAdapter(spinnerAdapter2);
        maxTrainingSessionsSpinner.setSelection(spinnerAdapter2.getPosition(
                getMaxTrainingSessions(this)));
    }

    private void saveDeloadValue(String s) {
        float value = Float.parseFloat(s);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(DELOAD_KEY, value);
        editor.apply();
    }

    public float getDeloadValue(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getFloat(DELOAD_KEY, 0.8f);
    }

    private void saveMaxTrainingSessions(String s) {
        deleteRedundantExerciseValues();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MAX_TRAINING_SESSIONS_KEY, s);
        editor.apply();
    }

    public String getMaxTrainingSessions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(MAX_TRAINING_SESSIONS_KEY, "All");
    }

    private void saveThemeCheckerValue() {
        boolean value = themeSwitch.isChecked();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(THEME_KEY, value);
        editor.apply();
    }

    static public boolean getThemeCheckerValue(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(THEME_KEY, true);
    }

    private void saveNotificationCheckerValue() {
        boolean value = notificationSwitch.isChecked();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(NOTIFICATION_KEY, value);
        editor.apply();
    }

    static public boolean getNotificationCheckerValue(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(NOTIFICATION_KEY, true);
    }

    private void savePreferences() {
        saveDeloadValue(deloadSpinner.getSelectedItem().toString());
        saveThemeCheckerValue();
        saveNotificationCheckerValue();
        saveMaxTrainingSessions(maxTrainingSessionsSpinner.getSelectedItem().toString());
    }

    private void deleteRedundantExerciseValues() {
        // deletes oldest exercise values, if more than the max amount of exercise values exist
        // in the database

        workoutsDB.open();
        if (!getMaxTrainingSessions(this).equals("All")) {
            ArrayList<String> exercises = new ArrayList<>();
            int maxTrainingSessions = Integer.parseInt(getMaxTrainingSessions(this));

            for (int i = 0; i < workoutsDB.getAllExerciseItems().size(); i++) {
                exercises.add(workoutsDB.getAllExerciseItems().get(i).getName());
            }

            for (int i = 0; i < exercises.size(); i++) {
                for (int j = 0; j < workoutsDB.getAllExerciseValuesItems(exercises.get(i)).size();
                     j++) {
                    ArrayList<ArrayList<Float>> exVals = workoutsDB.
                            getAllExerciseValuesItems(exercises.get(i));

                    if (exVals.size() > maxTrainingSessions) {
                        for (int k = 0; k < exVals.size(); k++) {
                            if (exVals.size() == maxTrainingSessions) break;
                            exVals.remove(0);
                        }
                    }

                    workoutsDB.removeExerciseValues(exercises.get(i));
                    long timeStamp = System.currentTimeMillis() / 100;

                    for (int k = 0; k < exVals.size(); k++) {
                        workoutsDB.insertExerciseValuesItem(exercises.get(i), exVals.get(k),
                                timeStamp + k);
                    }
                }
            }
        }
        workoutsDB.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePreferences();
    }
}
