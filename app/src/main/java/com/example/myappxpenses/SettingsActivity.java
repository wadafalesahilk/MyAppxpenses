package com.example.myappxpenses;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchDarkMode;
    private Button bUpdateUserInfo;
    private TextView tvLogout;

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DARK_MODE = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        bUpdateUserInfo = findViewById(R.id.bUpdateUserInfo);
        tvLogout = findViewById(R.id.tvLogout);

        loadPreferences();

        bUpdateUserInfo.setOnClickListener(v -> updateUserInfo());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference(KEY_DARK_MODE, isChecked));
        tvLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        etUsername.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        etPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
        switchDarkMode.setChecked(sharedPreferences.getBoolean(KEY_DARK_MODE, false));
    }

    private void updateUserInfo() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        savePreference(KEY_USERNAME, username);
        savePreference(KEY_PASSWORD, password);

        Toast.makeText(this, "User information updated", Toast.LENGTH_SHORT).show();
    }

    private void savePreference(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        finish();
    }
}
