package com.example.myappxpenses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private final List<Transaction> transactions;
    private final Context context;
    private final DatabaseHelper databaseHelper;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.amount.setText("₹" + String.format("%.2f", transaction.getAmount()));
        holder.date.setText(transaction.getDate());
        holder.notes.setText(transaction.getNotes());
        holder.category.setText(transaction.getCategory());
//        holder.type.setText(transaction.getAmount() >= 0 ? "Income" : "Expense"); // Setting the type directly
        int categoryId = transaction.getCategoryId(); // Assuming you have this field
        if (categoryId == 1 || categoryId == 2 || categoryId == 3) { // Example: IDs 1 and 2 are income categories
            holder.type.setText("Income");
            holder.type.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark)); // Set green color
        } else if (categoryId == 7 || categoryId == 4 || categoryId == 5 || categoryId == 6) { // IDs 3 and 4 are expense categories
            holder.type.setText("Expense");
            holder.type.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark)); // Set red color
        } else {
            holder.type.setText("Other");
        }

        holder.buttonEdit.setOnClickListener(v -> {
            // Create an Intent to navigate to the EditActivity with the transaction details
            Intent intent = new Intent(context, EditTransactionActivity.class);
            intent.putExtra("transactionId", transaction.getId());
            intent.putExtra("amount", transaction.getAmount());
            intent.putExtra("date", transaction.getDate());
            intent.putExtra("notes", transaction.getNotes());
            intent.putExtra("categoryId", transaction.getCategoryId());
//            intent.putExtra("type", transaction.getAmount() >= 0 ? "Income" : "Expense"); // Pass type
//            int categoryId = transaction.getCategoryId(); // Assuming you have this field
            if (categoryId == 1 || categoryId == 2 || categoryId == 3) { // Example: IDs 1 and 2 are income categories
                intent.putExtra("type", "Income");
            } else if (categoryId == 7 || categoryId == 4 || categoryId == 5 || categoryId == 6) { // IDs 3 and 4 are expense categories
                intent.putExtra("type", "Expense");
            } else {
                intent.putExtra("type", "Other");
            }
            context.startActivity(intent);

        });

        holder.buttonDelete.setOnClickListener(v -> {
            // Delete the transaction from the database
            databaseHelper.deleteTransaction(transaction.getId());
            // Remove the transaction from the list and notify the adapter
            transactions.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, date, notes, category, type;
        public ImageButton buttonEdit, buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.textAmount);
            date = itemView.findViewById(R.id.textDate);
            notes = itemView.findViewById(R.id.textNotes);
            category = itemView.findViewById(R.id.textCategory);
            type = itemView.findViewById(R.id.textType);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
