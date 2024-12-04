package com.example.planetzeapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {
    // Model
    interface LoginModel {
        // Model methods
        void onSuccess(FirebaseUser user);
        void onFailure(Exception exception);
    }

    // Presenter
    interface LoginPresenter {
        // Presenter methods
        void onLoginClicked(String email, String password);
    }

    // View
    interface LoginView {
        // View methods
        void showEmailError(String message);
        void showPasswordError(String message);
        void showMessage(String message);
        void navigateToMainActivity();
    }
}
