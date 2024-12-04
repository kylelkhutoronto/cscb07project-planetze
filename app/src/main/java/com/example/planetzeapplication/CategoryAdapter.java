package com.example.planetzeapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Map.Entry<String, Long>> categoryData;

    public CategoryAdapter(List<Map.Entry<String, Long>> categoryData) {
        this.categoryData = categoryData;
    }

    public void updateData(List<Map.Entry<String, Long>> newData) {
        this.categoryData = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Map.Entry<String, Long> entry = categoryData.get(position);
        holder.categoryName.setText(entry.getKey());
        holder.categoryValue.setText(entry.getValue() + " kg CO2e");
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, categoryValue;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(android.R.id.text1);
            categoryValue = itemView.findViewById(android.R.id.text2);
        }
    }
}
