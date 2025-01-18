//
//package com.example.myappxpenses;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.Calendar;
//
//public class AddExpenseActivity extends AppCompatActivity {
//
//    private EditText inputAmount, inputDate, inputNotes;
//    private Spinner spinnerCategory;
//    private Button buttonSave, buttonCancel;
//    private DatabaseHelper databaseHelper;
//    private int userId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_expense);
//
//        // Initialize views
//        inputAmount = findViewById(R.id.inputAmount);
//        inputDate = findViewById(R.id.inputDate);
//        inputNotes = findViewById(R.id.inputNotes);
//        spinnerCategory = findViewById(R.id.spinnerCategory);
//        buttonSave = findViewById(R.id.buttonSave);
//        buttonCancel = findViewById(R.id.buttonCancel);
//
//        databaseHelper = new DatabaseHelper(this);
//
//
//
//        userId = getIntent().getIntExtra("USER_ID", -1);
//
//
//        // Handle date picker
//        inputDate.setOnClickListener(view -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(
//                    AddExpenseActivity.this,
//                    (view1, year1, month1, dayOfMonth) -> {
//                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
//                        inputDate.setText(selectedDate);
//                    },
//                    year, month, day);
//            datePickerDialog.show();
//        });
//
//        // Save button click
//        buttonSave.setOnClickListener(view -> saveExpense());
//
//        // Cancel button click
//        buttonCancel.setOnClickListener(view -> finish());
//    }
//
//    private void saveExpense() {
//        String category = spinnerCategory.getSelectedItem().toString();
//        String amountText = inputAmount.getText().toString();
//        String date = inputDate.getText().toString();
//        String notes = inputNotes.getText().toString();
//
//        // Validate input
//        if (amountText.isEmpty() || date.isEmpty() || category.isEmpty()) {
//            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        double amount;
//        try {
//            amount = Double.parseDouble(amountText);
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Assuming userId is passed via intent
////        int userId = getIntent().getIntExtra("userId", -1);
//        Log.d("saveExpenseActivity", "User ID: " + userId);
//
//        if (userId == -1) {
//            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
////        int categoryId = 1;
//        // Save the expense to the database
//        boolean isSuccess = databaseHelper.addTransaction(userId, category, amount, date, notes);
//        if (isSuccess) {
//            Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Failed to save expense. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//
//        // Navigate back
//        finish();
//    }
//}



package com.example.myappxpenses;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText inputAmount, inputDate, inputNotes;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel;
    private DatabaseHelper databaseHelper;
    private int userId;
    private Map<String, Integer> categoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Initialize views
        inputAmount = findViewById(R.id.inputAmount);
        inputDate = findViewById(R.id.inputDate);
        inputNotes = findViewById(R.id.inputNotes);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        databaseHelper = new DatabaseHelper(this);

        // Retrieve user ID from intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Populate categories spinner
        populateCategories();

        // Handle date picker
        inputDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddExpenseActivity.this,
                    (view1, year1, month1, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        inputDate.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Save button click
        buttonSave.setOnClickListener(view -> saveExpense());

        // Cancel button click
        buttonCancel.setOnClickListener(view -> finish());
    }

    private void populateCategories() {
        categoryMap = new HashMap<>();
        ArrayList<String> categoryNames = new ArrayList<>();

        // Get all categories from the database
        Cursor cursor = databaseHelper.getAllCategories();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                // Add category name and ID to the map and list
                categoryMap.put(categoryName, categoryId);
                categoryNames.add(categoryName);
            } while (cursor.moveToNext());
            cursor.close(); // Close the cursor
        } else {
            Toast.makeText(this, "Failed to retrieve categories", Toast.LENGTH_SHORT).show();
        }

        // Set up the spinner with category names
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_spinner_item,
//                categoryNames
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCategory.setAdapter(adapter);
    }

    private void saveExpense() {
        String categoryName = spinnerCategory.getSelectedItem().toString();
        String amountText = inputAmount.getText().toString();
        String date = inputDate.getText().toString();
        String notes = inputNotes.getText().toString();

        // Validate input
        if (amountText.isEmpty() || date.isEmpty() || categoryName.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get category ID from map
        int categoryId = categoryMap.getOrDefault(categoryName, -1);
        if (categoryId == -1) {
            Toast.makeText(this, "Invalid category selected.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the expense to the database
        boolean isSuccess = databaseHelper.addExpense(userId, categoryId, amount, date, notes);
        if (isSuccess) {
            Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Navigate back
        } else {
            Toast.makeText(this, "Failed to save expense. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
