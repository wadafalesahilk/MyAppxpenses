package com.example.myappxpenses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView totalIncomeText, totalExpensesText, balanceText;
    private Button addIncomeButton, addExpenseButton, viewReportsButton;
    private ImageButton menuIcon, logoutButton;

    private DatabaseHelper databaseHelper;
    private int userId;
    double totalIncome,totalExpenses;

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        totalIncomeText = findViewById(R.id.totalIncome);
        totalExpensesText = findViewById(R.id.totalExpenses);
        balanceText = findViewById(R.id.balance);
        addIncomeButton = findViewById(R.id.addIncomeButton);
        addExpenseButton = findViewById(R.id.addExpenseButton);
        viewReportsButton = findViewById(R.id.viewReportsButton);
        menuIcon = findViewById(R.id.menuIcon);
        logoutButton = findViewById(R.id.logoutIcon);

        barChart = findViewById(R.id.barChart);

        databaseHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadFinancialSummary();

        addIncomeButton.setOnClickListener(view -> openAddIncomeActivity());
        addExpenseButton.setOnClickListener(view -> openAddExpenseActivity());
        viewReportsButton.setOnClickListener(view -> openReportsActivity());
        logoutButton.setOnClickListener(view -> logout());
        menuIcon.setOnClickListener(this::showMenu);

//        double totalIncome = databaseHelper.getTotalAmount(userId, "Income");
//        double totalExpenses = databaseHelper.getTotalAmount(userId, "Expense");

        setupBarChart(totalIncome, totalExpenses);
    }

    private void loadFinancialSummary() {
        totalIncome = databaseHelper.getTotalAmount(userId, "income");
        totalExpenses = databaseHelper.getTotalAmount(userId, "expense");
        double balance = totalIncome - totalExpenses;

        totalIncomeText.setText(String.format("₹%.2f", totalIncome));
        totalExpensesText.setText(String.format("₹%.2f", totalExpenses));
        balanceText.setText(String.format("₹%.2f", balance));
    }

    private void openAddIncomeActivity() {
        Intent intent = new Intent(this, AddIncomeActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void openAddExpenseActivity() {
        Intent intent = new Intent(this, AddExpenseActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void openReportsActivity() {
        Intent intent = new Intent(this, ReportsActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_top_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_view_reports:
                    openReportsActivity();
                    return true;
                case R.id.action_settings:
                    openSettingsActivity();
                    return true;
                case R.id.action_logout:
                    logout();
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void logout() {
        finish();
    }

    private void setupBarChart(double totalIncome, double totalExpenses) {
        // Prepare data entries
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) totalIncome));  // Income at index 0
        entries.add(new BarEntry(1, (float) totalExpenses)); // Expenses at index 1

        // Customize Y-Axis (Left Axis)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Start Y-axis from 0
        leftAxis.setAxisMaximum(100000f); // Set maximum value for Y-axis
        leftAxis.setAxisLineWidth(3f); // Line thickness
        leftAxis.setAxisLineColor(Color.BLACK); // Line color
        leftAxis.setLabelCount(10); // Number of labels on Y-axis

        // Disable Right Y-Axis
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false); // Remove right-side values

        // Customize X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Income", "Expenses"}));
        xAxis.setGranularity(1f); // Set granularity to 1
        xAxis.setGranularityEnabled(true); // Enable granularity
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set position to bottom

        // Prepare the dataset
        BarDataSet dataSet = new BarDataSet(entries, "Income vs Expenses");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Set bar colors
        dataSet.setValueTextSize(12f); // Set value text size

        // Create BarData and set it to the chart
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Customize the Bar Chart
        barChart.getDescription().setEnabled(false); // Disable description text
        barChart.setDrawGridBackground(false); // Disable grid background
        barChart.setDrawBorders(false); // Disable chart borders

        // Refresh the chart
        barChart.notifyDataSetChanged();
        barChart.invalidate(); // Redraw the chart
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFinancialSummary();
        setupBarChart(totalIncome, totalExpenses);
    }
}
