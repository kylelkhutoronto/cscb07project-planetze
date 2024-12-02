package com.example.planetzeapplication;

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

public class Q15Fragment extends Fragment {


    private RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    private TextView errorMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q15, container, false);

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

                switch (selectedText) {
                    case "Under $50":
                        saveAnswerToFirebase("q15", "under_50");
                        break;
                    case "$50-$100":
                        saveAnswerToFirebase("q15", "50_to_100");
                        break;
                    case "$100-$150":
                        saveAnswerToFirebase("q15", "100_to_150");
                        break;
                    case "$150-$200":
                        saveAnswerToFirebase("q15", "150_to_200");
                        break;
                    case "Over $200":
                        saveAnswerToFirebase("q15", "over_200");
                        break;
                }

                loadFragment(new Q16Fragment());
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
            Log.e("Q15Fragment", "Error: Parent activity is null.");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.survey_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
