package com.example.myappxpenses;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView valueTotalIncome, valueTotalExpense;
    private PieChart pieChart;
    private RecyclerView transactionRecyclerView;
    private TransactionAdapter transactionAdapter;
    private int userId;

    double totalIncome, totalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);

        // Get the user ID passed from the previous activity
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Initialize Views
        valueTotalIncome = findViewById(R.id.valueTotalIncome);
        valueTotalExpense = findViewById(R.id.valueTotalExpense);
        pieChart = findViewById(R.id.pieChart);
        transactionRecyclerView = findViewById(R.id.transactionRecyclerView);

        // Load data
        loadSummary();
        loadPieChartData();
        loadRecentTransactions();
    }

    private void loadSummary() {
        // Fetch total income and expense from the database
        totalIncome = databaseHelper.getTotalAmount(userId, "income");
        totalExpense = databaseHelper.getTotalAmount(userId, "expense");

        // Update UI with income and expense values
        valueTotalIncome.setText("₹" + String.format("%.2f", totalIncome));
        valueTotalExpense.setText("₹" + String.format("%.2f", totalExpense));
    }

    private void loadPieChartData() {
        List<PieEntry> entries = new ArrayList<>();

        // Fetch category-wise summaries
        List<CategorySummary> incomeSummaries = databaseHelper.getCategoryWiseSummary(userId, "income");
        List<CategorySummary> expenseSummaries = databaseHelper.getCategoryWiseSummary(userId, "expense");

        // Add income categories to Pie Chart
        for (CategorySummary summary : incomeSummaries) {
            entries.add(new PieEntry((float) summary.getTotalAmount(), summary.getCategoryName() + " (Income)"));
        }

        // Add expense categories to Pie Chart
        for (CategorySummary summary : expenseSummaries) {
            entries.add(new PieEntry((float) summary.getTotalAmount(), summary.getCategoryName() + " (Expense)"));
        }

        // If no data to display
        if (entries.isEmpty()) {
            Toast.makeText(this, "No data to display", Toast.LENGTH_SHORT).show();
            pieChart.setData(null);
            pieChart.invalidate();
            return;
        }

        // Set up PieChart data
        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh the chart
    }

    private void loadRecentTransactions() {
        List<Transaction> transactions = databaseHelper.getRecentTransactions(userId);

        // Update each transaction's category and type
        for (Transaction transaction : transactions) {
            String category = databaseHelper.getCategoryName(transaction.getCategoryId());
            transaction.setCategory(category);

            // Set the type based on category ID (income or expense)
            int categoryId = transaction.getCategoryId();
            if (categoryId == 1 || categoryId == 2 || categoryId == 3) {
                // Example: IDs 1, 2, 3 are income categories
                transaction.setType("Income");
            } else if (categoryId == 4 || categoryId == 5 || categoryId == 6 || categoryId == 7) {
                // Example: IDs 4, 5, 6, 7 are expense categories
                transaction.setType("Expense");
            } else {
                transaction.setType("Other");
            }
        }
            transactionAdapter = new TransactionAdapter(this, transactions);
            transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            transactionRecyclerView.setAdapter(transactionAdapter);

    }
}











//package com.example.myappxpenses;
//
//import android.os.Bundle;
//import android.view.SurfaceControl;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
////import com.anychart.AnyChart;
////import com.anychart.chart.common.dataentry.DataEntry;
////import com.anychart.chart.common.dataentry.ValueDataEntry;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReportsActivity extends AppCompatActivity {
//
//    private DatabaseHelper databaseHelper;
//    private TextView valueTotalIncome, valueTotalExpense;
////    private PieChart pieChartView; // Ensure you have the PieChart view in your layout
//    private RecyclerView transactionRecyclerView;
//    private TransactionAdapter transactionAdapter;
//    private int userId;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reports);
//
//        // Initialize Database Helper
//        databaseHelper = new DatabaseHelper(this);
//
//        userId = getIntent().getIntExtra("USER_ID", -1);
//
//
//        // Initialize Views
//        valueTotalIncome = findViewById(R.id.valueTotalIncome);
//        valueTotalExpense = findViewById(R.id.valueTotalExpense);
////        pieChartView = findViewById(R.id.pieChartView); // Ensure this view is in your layout
//        transactionRecyclerView = findViewById(R.id.transactionRecyclerView);
//
//        // Load Data
//        loadSummary();
////        loadPieChart();
//        loadRecentTransactions();
//    }
//
//    private void loadSummary() {
//        double totalIncome = databaseHelper.getTotalAmount(userId, "income");
//        double totalExpense = databaseHelper.getTotalAmount(userId, "expense");
//
//        valueTotalIncome.setText("₹" + String.format("%.2f", totalIncome));
//        valueTotalExpense.setText("₹" + String.format("%.2f", totalExpense));
//    }
//
////    private void loadPieChart() {
////        int userId = 1; // Replace with actual logged-in user ID
////        double totalIncome = databaseHelper.getTotalAmount(userId, "income");
////        double totalExpense = databaseHelper.getTotalAmount(userId, "expense");
////
////        // Data for Pie Chart
////        List<DataEntry> dataEntries = new ArrayList<>();
////        dataEntries.add(new ValueDataEntry("Income", totalIncome));
////        dataEntries.add(new ValueDataEntry("Expense", totalExpense));
////
////        // Configure Pie Chart
////        com.anychart.charts.Pie pie = AnyChart.pie();
////        pie.data(dataEntries);
////        pie.title("Income vs Expense");
////        pie.labels().position("outside");
////        pie.legend().title().enabled(true);
////        pie.legend().title().text("Summary").padding(0d, 0d, 10d, 0d);
////
////        // Set Pie Chart to View
////        pieChartView.setChart(pie);
////    }
//
////    private void loadRecentTransactions() {
////        int userId = 1; // Replace with the current logged-in user ID
////        List<Transaction> transactions = databaseHelper.getRecentTransactions(userId);
////
////        transactionAdapter = new TransactionAdapter(this, transactions);
////        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
////        transactionRecyclerView.setAdapter(transactionAdapter);
////    }
//
//    private void loadRecentTransactions() {
//        List<Transaction> transactions = databaseHelper.getRecentTransactions(userId);
//
//        // Adjust Transaction fetching to include category
//        for (Transaction transaction : transactions) {
//            String category = databaseHelper.getCategoryName(transaction.getCategoryId());
//            transaction.setCategory(category);
//            // Setting the type directly in the adapter
////            transaction.setType(transaction.getAmount() >= 0 ? "Income" : "Expense");
//            int categoryId = transaction.getCategoryId(); // Assuming you have this field
//            if (categoryId == 1 || categoryId == 2 || categoryId == 3) { // Example: IDs 1 and 2 are income categories
//                transaction.setType("Income");
//            } else if (categoryId == 7 || categoryId == 4 || categoryId == 5 || categoryId == 6) { // IDs 3 and 4 are expense categories
//                transaction.setType("Expense");
//            } else {
//                transaction.setType("Other");
//            }
//        }
//
//        TransactionAdapter transactionAdapter = new TransactionAdapter(this, transactions);
//        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        transactionRecyclerView.setAdapter(transactionAdapter);
//    }
//}
