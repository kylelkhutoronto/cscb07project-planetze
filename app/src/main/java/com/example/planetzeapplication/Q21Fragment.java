package com.example.planetzeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Q21Fragment extends Fragment {

    private RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    private TextView errorMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q21, container, false);

        radioGroup = view.findViewById(R.id.options);
        errorMessage = view.findViewById(R.id.noneSelected);
        Button nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                // No radio button is selected
                errorMessage.setVisibility(View.VISIBLE);
            } else {
                // Clear error message, if any
                errorMessage.setVisibility(View.INVISIBLE);

                selectedRadioButton = (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
                String selectedText = selectedRadioButton.getText().toString();

                saveAnswerToFirebase("q21",selectedText);
                updateCompletionStatus();

                Log.e("Q21", "Q21 saved and on to results...");

            }
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                errorMessage.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    private void saveAnswerToFirebase(String questionKey, String answer) {
        SurveyActivity parentActivity = (SurveyActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.saveAnswerToFirebase(questionKey, answer);
        } else {
            Log.e("Q21Fragment", "Error: Parent activity is null.");
        }
    }

    private void updateCompletionStatus() {
        SurveyActivity parentActivity = (SurveyActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.updateCompletionStatus();
        } else {
            Log.e("Q21Fragment", "Error: Parent activity is null.");
        }
    }
}
