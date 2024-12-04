package com.example.planetzeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity {
    private TextInputEditText editTextFullName;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Authentication and Database
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        editTextFullName = findViewById(R.id.full_name);
        editTextEmail = findViewById(R.id.email_address1);
        editTextPassword = findViewById(R.id.password1);
        editTextConfirmPassword = findViewById(R.id.confirm_password);
        Button registerButton = findViewById(R.id.register_btn2);
        TextView loginNowTextView = findViewById(R.id.log_in_now);

        registerButton.setOnClickListener(v -> userRegister());

        loginNowTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, LoginView.class);
            startActivity(intent);
            finish();
        });
    }

    private void userRegister() {
        String fullName = Objects.requireNonNull(editTextFullName.getText()).toString().trim();
        String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(editTextConfirmPassword.getText())
                .toString()
                .trim();

        if (TextUtils.isEmpty(fullName)) {
            editTextFullName.setError("Full Name cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email Address cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Confirm Password cannot be empty");
            return;
        }
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Password does not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // Send email verification
                            user.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            Toast.makeText(Register.this,
                                                    "Please verify your email!",
                                                    Toast.LENGTH_SHORT).show();

                                            saveUserData(user.getUid(), fullName, email);

                                            Intent intent = new Intent(Register.this,
                                                    LoginView.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(Register.this,
                                                    "Failed to send verification email!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

    }

    private void saveUserData(String userId, String fullName, String email) {
        User user = new User(fullName, email);

        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Register.this,
                                "User data successfully saved.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
