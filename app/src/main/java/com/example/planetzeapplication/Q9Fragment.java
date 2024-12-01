package com.example.planetzeapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Q9Fragment extends Fragment {
    private TextView errorMessage;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q9, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("Q9Fragment", "Error: User not authenticated.");
            return view;
        }

        uid = currentUser.getUid();

        errorMessage = view.findViewById(R.id.noneSelected);
        Button nextButton = view.findViewById(R.id.nextButton);

        Spinner beefSpinner = view.findViewById(R.id.beefSpinner);
        Spinner porkSpinner = view.findViewById(R.id.porkSpinner);
        Spinner chickenSpinner = view.findViewById(R.id.chickenSpinner);
        Spinner fishSpinner = view.findViewById(R.id.fishSpinner);

        initiateSpinner(beefSpinner, R.array.q9_options);
        initiateSpinner(porkSpinner, R.array.q9_options);
        initiateSpinner(chickenSpinner, R.array.q9_options);
        initiateSpinner(fishSpinner, R.array.q9_options);

        nextButton.setOnClickListener(v -> {

            String selectedBeef = beefSpinner.getSelectedItem().toString();
            String selectedPork = porkSpinner.getSelectedItem().toString();
            String selectedChicken = chickenSpinner.getSelectedItem().toString();
            String selectedFish = fishSpinner.getSelectedItem().toString();

            if ("Choose Answer".equals(selectedBeef) || "Choose Answer".equals(selectedPork) || "Choose Answer".equals(selectedChicken)|| "Choose Answer".equals(selectedFish)) {
                // One of the spinner wasn't used to select an option
                errorMessage.setVisibility(View.VISIBLE);
            } else {
                // Clear error message, if any
                errorMessage.setVisibility(View.INVISIBLE);

                saveAnswerToFirebase("q9Beef",selectedBeef);
                saveAnswerToFirebase("q9Pork",selectedPork);
                saveAnswerToFirebase("q9Chicken",selectedChicken);
                saveAnswerToFirebase("q9Fish",selectedFish);

                loadFragment(new Q10Fragment());
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.survey_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveAnswerToFirebase(String questionKey, String answer) {
        SurveyActivity parentActivity = (SurveyActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.saveAnswerToFirebase(questionKey, answer);
        } else {
            Log.e("Q9Fragment", "Error: Parent activity is null.");
        }
    }

    private void initiateSpinner(Spinner spinner, int stringArrayID) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                stringArrayID, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
