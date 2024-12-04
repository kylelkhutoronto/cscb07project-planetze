package com.example.planetzeapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionsViewHolder> {

    private final HashMap<String, String> suggestions;

    public SuggestionsAdapter(HashMap<String, String> suggestions) {
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public SuggestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item, parent, false);
        return new SuggestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionsViewHolder holder, int position) {
        String currentActivityId = (String) suggestions.keySet().toArray()[position];
        String suggestedActivityId = suggestions.get(currentActivityId);

        holder.currentActivity.setText(String.valueOf(currentActivityId));
        holder.suggestedActivity.setText(String.valueOf(suggestedActivityId));
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    static class SuggestionsViewHolder extends RecyclerView.ViewHolder {
        TextView currentActivity, suggestedActivity;

        public SuggestionsViewHolder(@NonNull View itemView) {
            super(itemView);
            currentActivity = itemView.findViewById(R.id.currentActivity);
            suggestedActivity = itemView.findViewById(R.id.suggestedActivity);
        }
    }
}
