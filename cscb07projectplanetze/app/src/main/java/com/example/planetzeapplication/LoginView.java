package com.example.planetzeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class LoginView extends AppCompatActivity implements LoginContract.LoginView{
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private LoginContract.LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email_address2);
        editTextPassword = findViewById(R.id.password2);
        Button loginButton = findViewById(R.id.login_btn2);
        TextView signUpTextView = findViewById(R.id.sign_up_register);
        TextView forgotPasswordTextView = findViewById(R.id.forgot_password);

        presenter = new LoginPresenter(this, new LoginModel());

        loginButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();
            presenter.onLoginClicked(email, password);
        });

        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginView.this, Register.class);
            startActivity(intent);
            finish();
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginView.this, Reset.class);
            startActivity(intent);
            finish();
        });
    }
    @Override
    public void showEmailError(String message) {
        editTextEmail.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        editTextPassword.setError(message);
    }

    @Override
    public void showMessage(String message) {
        // Display a toast message to the user
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(LoginView.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

