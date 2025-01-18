package com.example.myappxpenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button bLogin, bSignup;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this is the correct layout file

        // Initialize the UI components
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        bSignup = findViewById(R.id.bSignup);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.prepopulateCategories();

        // Set onClick listener for the Login button
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Set onClick listener for the Signup button
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupActivity();
            }
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Simple validation for username and password
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check user credentials in the SQLite database
        if (databaseHelper.checkUser(username, password)) {
            // Successful login
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

            // Navigate to the next activity (e.g., Home Activity)
            Intent intent = new Intent(MainActivity.this, HomeActivity.class); // Change to your home activity
            intent.putExtra("USER_ID", databaseHelper.getUserId(username)); // Pass user ID if needed
            startActivity(intent);
            finish(); // Close this activity
        } else {
            // Login failed
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void openSignupActivity() {
        // Navigate to the Signup Activity
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class); // Change to your sign-up activity
        startActivity(intent);
    }
}
