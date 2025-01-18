package com.example.myappxpenses;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button bSignUp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // Ensure this matches your layout file name

        // Initialize the UI components
        etUsername = findViewById(R.id.etFullName); // Ensure this ID is correct in your layout
        etPassword = findViewById(R.id.etPassword); // Ensure this ID is correct in your layout
        bSignUp = findViewById(R.id.bSignUp); // Ensure this ID is correct in your layout

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Set onClick listener for the Sign Up button
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Simple validation for username and password
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user already exists (you can enhance this to check for unique usernames)
        if (databaseHelper.checkUser(username, password)) {
            Toast.makeText(this, "User already exists. Please log in.", Toast.LENGTH_SHORT).show();
        } else {
            // Add user to the database
            databaseHelper.addUser(username, password);
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity
        }
    }
}



















//package com.example.myappxpenses;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SignUpActivity extends AppCompatActivity {
//
//    private EditText etUsername, etPassword;
//    private Button bSignUp;
//    private DatabaseHelper databaseHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        // Initialize the UI components
//        etUsername = findViewById(R.id.etFullName);
//        etPassword = findViewById(R.id.etPassword);
//        bSignUp = findViewById(R.id.bSignUp);
//
//        // Initialize the database helper
//        databaseHelper = new DatabaseHelper(this);
//
//        // Set onClick listener for the Sign Up button
//        bSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                registerUser();
//            }
//        });
//    }
//
//    private void registerUser() {
//        String username = etUsername.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        // Simple validation for username and password
//        if (username.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Check if user already exists (you can enhance this to check for unique usernames)
//        if (databaseHelper.checkUser(username, password)) {
//            Toast.makeText(this, "User already exists. Please log in.", Toast.LENGTH_SHORT).show();
//        } else {
//            // Add user to the database
//            databaseHelper.addUser(username, password);
//            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
//            finish(); // Close this activity
//        }
//    }
//}
