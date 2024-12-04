package com.example.planetzeapplication;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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

        // Verify the view shows an error for empty email address
        verify(view).showEmailError("Email Address cannot be empty");
        // Ensure the model is not called
        verifyNoMoreInteractions(model);
    }

    @Test
    public void testInvalidEmail() {
        LoginPresenter presenter = new LoginPresenter(view, model);

        presenter.onLoginClicked("invalidEmail", "password123");
        // Verify the view shows an error for invalid email address
        verify(view).showEmailError("Invalid Email Address");
        // Ensure the model is not called
        verifyNoMoreInteractions(model);
    }

    @Test
    public void testEmptyPassword() {

        LoginPresenter presenter = new LoginPresenter(view, model);

        presenter.onLoginClicked("testuser@example.com", "");

        // Verify the view shows an error for empty password
        verify(view).showPasswordError("Password cannot be empty");
        // Ensure the model is not called
        verifyNoMoreInteractions(model);

    }

    @Test
    public void testSuccessfulLogin() {
        String email = "testuser@example.com";
        String password = "password123";

        LoginPresenter presenter = new LoginPresenter(view, model);
        ArgumentCaptor<LoginContract.LoginModel> modelCaptor = ArgumentCaptor.
                forClass(LoginContract.LoginModel.class);

        // Simulate a successful login
        doAnswer(invocation -> {
            view.showMessage("Login Successful!");
            view.navigateToMainActivity();
            return null;
        }).when(model).userLogin(eq(email), eq(password), modelCaptor.capture());

        presenter.onLoginClicked(email, password);

        // Verify the login method is called
        verify(model).userLogin(eq(email), eq(password), modelCaptor.capture());

        // Verify the view shows a success message
        verify(view).showMessage("Login Successful!");
        // Verify the view navigates to the main activity
        verify(view).navigateToMainActivity();
    }

    @Test
    public void testUnverifiedEmail() {
        String email = "testuser@example.com";
        String password = "password123";

        LoginPresenter presenter = new LoginPresenter(view, model);
        ArgumentCaptor<LoginContract.LoginModel> modelCaptor = ArgumentCaptor.
                forClass(LoginContract.LoginModel.class);

        // Simulate an unverified email
        doAnswer(invocation -> {
            view.showMessage("Please verify your email!");
            return null;
        }).when(model).userLogin(eq(email), eq(password), modelCaptor.capture());

        presenter.onLoginClicked(email, password);

        // Verify the login method is called
        verify(model).userLogin(eq(email), eq(password), modelCaptor.capture());

        // Verify the view shows a message for an unverified email
        verify(view).showMessage("Please verify your email!");
        verify(view, never()).navigateToMainActivity();
    }

    @Test
    public void testInvalidCredentials() {
        String email = "testuser@example.com";
        String password = "password123";

        LoginPresenter presenter = new LoginPresenter(view, model);
        ArgumentCaptor<LoginContract.LoginModel> modelCaptor = ArgumentCaptor.
                forClass(LoginContract.LoginModel.class);

        // Simulate an invalid login
        doAnswer(invocation -> {
            view.showMessage("Invalid email or password!");
            return null;
        }).when(model).userLogin(eq(email), eq(password), modelCaptor.capture());

        presenter.onLoginClicked(email, password);

        // Verify the login method is called
        verify(model).userLogin(eq(email), eq(password), modelCaptor.capture());

        // Verify the view shows an error message for invalid credentials
        verify(view).showMessage("Invalid email or password!");
        verify(view, never()).navigateToMainActivity();
    }

}