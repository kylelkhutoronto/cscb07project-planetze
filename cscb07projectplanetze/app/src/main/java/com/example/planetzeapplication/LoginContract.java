package com.example.planetzeapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {
    interface LoginModel {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception exception);
    }

    interface LoginPresenter {
        void onLoginClicked(String email, String password);
    }

    interface LoginView {
        void showEmailError(String message);
        void showPasswordError(String message);
        void showMessage(String message);
        void navigateToMainActivity();
    }
}
