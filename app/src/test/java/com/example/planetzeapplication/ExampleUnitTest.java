package com.example.planetzeapplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    LoginContract.LoginView view;

    @Mock
    LoginModel model;

    @Test
    public void testEmptyEmail() {

        LoginPresenter presenter = new LoginPresenter(view, model);

        presenter.onLoginClicked("", "password123");

        verify(view).showEmailError("Email Address cannot be empty");
        verifyNoMoreInteractions(model);
    }

    @Test
    public void testInvalidEmail() {
        LoginPresenter presenter = new LoginPresenter(view, model);

        presenter.onLoginClicked("invalidEmail", "password123");

        verify(view).showEmailError("Invalid Email Address");
        verifyNoMoreInteractions(model);
    }

    @Test
    public void testEmptyPassword() {

        LoginPresenter presenter = new LoginPresenter(view, model);

        presenter.onLoginClicked("testuser@example.com", "");

        verify(view).showPasswordError("Password cannot be empty");
        verifyNoMoreInteractions(model);

    }

    @Test
    public void testSuccessfulLogin() {
        LoginPresenter presenter = new LoginPresenter(view, model);
        FirebaseUser mockUser = mock(FirebaseUser.class);
        when(mockUser.isEmailVerified()).thenReturn(true);

        doAnswer(invocation -> {
            LoginContract.LoginModel loginModel = invocation.getArgument(2);
            loginModel.onSuccess(mockUser);
            return null;
        }).when(model).userLogin(eq("test@example.com"), eq("password123"), any());

        presenter.onLoginClicked("test@example.com", "password123");

        verify(view).showMessage("Login Successful!");
        verify(view).navigateToMainActivity();
    }

    @Test
    public void testUnverifiedEmail() {
        LoginPresenter presenter = new LoginPresenter(view, model);
        FirebaseUser mockUser = mock(FirebaseUser.class);
        when(mockUser.isEmailVerified()).thenReturn(false);

        doAnswer(invocation -> {
            LoginContract.LoginModel loginModel = invocation.getArgument(2);
            loginModel.onSuccess(mockUser);
            return null;
        }).when(model).userLogin(eq("test@example.com"), eq("password123"), any());

        presenter.onLoginClicked("test@example.com", "password123");

        verify(view).showMessage("Please verify your email!");
        verify(view, never()).navigateToMainActivity();
    }

    @Test
    public void testInvalidCredentials() {
        LoginPresenter presenter = new LoginPresenter(view, model);
        doAnswer(invocation -> {
            LoginContract.LoginModel loginModel = invocation.getArgument(2);
            loginModel.onFailure(new Exception("Invalid email or password!"));
            return null;
        }).when(model).userLogin(eq("test@example.com"), eq("password123"), any());

        presenter.onLoginClicked("test@example.com", "password123");

        verify(view).showMessage("Invalid email or password!");
        verify(view, never()).navigateToMainActivity();
    }
}