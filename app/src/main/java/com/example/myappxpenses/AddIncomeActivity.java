//
//package com.example.myappxpenses;
//
//import android.app.DatePickerDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.Calendar;
//
//public class AddIncomeActivity extends AppCompatActivity {
//
//    private EditText inputAmount, inputDate, inputNotes;
//    private Spinner spinnerCategory;
//    private Button buttonSave, buttonCancel;
//    private DatabaseHelper databaseHelper;
//
//    private int userId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_income);
//
//        // Initialize views
//        inputAmount = findViewById(R.id.inputIncomeAmount);
//        inputDate = findViewById(R.id.inputIncomeDate);
//        inputNotes = findViewById(R.id.inputIncomeNotes);
//        spinnerCategory = findViewById(R.id.spinnerIncomeCategory);
//        buttonSave = findViewById(R.id.buttonIncomeSave);
//        buttonCancel = findViewById(R.id.buttonIncomeCancel);
//
//        // Initialize DatabaseHelper
//        databaseHelper = new DatabaseHelper(this);
//
//        userId = getIntent().getIntExtra("USER_ID", -1);
//
////        databaseHelper.addCategory("Salary", "income");
////        databaseHelper.addCategory("Business", "income");
////        databaseHelper.addCategory("Investments", "income");
////        databaseHelper.addCategory("Other", "income");
////        databaseHelper.addCategory("Food", "expense");
////        databaseHelper.addCategory("Transport", "expense");
////        databaseHelper.addCategory("Utilities", "expense");
////        databaseHelper.addCategory("Entertainment", "expense");
////        databaseHelper.addCategory("Other", "expense");
//
//
//        // Set up date picker for inputDate
//        inputDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog();
//            }
//        });
//
//        // Handle Save button click
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveIncome();
//            }
//        });
//
//        // Handle Cancel button click
//        buttonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish(); // Close the activity
//            }
//        });
//    }
//
//    private void showDatePickerDialog() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                (view, year1, month1, dayOfMonth) -> {
//                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
//                    inputDate.setText(selectedDate);
//                }, year, month, day);
//        datePickerDialog.show();
//    }
//
//    private void saveIncome() {
//        // Get input values
//        String category = spinnerCategory.getSelectedItem().toString();
//        String amountStr = inputAmount.getText().toString();
//        String date = inputDate.getText().toString();
//        String notes = inputNotes.getText().toString();
//
//        // Validate inputs
//        if (amountStr.isEmpty() || date.isEmpty()) {
//            Toast.makeText(this, "Amount and Date are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        double amount;
//        try {
//            amount = Double.parseDouble(amountStr);
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Assuming user ID is fetched from session or passed via Intent
////        int userId = getIntent().getIntExtra("userId", -1);// Replace this with the actual user ID
////        int categoryId = 1;
//
//        // Save income to database
//        boolean isAdded = databaseHelper.addTransaction(userId, category, amount, date, notes);
//
//        // Show success message based on the result of the addTransaction
//        if (isAdded) {
//            Toast.makeText(this, "Income added successfully", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Failed to add income", Toast.LENGTH_SHORT).show();
//        }
//
//        // Clear inputs
//        inputAmount.setText("");
//        inputDate.setText("");
//        inputNotes.setText("");
//        spinnerCategory.setSelection(0);
//    }
//}





package com.example.myappxpenses;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddIncomeActivity extends AppCompatActivity {

    private EditText inputAmount, inputDate, inputNotes;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel;
    private DatabaseHelper databaseHelper;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        // Initialize views
        inputAmount = findViewById(R.id.inputIncomeAmount);
        inputDate = findViewById(R.id.inputIncomeDate);
        inputNotes = findViewById(R.id.inputIncomeNotes);
        spinnerCategory = findViewById(R.id.spinnerIncomeCategory);
        buttonSave = findViewById(R.id.buttonIncomeSave);
        buttonCancel = findViewById(R.id.buttonIncomeCancel);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("USER_ID", -1);

        // Set up date picker
        inputDate.setOnClickListener(v -> showDatePickerDialog());

        // Set save button listener
        buttonSave.setOnClickListener(v -> saveIncome());

        // Set cancel button listener
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    inputDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveIncome() {
        // Get input values
        String category = spinnerCategory.getSelectedItem().toString(); // Category name from spinner
        String amountStr = inputAmount.getText().toString();
        String date = inputDate.getText().toString();
        String notes = inputNotes.getText().toString();

        // Validate inputs
        if (amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Amount and Date are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save income to database using the new method
        boolean isAdded = databaseHelper.addTransaction(userId, category, amount, date, notes);

        // Show success message based on the result of the addTransaction
        if (isAdded) {
            Toast.makeText(this, "Income added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity after saving
        } else {
            Toast.makeText(this, "Failed to add income", Toast.LENGTH_SHORT).show();
        }

        // Clear inputs
        inputAmount.setText("");
        inputDate.setText("");
        inputNotes.setText("");
        spinnerCategory.setSelection(0);
    }
}
