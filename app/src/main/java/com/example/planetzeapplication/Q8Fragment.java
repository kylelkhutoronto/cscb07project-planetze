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

public class Q8Fragment extends Fragment {

    private RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    private TextView errorMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q8, container, false);

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

                saveAnswerToFirebase("q8",selectedText);

                if ("Meat-based (eat all types of animal products)".equals(selectedText)) {
                    loadFragment(new Q9Fragment());
                }
                else {
                    // skips to q10 and survey answers for skipped questions are automatically saved where they will reach return 0
                    saveAnswerToFirebase("q9Beef","Never");
                    saveAnswerToFirebase("q9Pork","Never");
                    saveAnswerToFirebase("q9Chicken","Never");
                    saveAnswerToFirebase("q9Fish","Never");
                    loadFragment(new Q10Fragment());
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
            Log.e("Q8Fragment", "Error: Parent activity is null.");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.survey_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
