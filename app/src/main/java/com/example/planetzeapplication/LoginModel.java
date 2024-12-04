package com.example.planetzeapplication;

import com.google.firebase.auth.FirebaseAuth;

public class LoginModel {
    private final FirebaseAuth mAuth;

    // Constructor
    public LoginModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Method to handle user login
    public void userLogin(String email, String password, LoginContract.LoginModel loginModel) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginModel.onSuccess(mAuth.getCurrentUser());
                    } else {
                        loginModel.onFailure(task.getException());
                    }
                });
    }
}
