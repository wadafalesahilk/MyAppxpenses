package com.example.myappxpenses;

public class Transaction {
    private int id;
    private int userId;
    private String category;
    private int categoryId; // Add this field for category ID
    private double amount;
    private String date;
    private String notes;
    private String type; // Add this field for transaction type

    public Transaction(int id, int userId, int categoryId, String category, double amount, String date, String notes) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId; // Initialize category ID
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
        this.type = ""; // Initialize with an empty string
    }

    // Getters for each field
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryId() { // Add getter for category ID
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public String getType() { // Add getter for type
        return type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) { // Add setter for type
        this.type = type;
    }
}
