package janbiko.urgaining;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Jannik on 20.09.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String DELOAD_KEY = "DeloadValue";
    private static final String THEME_KEY = "ThemeValue";
    private static final String NOTIFICATION_KEY = "NotificationValue";

    private Spinner deloadSpinner;
    private Switch themeSwitch;
    private Switch notificationSwitch;
    private SharedPreferences prefs;


    //Facebook login
    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView textStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_settings);

        initLogin();
        initLogout();

        initPrefs();
        initUI();
    }

    private void initLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.settings_login_button);
        textStatus = (TextView) findViewById(R.id.text_status_login);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String succ = "Success! Result: " + loginResult.toString();
                textStatus.setText(succ);
                // App code
            }

            @Override
            public void onCancel() {
                String canc = "Cancelled!";
                textStatus.setText(canc);
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                String err = "Error! Result: " + exception.toString();
                textStatus.setText(err);
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
        Float[] values = new Float[] {0.95f, 0.9f, 0.85f, 0.8f, 0.75f, 0.7f, 0.65f, 0.6f, 0.55f, 0.5f};
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(SettingsActivity.this,
                R.layout.support_simple_spinner_dropdown_item, values);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        deloadSpinner.setAdapter(spinnerAdapter);
        deloadSpinner.setSelection(spinnerAdapter.getPosition(getDeloadValue(this)));
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
