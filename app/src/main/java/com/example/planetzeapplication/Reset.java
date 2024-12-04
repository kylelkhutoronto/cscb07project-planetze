package com.example.planetzeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Reset extends AppCompatActivity {
    private TextInputEditText editTextEmail;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email_address3);
        Button resetButton = findViewById(R.id.reset_btn);
        Button backButton = findViewById(R.id.back_btn);

        resetButton.setOnClickListener(v -> passwordReset());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Reset.this, LoginView.class);
            startActivity(intent);
            finish();
        });
    }

    private void passwordReset() {
        String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email Address cannot be empty");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid Email Address");
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Reset.this, "Password reset email sent!",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Reset.this, LoginView.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Reset.this,
                                "Failed to send password reset email!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
