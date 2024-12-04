package com.example.planetzeapplication;

import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.LoginPresenter{
    LoginContract.LoginView view;
    LoginModel model;

    public LoginPresenter(LoginContract.LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onLoginClicked(String email, String password) {
        if (email.isEmpty()) {
            view.showEmailError("Email Address cannot be empty");
            return;
        }
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if (!(email.matches(regex))) {
            view.showEmailError("Invalid Email Address");
            return;
        }
        if (password.isEmpty()) {
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

