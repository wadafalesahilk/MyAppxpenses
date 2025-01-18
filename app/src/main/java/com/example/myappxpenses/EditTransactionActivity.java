package com.example.myappxpenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditTransactionActivity extends AppCompatActivity {

    private EditText editAmount, editDate, editNotes;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupType;
    private Button buttonSave;
    private DatabaseHelper databaseHelper;
    private int transactionId;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        // Initialize UI components
        editAmount = findViewById(R.id.editAmount);
        editDate = findViewById(R.id.editDate);
        editNotes = findViewById(R.id.editNotes);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupType = findViewById(R.id.radioGroupType);
        buttonSave = findViewById(R.id.buttonSave);

        databaseHelper = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("USER_ID", -1);

        // Get the transaction details from the intent
        Intent intent = getIntent();
        transactionId = intent.getIntExtra("transactionId", -1);
        double amount = intent.getDoubleExtra("amount", 0);
        String date = intent.getStringExtra("date");
        String notes = intent.getStringExtra("notes");
        String category = intent.getStringExtra("category");
        String type = intent.getStringExtra("type");

        // Populate the fields with the existing transaction details
        editAmount.setText(String.valueOf(amount));
        editDate.setText(date);
        editNotes.setText(notes);
        setCategory(category);
        setTransactionType(type);

        buttonSave.setOnClickListener(v -> {
            // Validate inputs
            if (validateInputs()) {
                // Update the transaction in the database
                double updatedAmount = Double.parseDouble(editAmount.getText().toString());
                String updatedDate = editDate.getText().toString();
                String updatedNotes = editNotes.getText().toString();
                String updatedCategory = spinnerCategory.getSelectedItem().toString();
                String updatedType = (radioGroupType.getCheckedRadioButtonId() == R.id.radioIncome) ? "Income" : "Expense";

                // Update the transaction in the database
                boolean isUpdated = databaseHelper.updateTransaction(transactionId, userId, updatedCategory, updatedAmount, updatedDate, updatedNotes);
                if (isUpdated) {
                    Toast.makeText(EditTransactionActivity.this, "Transaction updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(EditTransactionActivity.this, "Failed to update transaction", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setCategory(String category) {
        // Assuming you have a predefined list of categories in your spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.income_categories, android.R.layout.simple_spinner_item);
        spinnerCategory.setAdapter(adapter);

        // Set the selected category
        int spinnerPosition = adapter.getPosition(category);
        spinnerCategory.setSelection(spinnerPosition);
    }

    private void setTransactionType(String type) {
        if ("Income".equals(type)) {
            radioGroupType.check(R.id.radioIncome);
        } else {
            radioGroupType.check(R.id.radioExpense);
        }
    }

    private boolean validateInputs() {
        String amount = editAmount.getText().toString();
        String date = editDate.getText().toString();
        String notes = editNotes.getText().toString();

        if (amount.isEmpty() || date.isEmpty() || notes.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
