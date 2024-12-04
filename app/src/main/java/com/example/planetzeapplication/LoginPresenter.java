package com.example.planetzeapplication;

import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.LoginPresenter{
    LoginContract.LoginView view;
    LoginModel model;

    // Constructor
    public LoginPresenter(LoginContract.LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onLoginClicked(String email, String password) {
        // Check if email is empty
        if (email.isEmpty()) {
            view.showEmailError("Email Address cannot be empty");
            return;
        }

        // Check if email is valid
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if (!(email.matches(regex))) {
            view.showEmailError("Invalid Email Address");
            return;
        }

        // Check if password is empty
        if (password.isEmpty()) {
            view.showPasswordError("Password cannot be empty");
            return;
        }

        // Handle user login
        model.userLogin(email, password, new LoginContract.LoginModel() {
            @Override
            public void onSuccess(FirebaseUser user) {
                if (user.isEmailVerified()) {
                    view.showMessage("Login Successful!");
                    view.navigateToMainActivity();
                } else {
                    view.showMessage("Please verify your email!");
                }
            }

            // Handle incorrect email or password
            @Override
            public void onFailure(Exception exception) {
                view.showMessage("Invalid email or password!");
            }
        });
    }
}

