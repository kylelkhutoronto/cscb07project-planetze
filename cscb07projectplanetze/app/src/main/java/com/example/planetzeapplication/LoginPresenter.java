package com.example.planetzeapplication;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.LoginPresenter{
    private final LoginContract.LoginView view;
    private final LoginModel model;

    public LoginPresenter(LoginContract.LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onLoginClicked(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            view.showEmailError("Email Address cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            view.showPasswordError("Password cannot be empty");
            return;
        }

        model.userLogin(email, password, new LoginContract.LoginModel() {
            @Override
            public void onSuccess(FirebaseUser user) {
                if (user != null && user.isEmailVerified()) {
                    view.showMessage("Login Successful!");
                    view.navigateToMainActivity();
                } else {
                    view.showMessage("Please verify your email!");
                }
            }

            @Override
            public void onFailure(Exception exception) {
                view.showMessage("Invalid email or password!");
            }
        });
    }
}

