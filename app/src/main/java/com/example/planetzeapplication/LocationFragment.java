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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {

    private TextView errorMessage;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("LocationFragment", "Error: User not authenticated.");
            return view;
        }

        uid = currentUser.getUid();

        errorMessage = view.findViewById(R.id.noneSelected);
        Button nextButton = view.findViewById(R.id.nextButton);
        List<String> countryList = new ArrayList<>();

        try (InputStream inputStream = getResources().openRawResource(R.raw.countries);

             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    countryList.add(line.trim());
                }
        } catch (IOException e) {
            Log.e("Error", "IOException occurred while reading the countries file", e);
        } catch (Exception e) {
            Log.e("Error", "An unexpected error occurred", e);
        }

        Spinner countrySpinner = view.findViewById(R.id.locationSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        nextButton.setOnClickListener(v -> {

            String selectedCountry = countrySpinner.getSelectedItem().toString();

            if ("Choose Answer".equals(selectedCountry)) {
                // One of the spinner wasn't used to select an option
                errorMessage.setVisibility(View.VISIBLE);
            } else {
                // Clear error message, if any
                errorMessage.setVisibility(View.INVISIBLE);

                saveAnswerToFirebase("location",selectedCountry );

                loadFragment(new Q1Fragment());
            }
        });

        // removes none selected message
        countrySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedCountry = parentView.getItemAtPosition(position).toString();

                if (!"Choose Answer".equals(selectedCountry)) {
                    errorMessage.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
            }
        });

        return view;
    }

    private void saveAnswerToFirebase(String questionKey, String answer) {
        SurveyActivity parentActivity = (SurveyActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.saveAnswerToFirebase(questionKey, answer);
        } else {
            Log.e("LocationFragment", "Error: Parent activity is null.");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.survey_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
