package com.example.myappxpenses;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseXpert.db";
    private static final int DATABASE_VERSION = 3;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Categories table
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_TYPE = "type"; // income/expense

    // Expenses table
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_EXPENSE_ID = "id";
    private static final String COLUMN_EXPENSE_USER_ID = "user_id";
    private static final String COLUMN_EXPENSE_CATEGORY_ID = "category_id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_NOTES = "notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Categories table
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY_NAME + " TEXT, "
                + COLUMN_CATEGORY_TYPE + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        // Prepopulate categories

        // Create Expenses table
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EXPENSE_USER_ID + " INTEGER, "
                + COLUMN_EXPENSE_CATEGORY_ID + " INTEGER, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_NOTES + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_EXPENSE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
                + "FOREIGN KEY(" + COLUMN_EXPENSE_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "))";
        db.execSQL(CREATE_EXPENSES_TABLE);

//        prepopulateCategories();

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Prepopulate categories
    void prepopulateCategories() {
        String[] categories = {"Food", "Transport", "Entertainment", "Health", "Bills"};
        for (String category : categories) {
            addCategory(category, "Expense"); // assuming all are expenses
        }
        // Prepopulate income categories
        String[] incomeCategories = {"Salary", "Business", "Investments", "Other"};
        for (String category : incomeCategories) {
            addCategory(category, "Income"); // assuming all are income
        }
    }

    // Add a new user
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Check user credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean userExists = false;

        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            cursor = db.rawQuery(query, new String[]{username, password});

            if (cursor != null && cursor.moveToFirst()) {
                userExists = true; // User found
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close(); // Avoid potential database lock
            }
        }

        return userExists;
    }


    // Add a new category
    public void addCategory(String name, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_CATEGORY_TYPE, type);
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    // Add a new expense
    public boolean addExpense(int userId, int categoryId, double amount, String date, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_USER_ID, userId);
        values.put(COLUMN_EXPENSE_CATEGORY_ID, categoryId);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_NOTES, notes);
        long result = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return result != -1; // Return true if insertion was successful
    }

    // Update an existing transaction
    public boolean updateTransaction(int transactionId, int userId, String category, double amount, String date, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_USER_ID, userId);
        values.put(COLUMN_EXPENSE_CATEGORY_ID, getCategoryId(category)); // Get category ID based on name
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_NOTES, notes);

        int rowsAffected = db.update(TABLE_EXPENSES, values, COLUMN_EXPENSE_ID + "=?", new String[]{String.valueOf(transactionId)});
        db.close();
        return rowsAffected > 0; // Return true if update was successful
    }

    // Get category ID from category name
    private int getCategoryId(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{COLUMN_CATEGORY_ID},
                COLUMN_CATEGORY_NAME + "=?", new String[]{categoryName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
            cursor.close();
            return categoryId;
        }
        if (cursor != null) cursor.close();
        return -1; // Return -1 if category not found
    }

    // Get total amount based on user ID and category type
    public double getTotalAmount(int userId, String categoryType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_EXPENSES +
                " INNER JOIN " + TABLE_CATEGORIES + " ON " +
                TABLE_EXPENSES + "." + COLUMN_EXPENSE_CATEGORY_ID + " = " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID +
                " WHERE " + COLUMN_EXPENSE_USER_ID + "=? AND " +
                COLUMN_CATEGORY_TYPE + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), categoryType});

        double totalAmount = 0.0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                totalAmount = cursor.getDouble(0);
            }
            cursor.close();
        }
        return totalAmount; // Return total amount for specified category type
    }

    // Get recent transactions for a specific user
    // Get recent transactions for a specific user
    public List<Transaction> getRecentTransactions(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + TABLE_EXPENSES + "." + COLUMN_EXPENSE_ID + ", " +
                COLUMN_EXPENSE_USER_ID + ", " +
                COLUMN_AMOUNT + ", " + COLUMN_DATE + ", " + COLUMN_NOTES + ", " +
                COLUMN_EXPENSE_CATEGORY_ID + " FROM " + TABLE_EXPENSES +
                " WHERE " + COLUMN_EXPENSE_USER_ID + "=? " +
                " ORDER BY " + COLUMN_DATE + " DESC LIMIT 10"; // Retrieve last 10 transactions

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int transactionId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID));
                @SuppressLint("Range") int expenseUserId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_USER_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") String notes = cursor.getString(cursor.getColumnIndex(COLUMN_NOTES));
                @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY_ID)); // Get category ID

                // Create a Transaction object including category ID
                Transaction transaction = new Transaction(transactionId, expenseUserId, categoryId, "", amount, date, notes);
                transactions.add(transaction);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return transactions; // Return list of recent transactions
    }


    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            cursor.close();
            return userId; // Return the user ID
        }
        if (cursor != null) cursor.close();
        return -1; // Return -1 if user not found
    }

    @SuppressLint("Range")
    public String getCategoryName(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String categoryName = null;

        String query = "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORIES +
                " WHERE " + COLUMN_CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
            }
            cursor.close();
        }

        return categoryName; // Return null if category not found
    }

    public void deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the transaction from the database
        db.delete(TABLE_EXPENSES, COLUMN_EXPENSE_ID + " = ?", new String[]{String.valueOf(transactionId)});
        db.close(); // Close the database connection
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CATEGORIES, null, null, null, null, null, null);
    }

    // Add a new transaction (for income or expenses)
    public boolean addTransaction(int userId, String categoryName, double amount, String date, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get category ID from the category name
        int categoryId = getCategoryIdByName(categoryName);
        if (categoryId == -1) {
            return false; // Category not found
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_USER_ID, userId);
        values.put(COLUMN_EXPENSE_CATEGORY_ID, categoryId);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_NOTES, notes);

        long result = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return result != -1;
    }
    private int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_ID},
                COLUMN_CATEGORY_NAME + "=?",
                new String[]{categoryName},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int categoryId = cursor.getInt(0);
            cursor.close();
            return categoryId;
        }
        return -1; // Category not found
    }

    // Get total amount for each category (income/expense)
    public List<CategorySummary> getCategoryWiseSummary(int userId, String categoryType) {
        List<CategorySummary> summaries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_CATEGORY_NAME + ", SUM(" + COLUMN_AMOUNT + ") as total " +
                "FROM " + TABLE_EXPENSES +
                " INNER JOIN " + TABLE_CATEGORIES + " ON " +
                TABLE_EXPENSES + "." + COLUMN_EXPENSE_CATEGORY_ID + " = " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORY_ID +
                " WHERE " + COLUMN_EXPENSE_USER_ID + "=? AND " +
                COLUMN_CATEGORY_TYPE + "=? GROUP BY " + COLUMN_CATEGORY_NAME;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), categoryType});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                @SuppressLint("Range") double total = cursor.getDouble(cursor.getColumnIndex("total"));
                summaries.add(new CategorySummary(categoryName, total));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return summaries; // Return the list of category summaries
    }

}

