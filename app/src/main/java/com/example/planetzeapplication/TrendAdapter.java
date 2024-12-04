package com.example.planetzeapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.TrendViewHolder> {

    private List<Map.Entry<Long, Long>> trendData;

    public TrendAdapter(List<Map.Entry<Long, Long>> trendData) {
        this.trendData = trendData;
    }

    public void updateData(List<Map.Entry<Long, Long>> newData) {
        this.trendData = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new TrendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendViewHolder holder, int position) {
        Map.Entry<Long, Long> entry = trendData.get(position);
        holder.date.setText("Date: " + entry.getKey());
        holder.emissionValue.setText("Emissions: " + entry.getValue() + " kg CO2e");
    }

    @Override
    public int getItemCount() {
        return trendData.size();
    }

    static class TrendViewHolder extends RecyclerView.ViewHolder {
        TextView date, emissionValue;

        public TrendViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(android.R.id.text1);
            emissionValue = itemView.findViewById(android.R.id.text2);
        }
    }
}
