package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.models.LoginRequest;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.models.RegisterRequest;
import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private final String EMAIL_KEY = "email";
    private final String USERNAME_KEY = "username";
    private final String FULLNAME_KEY = "fullname";
    private final String PASSWORD_KEY = "password";
    private final String PASSWORD_CONFIRM_KEY = "password_confirm";

    private SessionManager sessionManager;

    private TextInputEditText emailEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText fullnameEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText passwordConfirmEditText;

    private TextInputLayout emailLayout;
    private TextInputLayout usernameLayout;
    private TextInputLayout fullnameLayout;
    private TextInputLayout firstPasswordLayout;
    private TextInputLayout secondPasswordLayout;

    private EditText[] fields;
    private TextInputLayout[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);
        findViews();
        setEmptyTextListeners();
        createPasswordListener();
    }

    public void onClickRegister(View view) {
        if (!validateFields(this, fields, layouts)) return;
        if (!validatePassword()) {
            secondPasswordLayout.setError(getString(R.string.passwords_not_match));
            return;
        }
        RegisterRequest registerRequest = new RegisterRequest(
                Objects.requireNonNull(usernameEditText.getText()).toString(),
                Objects.requireNonNull(fullnameEditText.getText()).toString(),
                Objects.requireNonNull(emailEditText.getText()).toString(),
                Objects.requireNonNull(passwordEditText.getText()).toString()
        );
        makeRegisterRequest(registerRequest);
    }

    private void makeRegisterRequest(RegisterRequest registerRequest) {
        NetworkService
                .getInstance()
                .getJSONApi()
                .register(registerRequest)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            sessionManager.saveToken(loginResponse.getToken());
                            makeIntent(MainActivity.class);
                        } else if (response.code() == 400) {
                            usernameLayout.setError(getString(R.string.username_or_email_already_taken));
                            emailLayout.setError(getString(R.string.username_or_email_already_taken));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("ERROR", Objects.requireNonNull(t.getMessage()));
                    }
                });
    }

    public void onClickLogin(View view) {
        makeIntent(LoginActivity.class);
        finishAffinity();
    }

    private void findViews() {
        emailEditText = findViewById(R.id.email_et);
        usernameEditText = findViewById(R.id.username_et);
        fullnameEditText = findViewById(R.id.fullname_et);
        passwordEditText = findViewById(R.id.password_et);
        passwordConfirmEditText = findViewById(R.id.confirm_password_et);
        emailLayout = findViewById(R.id.email_il);
        usernameLayout = findViewById(R.id.username_il);
        fullnameLayout = findViewById(R.id.fullname_il);
        firstPasswordLayout = findViewById(R.id.password_il);
        secondPasswordLayout = findViewById(R.id.confirm_password_il);

        fields = new EditText[]{emailEditText, usernameEditText, fullnameEditText, passwordEditText, passwordConfirmEditText};
        layouts = new TextInputLayout[]{emailLayout, usernameLayout, fullnameLayout, firstPasswordLayout, secondPasswordLayout};
    }

    private boolean validatePassword() {
        String firstPassword = String.valueOf(passwordEditText.getText());
        String secondPassword = String.valueOf(passwordConfirmEditText.getText());
        return firstPassword.equals(secondPassword);
    }

    private  boolean validateFields(Context context, EditText[] fields, TextInputLayout[] layouts) {
        boolean result = true;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].length() == 0) {
                layouts[i].setError(context.getString(R.string.field_is_required));
                result = false;
            }
        }
        return result;
    }

    private void setEmptyTextListeners() {
        LoginActivity.createEmptyTextListener(this, emailEditText, emailLayout);
        LoginActivity.createEmptyTextListener(this, usernameEditText, usernameLayout);
        LoginActivity.createEmptyTextListener(this, fullnameEditText, fullnameLayout);
        LoginActivity.createEmptyTextListener(this, passwordEditText, firstPasswordLayout);
    }

    private void createPasswordListener() {
        passwordConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(String.valueOf(passwordEditText.getText()))) {
                    secondPasswordLayout.setError(getString(R.string.passwords_not_match));
                } else {
                    secondPasswordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordConfirmEditText.getText().length() != 0) {
                    if (!s.toString().equals(String.valueOf(passwordConfirmEditText.getText()))) {
                        secondPasswordLayout.setError(getString(R.string.passwords_not_match));
                    } else {
                        secondPasswordLayout.setErrorEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void makeIntent(Class<?> destinationClass) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);
        emailEditText.setText(savedInstanceState.getString(EMAIL_KEY));
        usernameEditText.setText(savedInstanceState.getString(USERNAME_KEY));
        fullnameEditText.setText(savedInstanceState.getString(FULLNAME_KEY));
        passwordEditText.setText(savedInstanceState.getString(PASSWORD_KEY));
        passwordConfirmEditText.setText(savedInstanceState.getString(PASSWORD_CONFIRM_KEY));
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        outState.putString(EMAIL_KEY, emailEditText.getText().toString());
        outState.putString(USERNAME_KEY, usernameEditText.getText().toString());
        outState.putString(FULLNAME_KEY, fullnameEditText.getText().toString());
        outState.putString(PASSWORD_KEY, passwordEditText.getText().toString());
        outState.putString(PASSWORD_CONFIRM_KEY, passwordConfirmEditText.getText().toString());
    }
}