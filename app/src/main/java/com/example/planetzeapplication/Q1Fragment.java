package com.example.planetzeapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Q1Fragment extends Fragment {

    private RadioGroup radioGroup;
    private TextView errorMessage;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q1, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("Q1Fragment", "Error: User not authenticated.");
            return view;
        }

        uid = currentUser.getUid();

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

                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == R.id.option1) {
                    saveAnswerToFirebase("q1","Yes");
                    loadFragment(new Q2Fragment());
                } else if (selectedId == R.id.option2) {
                    saveAnswerToFirebase("q1","No");
                    saveAnswerToFirebase("q2","N/A");
                    saveAnswerToFirebase("q3","N/A");
                    loadFragment(new Q4Fragment());
                }
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
            Log.e("Q1Fragment", "Error: Parent activity is null.");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.survey_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
