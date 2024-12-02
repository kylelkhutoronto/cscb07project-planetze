package com.example.planetzeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SurveyActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Log.e("SurveyActivity", "Error: User not authenticated.");
            finish();
            return;
        }

        uid = currentUser.getUid();
        DatabaseReference myRef = db.getReference("Users").child(uid);

        // Check if the user has completed the questionnaire
        myRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                Boolean completed = snapshot.child("doneSurvey").getValue(Boolean.class);

                if (completed != null && completed) {
                    Log.d("MainActivity", "Survey completed. To the main page.");

                    Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Log.d("MainActivity", "Survey not completed. Start survey.");

                    if (savedInstanceState == null) {
                        loadFragment(new CalcIntroFragment());
                    }
                }
            } else {
                Log.e("Firebase", "Error checking completion status", task.getException());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.survey_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void updateCompletionStatus() {
        DatabaseReference userRef = db.getReference("Users").child(uid);
        userRef.child("doneSurvey").setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SurveyActivity", "doneSurvey status updated.");
                    } else {
                        Log.e("SurveyActivity", "Error updating doneSurvey status.", task.getException());
                    }
                });

        Intent intent = new Intent(SurveyActivity.this, SurveyResultsActivity.class);
        startActivity(intent);
    }

    public void saveAnswerToFirebase(String questionKey, String answer) {
        DatabaseReference userRef = db.getReference("Users")
                .child(uid).child("questionnaire")
                .child("answers");
        userRef.child(questionKey).setValue(answer);
    }

}
